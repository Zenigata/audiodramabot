# audiodramabot

## Add airtable.java jar located in /lib
    mvn install:install-file -Dfile=lib/airtable.java-0.2.2.jar -DgroupId=sybit-education -DartifactId=airtable.java -Dversion=0.2.2 -Dpackaging=jar -DgeneratePom=true

## Create credentials.properties in src/main/resources folder
    AIRTABLE_API_KEY=
