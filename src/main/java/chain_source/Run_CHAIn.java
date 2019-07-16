package chain_source;

import com.hp.hpl.jena.query.ResultSet;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 
 * Run the complete CHAIN workflow
 * 
 * Responsible for running the overall CHAIn functionality.
 * 
 * 
 * Tries the input SPARQL query; if the query fails, extract a schema from the
 * query; run SPSM on the que ry schema and target schemas; sort and filter the
 * results; create repaired schemas and SPARQL queries and run them.
 * 
 * This class is tested in Run_CHAIn_Test_Cases.java
 * 
 * @author Tanya Howden (September 2017)
 * @author Diana Bental (Modified December 2017)
 *
 */

public class Run_CHAIn {

	// Return Status
	private static final int UNKNOWNSTATUS = 0;
	private static final int INITIALQUERYSUCCESS = 5;
	private static final int INVALIDQUERY = 6;
	private static final int SPSMFAILURE = 7;
	private static final int NOMATCHESFROMSPSM = 8;
	private static final int REPAIREDQUERYRUNERROR = 9;
	private static final int REPAIREDQUERYRESULTS = 10;
	private static final int REPAIREDQUERYNORESULTS = 11;
	private static final int DATAREPAIREDWITHRESULTS = 12;

	private Schema_From_Query getSchema = new Schema_From_Query();
	private Call_SPSM spsm = new Call_SPSM();
	private Best_Match_Results filterRes = new Best_Match_Results();
	private Repair_Schema repairSchema = new Repair_Schema();
	private Create_Query createQuery = new Create_Query();
	private Run_Query runQuery = new Run_Query();

	///////////////////////// TOM
	private Match_Struc current = null;
	private ResultSet resultsFromARepairedQuery = null;
	private ResultSet resultsFromInitialQuery = null;
	private ArrayList<Match_Struc> repairedQueriesList = null;
	// List of the result(s) of the repaired query(ies)
	private ArrayList<ResultSet> listResultsFromRepairedQueries = null;
	private String initialQuerySchema = "";
	//////////////////////////

	// main method for testing during implementation
	public static void main(String[] args) {
		Run_CHAIn run_CHAIn = new Run_CHAIn();

		// need to pass in the query
		// and also target schemas for dataset
		// we are trying to query
		String query = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n" + "SELECT *  \n"
				+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressurestest.n3>\n"
				+ "WHERE { ?id sepaw:dataSource ?dataSource;\n" + "sepaw:identifiedDate  ?identifiedDate  ;\n"
				+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n" + "sepaw:waterBodyId ?waterBodyId .}" + "\n\n";
		String queryType = "sepa";
		double simThresholdVal = 0.3;
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";

		// note if there are several schemas here they are separated with ';'
		// -- Two target schemas
		// String targetSchemas="waterBodyPressures(dataSource, identifiedDate,
		// affectsGroundwater, waterBodyId) ; waterBodyMeasures(dataSource,
		// identifiedDate, affectsGroundwater, waterBodyId)";
		// -- No target schemas
		// String targetSchemas="";
		// -- One target schema
		// String targetSchemas="waterBodyPressures(dataSource, identifiedDate,
		// affectsGroundwater, waterBodyId)" ;
		// -- No narrowed schemas
		// String targetSchemas="places(dataSource, identifiedDate, affectsGroundwater,
		// waterBodyId)" ;
		// -- Two targets, one narrowed target schema

		//
		String targetSchemas = "places(dataSource, identifiedDate, affectsGroundwater, waterBodyId) ; waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)";

		run_CHAIn.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, simThresholdVal, 5, null);
	}

	/**
	 * Responsible for running the overall CHAIn functionality
	 * 
	 * Returns a status code
	 * 
	 * @param query
	 * @param queryType
	 *            (either sepa or dbpedia)
	 * @param targetSchemas
	 *            (each schema being separated by a ‘;’)
	 * @param dataDir
	 *            (the dataset directory based on the query type)
	 * @param ontologyPath
	 *            (the ontology path based on the query type)
	 * @param queryLim
	 *            (the maximum number of returned results wanted)
	 * @param simThresholdVal
	 *            (the similarity threshold value)
	 * @param resLimit
	 *            (the maximum number of queries we want produced)
	 * @param fOut
	 *            (a PrintWriter that you want the results to be printed out to OR
	 *            null)
	 * 
	 * @return result_status
	 */
	public int runCHAIn(String query, String queryType, String targetSchemas, String dataDir, String ontologyPath,
			int queryLim, double simThresholdVal, int resLimit, PrintWriter fOut) {

		int result_status = UNKNOWNSTATUS;
		current = new Match_Struc();

		// first step is trying to run the initial query

		query += "\nLIMIT " + queryLim; // TOM: otherwise the user have to enter manually the limit when framing the
										// query
		current.setQuery(query);

		resultsFromInitialQuery = runQuery.runQuery(current, queryType, dataDir);

		if ((resultsFromInitialQuery != null) && (resultsFromInitialQuery.hasNext())) {
			// query has run successfully!
			// no need to repair
			System.out.println("Query has run successfully first time. CHAIn has finished running.");

			if (fOut != null) {
				fOut.write("Query has run successfully first time.\n\n CHAIn has finished running.\n\n\n");
			}
			return INITIALQUERYSUCCESS;

		} else {
			// has not run successfully, need to call SPSM & start repair work
			System.out.println("Query has not run successfully.");
			System.out.println("CHAIn will now try to repair this query.\n");

			if (fOut != null) {
				fOut.write(
						"Query has NOT run successfully the first time.\n\nCalling SPSM to try and create new query\n");
			}

			current = getSchema.getSchemaFromQuery(query, queryType);

			if (current == null) {
				// then we have an invalid query
				// terminate chain with appropriate message
				if (fOut != null) {
					fOut.write("Invalid SPARQL query, please enter a valid query, terminating...\n\n");
				}
				System.out.println("Invalid SPARQL query, Terminating.");

				return INVALIDQUERY;
			}

			// Parse the query and store useful information about names, prefixes, literals
			Query_Data queryData = new Query_Data(query);

			// ArrayList<Match_Struc> repairedQueries = createRepairedQueries(current,
			// queryData, targetSchemas, queryType,
			// dataDir, ontologyPath, queryLim, simThresholdVal, resLimit);

			// TOM
			// Because the schema and schema head dissapeard during the CHAIn process....
			initialQuerySchema = current.getQuerySchema();

			this.repairedQueriesList = createRepairedQueries(current, queryData, targetSchemas, queryType, dataDir,
					ontologyPath, queryLim, simThresholdVal, resLimit);

			if (repairedQueriesList == null) {
				System.out.println("\nSPSM Failure. Terminating.");
				if (fOut != null) {
					fOut.write("\nSPSM Failure. Terminating.\n\n");
				}
				return SPSMFAILURE;

			} else if (repairedQueriesList.size() == 0) {
				// no results returned from spsm
				System.out.println("\nNo results from SPSM. Terminating.");
				if (fOut != null) {
					fOut.write("There have been 0 matches returned from SPSM, terminating...\n\n");
				}
				return NOMATCHESFROMSPSM;
			} else {
				System.out.println("Now running the new queries that have been created...");
				if (fOut != null) {
					fOut.write("Now running the new queries that have been created...\n\n");
				}

				// ResultSet resultsFromARepairedQuery;

				// Print all the match structures with their repaired queries
				// Tom: Good example
				System.out.println("\nStart of the display for the repaired queries !");
				for (Match_Struc r : repairedQueriesList) {
					System.out.println("Match_Struc:" + r);
				}
				System.out.println("End of the display for the repaired queries !\n ");
				// Initialize our list to store the results in it
				listResultsFromRepairedQueries = new ArrayList<ResultSet>();
				for (int i = 0; i < repairedQueriesList.size(); i++) {
					// try running new queries
					Match_Struc curr = repairedQueriesList.get(i);
					if (fOut != null) {
						fOut.write("Target Schema, " + curr.getDatasetSchema()
								+ ", has created the following query:\n\n" + curr.getQuery() + "\n\n");
					}

					resultsFromARepairedQuery = runRepairedQueries(curr, queryType, dataDir);
					if (resultsFromARepairedQuery == null) {
						System.out.println("This new query has NOT run successfully.");
						// fOut.write("This new query has NOT run successfully.\n\n\n");
						return REPAIREDQUERYRUNERROR;
					} else if (!resultsFromARepairedQuery.hasNext()) {
						System.out.println("This new query has run with no results.");
						if (fOut != null) {
							fOut.write("This new query has run with no results.\n\n\n");
						}

						resultsFromARepairedQuery = dataRepair(queryType, curr, queryData, dataDir, queryLim,
								ontologyPath, fOut);

						if (!resultsFromARepairedQuery.hasNext() && result_status != REPAIREDQUERYRESULTS) {
							result_status = REPAIREDQUERYNORESULTS;

						} else if (result_status != REPAIREDQUERYRESULTS) {
							result_status = DATAREPAIREDWITHRESULTS;
						}

					} else {
						System.out.println("This new query has run successfully with results.");

						/////////////
						System.out.println("\n\n\n QUERY DATA:\n " + queryData + "\n\n");
						/////////////

						if (fOut != null) {
							fOut.write("This new query has run successfully.");
						}
						result_status = REPAIREDQUERYRESULTS;
					}
					// Add to our list the result
					listResultsFromRepairedQueries.add(resultsFromARepairedQuery);
				}
			}
		}
		return result_status;
	}

	/**
	 * Attempt to repair the query data by making the new (open) query and then try
	 * to run it
	 * 
	 * @param queryType
	 * @param curr
	 * @param queryData
	 * @param dataDir
	 * @param queryLim
	 * @param ontologyPath
	 * @param fOut
	 * 
	 * @return result
	 */
	public ResultSet dataRepair(String queryType, Match_Struc curr, Query_Data queryData, String dataDir, int queryLim,
                                String ontologyPath, PrintWriter fOut) {

		System.out.println("Attempting data repair.");
		if (fOut != null) {
			fOut.write("Attempting data repair.\n\n\n");
		}

		ArrayList<Ontology_Struc> ontologies = createQuery.make_ontologies(ontologyPath);
		// Make the new (open) query
		String newQuery = createQuery.createOpenQuery(queryType, curr, queryData, dataDir, queryLim, ontologies);
		// System.out.println("Run_CHAIn: "+ newQuery);
		curr.setQuery(newQuery);
		// Then run it
		return runQuery.runQuery(curr, queryType, dataDir);
	}

	/**
	 * Narrow down the target schema(s) then call SPSM and store its result in a
	 * array list of Match_Struct
	 * 
	 * @param current
	 * @param queryData
	 * @param targetSchemas
	 * @param queryType
	 * @param dataset
	 * @param ontFile
	 * @param queryLim
	 * @param simThresholdVal
	 * @param resLimit
	 * 
	 * @return matches
	 */
	public ArrayList<Match_Struc> createRepairedQueries(Match_Struc current, Query_Data queryData, String targetSchemas,
                                                        String queryType, String dataset, String ontFile, int queryLim, double simThresholdVal, int resLimit) {

		// Narrow down the target schemas by filtering them against the associated words
		// in the source schema
		System.out.println("All Target Schemas: " + targetSchemas);

		targetSchemas = Narrow_Down.narrowDown(current.getQuerySchemaHead(), targetSchemas);

		System.out.println("Narrowed Target Schemas: " + targetSchemas);

		// start off by calling SPSM with schema created from query
		// and target schemas passed in originally
		ArrayList<Match_Struc> matches = new ArrayList<Match_Struc>();

		matches = spsm.callSPSM(matches, current.getQuerySchema(), targetSchemas);

		if (matches == null) {
			System.out.println("Null Results due to SPSM Error.");
			return null;
		} else if (matches.size() == 0) {
			// there are no results from spsm
			System.out.println("There are no matches returned from SPSM.");
		} else {
			// spsm has returned something
			// filter results
			matches = filterRes.getThresholdAndFilter(matches, simThresholdVal, resLimit);

			// return repaired schema
			matches = repairSchema.repairSchemas(matches);

			// then create a query from the repaired schema
			matches = createQuery.createQueries(matches, queryData, queryType, dataset, ontFile, queryLim);
		}

		return matches;
	}

	/**
	 * Run the repaired query
	 * 
	 * @param matchStructure
	 * @param queryType
	 * @param dataset
	 * 
	 * @return result
	 */
	public ResultSet runRepairedQueries(Match_Struc matchStructure, String queryType, String dataset) {

		try {
			// running new queries
			System.out.println("\nRunning New Query\n");
			ResultSet result = runQuery.runQuery(matchStructure, queryType, dataset);
			return result;
		} catch (Exception e) {
			System.out.println("Error running query");
			return null;
		}

	}

	///////////////////

	public ArrayList<Match_Struc> getRepairedQueriesList() {
		return repairedQueriesList;
	}

	public void setRepairedQueriesList(ArrayList<Match_Struc> repairedQueries) {
		this.repairedQueriesList = repairedQueries;
	}

	public ResultSet getResultsFromInitialQuery() {
		return resultsFromInitialQuery;
	}

	public void setResultsFromInitialQuery(ResultSet resultsFromInitialQuery) {
		this.resultsFromInitialQuery = resultsFromInitialQuery;
	}

	public ResultSet getResultsFromARepairedQuery() {
		return resultsFromARepairedQuery;
	}

	public void setResultsFromARepairedQuery(ResultSet resultsFromARepairedQuery) {
		this.resultsFromARepairedQuery = resultsFromARepairedQuery;
	}

	public ArrayList<ResultSet> getListResultsFromRepairedQuery() {
		return listResultsFromRepairedQueries;
	}

	public void setListResultsFromRepairedQuery(ArrayList<ResultSet> listResultsFromRepairedQueries) {
		this.listResultsFromRepairedQueries = listResultsFromRepairedQueries;
	}

	// TEST
	public String getInitialQuerySchema() {
		return initialQuerySchema;
	}

	public void setInitialQuerySchema(String initialQuerySchema) {
		this.initialQuerySchema = initialQuerySchema;
	}

}
