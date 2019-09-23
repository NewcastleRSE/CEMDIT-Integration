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
