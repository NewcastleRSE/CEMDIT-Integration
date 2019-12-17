# CEMDIT-Integration

* In IntelliJ, add the project as a maven project
* Set an SDK. If you don't, you will be prompted the first time you try to build the project.
* Add SPSM as a library from the directory spsm/spsm-match/lib

# GUI structure
* The GUI has the following panels:

1. Query: A query has to be typed into the text area. The last query entered will be 
saved to the properties file and reloaded the next time the program runs. A template 
has to be selected and the type of query that needs to be executed, i.e. REST, RDF, SQL 
and CHAIn (for the moment). When the "Run" button is pressed the query is executed and
the results displayed in the "Query Results" tab.
1. Query Results: The result of an executed query is displayed on this tab in table
format. The "Bind Data to Template" above the table will bind all the rows in the 
returned data set to the selected template. The following files are created:
    2. A .json file for each row
    2. A .provn file for each row (data bound to provn template)
    2. An index file of all the .provn files
    2. A merged provn file
    2. A flattened merged provn file
1. Matches: The matches tab display the matches of the returned data and the operator 
(This is CHAIn stuff that needs to be explained in detail by Heriott-Watt)

# Provenance Explorer

# URLs
## MongoDB Atlas
https://cloud.mongodb.com

## ProvStore
https://openprovenance.org/store

ISCRAM Journal

Tasks before Dec:
* Get queries with interesting provenance
* Full integration of CHAIn with GUI
* Show provenance template

# MongoDB Database for lookup
* Set up MongoDB Docker container
* Create database CEMDIT (use CEMDIT)
* Insert into COLLECTION:
> db.COLLECTION.insert({query:'Vehicle Count',type:'ProvTemplate',uri:'https://openprovenance.org/store/documents/498.json'})

# Add SPSM 2.0.0 to local maven repository

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-core-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-core -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-io-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-io -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-logic-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-logic -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-nlp-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-nlp -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-nlp-annotation-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-nlp-annotation -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-nlp-opennlp-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-nlp-opennlp -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-spsm-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-spsm -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-spsm-prolog-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-spsm-prolog -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-utils-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-utils -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=spsm/s-match/lib/s-match-wordnet-2.0.0-SNAPSHOT.jar -Dit.unitn.disi -DartifactId=smatch-wordnet -Dversion=2.0.0 -Dpackaging=jar -DgeneratePom=true
