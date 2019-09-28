package chain_source;

/* @author Diana Bental
 * @author Tanya Howden
 * @author Sabina Jedrzejczyk
 * 
 * Date July 2019
 * Modified July 2019
 */

import java.io.*;
import java.util.*;

import it.unitn.disi.smatch.CLI;
import it.unitn.disi.smatch.IMatchManager;
import it.unitn.disi.smatch.MatchManager;
import it.unitn.disi.smatch.SMatchException;
import it.unitn.disi.smatch.data.mappings.IContextMapping;
import it.unitn.disi.smatch.data.mappings.IMappingElement;
import it.unitn.disi.smatch.data.trees.INode;
import it.unitn.disi.smatch.filters.SPSMMappingFilter;
//imports from CLI.java in s-match-utils package
import it.unitn.disi.common.DISIException;
import it.unitn.disi.smatch.data.mappings.IContextMapping;
import it.unitn.disi.smatch.data.trees.IBaseContext;
import it.unitn.disi.smatch.data.trees.IContext;
import it.unitn.disi.smatch.data.trees.INode;
import it.unitn.disi.smatch.loaders.context.IContextLoader;
import it.unitn.disi.smatch.matchers.structure.tree.spsm.ted.TreeEditDistance;
import it.unitn.disi.smatch.matchers.structure.tree.spsm.ted.utils.impl.MatchedTreeNodeComparator;
import it.unitn.disi.smatch.matchers.structure.tree.spsm.ted.utils.impl.WorstCaseDistanceConversion;
import it.unitn.disi.smatch.oracles.wordnet.InMemoryWordNetBinaryArray;
import it.unitn.disi.smatch.oracles.wordnet.WordNet;
import it.unitn.disi.smatch.renderers.context.IContextRenderer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/*
 * This class is responsible for initially taking in both the 
 * target and source schema before calling SPSM directly with these schemas and not via a bash
 * script like the Call_SPSM class does
 * it will then store the results as an ArrayList of Match_Struc objects
 * 
 * This class is tested in SPSM_Test_Cases.java & SPSM_Filter_Results_Test_Cases.java
 * 
 */
public class Direct_SPSM{
	
	private static final Logger log = LoggerFactory.getLogger(Direct_SPSM.class);

	static {
        String log4jConf = System.getProperty("log4j.configuration");
        if (null != log4jConf) {
            PropertyConfigurator.configure(log4jConf);
        }
    }
	
    //default configuration
    public static final String DEFAULT_CONFIG_FILE_NAME = "/it/unitn/disi/smatch/s-match.xml";

    //configuration files used in s-match and spsm
    public static final String FUNCTION_2_XML_CONGIF_FILE = "spsm/s-match/conf/s-match-Function2XML.xml";
    public static final String SMATCH_SPSM_PROLOG_CONFIG_FILE = "spsm/s-match/conf/s-match-spsm-prolog.xml";
	
	
	private String[] targetList;
	private static int spsmCallCounter = 0;
	private static int spsmCrashCounter = 0;
	private static int spsmSuccessCounter = 0;
	private static int spsmNoMatchCounter = 0;
	
	//main method used for testing purposes during dev only
	public static void main (String[] args){
		Direct_SPSM classInst = new Direct_SPSM();
		
		ArrayList<Match_Struc> result = new ArrayList<Match_Struc>();
//		String source = "waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
//		String target = "waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		
//		String source = "car(make,model,year)";
//		String target = "vehicle(model,year,make)";
		
		String source = "car(make, model, year, serialNumber)";
		String target = "automobile(make, model, year, serialNum)";
		
		
		//String source="author(name)";
		//String target="document(title,author) ; author(name,document) ; reviewAuthor(firstname,lastname,review)";
		result = classInst.callSPSM(result,source,target);
		
		//then lets see if we can read the results from our new structure
		//and print them out to the console for the user
		System.out.println("\nSource: " + source);
		System.out.println("Target: " + target);
		
		if(result == null){
			System.out.println("No Results Returned from SPSM!");
		}else if(result.size() == 0){
			System.out.println("No Matches.");
		}else{
			for(int i = 0 ; i < result.size() ; i++){
				Match_Struc currMatch = result.get(i);
				System.out.println("\nResult Number "+(i+1)+": "+currMatch.getDatasetSchema());
				System.out.println("Has a similarity of "+currMatch.getSimValue());
				System.out.println("And " + currMatch.getNumMatchComponents() + " matche(s)");

				if(currMatch.getNumMatchComponents() > 0){
					System.out.println("These matches are: ");

					ArrayList<String[]> indivMatches = currMatch.getMatchComponents();
					for(int j = 0 ; j < indivMatches.size() ; j++){
						String[] currIndivMatch = indivMatches.get(j);
						System.out.println(currIndivMatch[0] + "," + currIndivMatch[1] + "," + currIndivMatch[2]);
					}
				}
			}
			
			
		}	
	}
	
	// Call SPSM on the source schema and one or more target schemas.
	// If the schemas are not passed as a parameter then get them from the user
	
	public ArrayList<Match_Struc> callSPSM(ArrayList<Match_Struc> results, String srcSchema, String targetSchemas){
		//if we haven't been passed the schemas as params
		//then get them through the command line
		if(srcSchema==null && targetSchemas==null){
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			try{
				//get source schema through command line
				System.out.println("Please enter source schema: ");
				while((srcSchema=reader.readLine()).equals("")){
				}
				
				//then get target schema(s)
				//separated by a ; for now for easy manipulation
				System.out.println("Please enter target schema(s) seperated by a ';' : ");
				while((targetSchemas=reader.readLine()).equals("")){
				}
				
				reader.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		try{
			//then write the source schema to file
			PrintWriter srcWriter = new PrintWriter("inputs/source.txt","UTF-8");
			srcWriter.write(srcSchema);
			srcWriter.close();
			
			//then save the target schemas to our array
			//by splitting string at ';'
			targetList = targetSchemas.split(";");
			
			//then for each of the target schemas
			//we want to call SPSM and store the results
			String targetSchema="";
			for(int i = 0 ; i < targetList.length ; i++){
				//write the current target schema to file
				PrintWriter targetWriter = new PrintWriter("inputs/target.txt","UTF-8");
				targetSchema = targetList[i].trim();
				targetWriter.write(targetSchema);
				targetWriter.close();
				
				if(srcSchema.equals("") || targetSchema.equals("")){
					//if we have either an empty source or target
					//then move on because that will not return any
					//results
					continue;
				}else{
					//then call SPSM & store results
					System.out.println("Calling SPSM with Source Schema: " + srcSchema) ;
					System.out.println("Calling SPSM with Target Schema: " + targetSchema) ;
					//calls spsm directly
					results = callSPSMDirectly(results,targetSchema);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return results;
		
	}
	
	/* calls spsm directly by using appropriate methods 
	 * works the same way as with the bash script
	 * 
	 * */
	public ArrayList<Match_Struc> callSPSMDirectly(ArrayList<Match_Struc> results, String currTarget){
		String configFile = null;
		
		//first clean the files
		try {
			new PrintWriter("outputs/serialised-results.ser").close();
			new PrintWriter("outputs/result-spsm.txt").close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* These calls are the same as in the bash script file 
		 * with the same inputs, outputs and config files used
		 * 
		 * */
		convert("inputs/source.txt", "inputs/source.xml", FUNCTION_2_XML_CONGIF_FILE);
		convert("inputs/target.txt", "inputs/target.xml", FUNCTION_2_XML_CONGIF_FILE);
		offline("inputs/source.xml", "inputs/source.xml", configFile);
		offline("inputs/target.xml", "inputs/target.xml", configFile);
		online("inputs/source.xml", "inputs/target.xml", "outputs/result-spsm.txt", SMATCH_SPSM_PROLOG_CONFIG_FILE);
		results = readSerialisedResults(results,currTarget);
		
		return results;
		
	}
	
	/* this method has been adapted from the CMD_CONVERT located
	 * in the CLI.java (Command Line Interface) from the
	 * s-match-utils/src/main/java it.unitn.disi.smatch package
	 * 
	 * convert reads an input file and writes it out into an output file
	 * this essentially converts the text file into an xml file
	 *
	 * the xml file is needed for the tree edit distance algorithm
	 * 
	 * this method is used twice; once for the source text file and the other for target text file
	 * */
	public void convert(String inputFile, String outputFile, String configFile){
		IMatchManager mm = createMatchManager(configFile);
                IBaseContext ctxSource;
                try {
                	ctxSource = mm.loadContext(inputFile);
					mm.renderContext(ctxSource, outputFile);
				} catch (SMatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	/* this method has been adapted from the CMD_OFFLINE located
	 * in the CLI.java (Command Line Interface) from the
	 * s-match-utils/src/main/java it.unitn.disi.smatch package
	 * 
	 * offline reads in an input file, preprocess it and writes it out into an output file
	 * 
	 * essentially the offline method performs the first two steps of the semantic matching algorithm
	 * that is; computing concepts for all labels L in two trees (step 1) and computing concepts for all nodes N
	 * in the two trees (step 2)
	 * 
	 * this method is used twice; once for the source xml file and the other for the target xml file
	 * */
	public void offline(String inputFile, String outputFile, String configFile){
		IMatchManager mm = createMatchManager(configFile);
		if (mm.getContextLoader() instanceof IContextLoader && mm.getContextRenderer() instanceof IContextRenderer) {
            IContext ctxSource;
			try {
				ctxSource = (IContext) mm.loadContext(inputFile);
				mm.offline(ctxSource);
	            mm.renderContext(ctxSource, outputFile);
			} catch (SMatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
           log.warn("To preprocess a mapping, use context loaders and renderers support IContextLoader and IContextRenderer.");
        }
	}
	
	/* this method has been adapted from the CMD_ONLINE located
	 * in the CLI.java (Command Line Interface) from the
	 * s-match-utils/src/main/java it.unitn.disi.smatch package
	 * 
	 * online reads in source and target files, runs matching and writes the output file
	 * 
	 * essentially the online method performs the last two steps of the semantic matching algorithm,
	 * that is; for all pairs of labels in the two trees, compute the relations among them 
	 * (Step 3, Element Level Matching) and for all pairs of nodes in the two trees, 
	 * compute relations among them (Step 4, Structure Level Matching)
	 * 
	 * this method is used once, and it is where the Tree Edit Distance algorithm is used
	 * to calculate the similarity score and where the user input takes place after the first run
	 * (this is done inside the MatchManager.java located in the s-match-core/src/main/java
	 * in the it.unitn.disi.smatch package)
	 * 
	 * */
	public void online(String sourceFile, String targetFile, String outputFile, String configFile){
		IMatchManager mm = createMatchManager(configFile);
		 if (mm.getContextLoader() instanceof IContextLoader) {
             try {
            	 IContext ctxSource = (IContext) mm.loadContext(sourceFile);
                 IContext ctxTarget = (IContext) mm.loadContext(targetFile);
                 //original mappings not for changing
                 IContextMapping<INode> unchangedResult = mm.online(ctxSource, ctxTarget);
                 //original mappings used for user input
                 IContextMapping<INode> userResult = mm.online(ctxSource, ctxTarget);
                 
                 //call to display matches and get user input
                // displayMatches(userResult);
                 
                 mm.renderMapping(unchangedResult, "outputs/unchanged-mappings.txt");
				 mm.renderMapping(userResult, outputFile);
			} catch (SMatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         } else {
        	 log.warn("To match contexts, use context loaders supporting IContextLoader.");
         }
	}
	
	/* method for creating a MatchManager object
	 * taken from the CLI.java class located in the
	 * s-match-utils/src/main/java package
	 * */
	public static IMatchManager createMatchManager(String configFileName) {
	        IMatchManager mm;
	        if (configFileName == null) {
	            mm = MatchManager.getInstanceFromResource(DEFAULT_CONFIG_FILE_NAME);
	            log.info("Using resource config file: " + DEFAULT_CONFIG_FILE_NAME);
	        } else {
	            mm = MatchManager.getInstanceFromConfigFile(configFileName);
	        }
	        return mm;
	 }
	
	 /*
     * this needs to be slightly changed so that it sends the mapping to the GUI
     * and then the GUI displays it in the format 
     * 
     * source relation target   
     * source relation target
     * source relation target 
     * 
     * with a drop down option for each of them that allows the user to change the
     * relation to what they want, from one of the following; !,<,>,=
     * 
     * instead of the current implementation which uses a scanner, console and a simple for loop
     * to display and change the mappings
     * 
     * */
    public void displayMatches(IContextMapping<INode> mapping){
    	for(IMappingElement elm: mapping){
			System.out.println(elm.getSource() + " " + elm.getRelation() + " " + elm.getTarget());
		}
    	System.out.println("Similarity Score: " + mapping.getSimilarity());
    	System.out.println("Would you like to make changes to any of the mappings?");
    	Scanner scanner = new Scanner(System.in);
    	String edit = scanner.nextLine();
		if(edit.equals("yes")){
			makeChanges(mapping);
		}
    }
    
    /* This will also need to be slightly changed for the GUI as this will be what the
     * drop down menu does (so there won't be a scanner but rather an if statement?)
     *
     **/
    
    public void makeChanges(IContextMapping<INode> mapping){
    	Scanner scanner = new Scanner(System.in);
    	for(IMappingElement elm: mapping){
    		INode source = (INode) elm.getSource();
    		INode target = (INode) elm.getTarget();
    		char newRelation = scanner.next().charAt(0);
			mapping.setRelation(source, target, newRelation);
		}
    	
		double newSimilarity = recalculateSimilarity(mapping);
    	mapping.setSimilarity(newSimilarity);
    	displayMatches(mapping);
    }
    
    /*duplicated method from the SPSMMappingFilter class
     * this takes in the mapping and recalculates the similarity score
     * by putting it through the tree edit distance algorithm
     * */
    
    protected double recalculateSimilarity(IContextMapping<INode> mapping) {
        MatchedTreeNodeComparator mntc = new MatchedTreeNodeComparator(mapping);
        TreeEditDistance tde = new TreeEditDistance(mapping.getSourceContext(), mapping.getTargetContext(), mntc, new WorstCaseDistanceConversion());

        tde.calculate();
        double ed = tde.getTreeEditDistance();

        return 1 - (ed / Math.max(mapping.getSourceContext().nodesCount(), mapping.getTargetContext().nodesCount()));
    }
   
	
	//then record the results from the .ser file returned from spsm
	@SuppressWarnings("unchecked")
	public ArrayList<Match_Struc> readSerialisedResults(ArrayList<Match_Struc> results,String targetSchema){
		// System.out.println("Reading Results from SPSM");
		
		//get the serialised content from the .ser file
		//and store it in a IContextMapping object var
		IContextMapping<INode> mapping=null;
		
		//read in the object
		try{
			File resFile = new File("outputs/serialised-results.ser");
			FileInputStream fIn = new FileInputStream(resFile);
			ObjectInputStream inStream = new ObjectInputStream(fIn);
			
			mapping = (IContextMapping<INode>) inStream.readObject();	
			
			inStream.close();
			fIn.close();
			// System.out.println("Successfully read back results.");
			
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("SPSM crash? - error reading back results from  SPSM, returning no additional results.");
			spsmCrashCounter++ ;
			return results; // DB (instead of returning null)
		}
		
		return parseMatchObject(results,targetSchema,mapping);
	}
	
	public ArrayList<Match_Struc> parseMatchObject(ArrayList<Match_Struc> results, String targetSchema, IContextMapping<INode> mapping){
		//start picking data out of the object for this target
	
		Match_Struc newMatch;
	
		double similarity = mapping.getSimilarity();
		newMatch = new Match_Struc(similarity,targetSchema);
		
		String[] currMatch;
		
		// System.out.println("Parsing the match objects.");
		
		//loop through each of the individual matching elements w relations
		for (IMappingElement<INode> mappingElement : mapping) {
			//System.out.println("Looping through a single match ");
			String sourceConceptName = getNodePathString(mappingElement.getSource());
			String targetConceptName = getNodePathString(mappingElement.getTarget());
			String relation = Character.toString(mappingElement.getRelation());
			
			currMatch = new String[]{sourceConceptName,relation,targetConceptName};
			
			newMatch.addMatch(currMatch);
		}
		
		//then add this new match to our overall list of results, if there have been matches only
		if(newMatch.getNumMatchComponents() != 0){
			results.add(newMatch);
			// System.out.println("Adding a new match.");
			spsmSuccessCounter++ ;
		} else{
			// System.out.println("No match to add.");
			spsmNoMatchCounter++;
		}
		
		return results;
	}
	
	//used by recordSerialisedResults to return string
	//of the node
    public String getNodePathString(INode node) {
        StringBuilder sb = new StringBuilder();

        sb.insert(0, node.nodeData().getName());
        node = node.getParent();

        while (node != null) {
            sb.insert(0, node.nodeData().getName() + ",");
            node = node.getParent();
        }

        return sb.toString();
    }
    
    // Report SPSM performance
    public static void reportSPSM(PrintWriter fOut) {
    	if(!fOut.equals(null)) {
    		fOut.println("\n\nSPSM Calls: " + spsmCallCounter);
    		fOut.println("SPSM Crashes: " + spsmCrashCounter);
    		fOut.println("SPSM Successful (matching) calls: " + spsmSuccessCounter);
    		fOut.println("SPSM Non-matching calls: " + spsmNoMatchCounter);
    	}
    	reportSPSM() ;
    }
    
    // Report SPSM performance
    public static void reportSPSM() {
    	System.out.println("\n\nSPSM Calls: " + spsmCallCounter);
    	System.out.println("SPSM Crashes: " + spsmCrashCounter);
    	System.out.println("SPSM Successful (matching) calls: " + spsmSuccessCounter);
    	System.out.println("SPSM Non-matching calls: " + spsmNoMatchCounter);
    }
    

}