# Lab 6

The same console app but now divided into client and server... with UDP...

## Requirements

- Java 17 or newer
- Maven 3.9 or newer

## Environment Variable

Before running the application, set the `path_to_xml` environment variable to the path of the XML data file.

### Linux Bash / Fish
```bash
export path_to_xml=./data.xml
```
### Windows CMD
```bash
set path_to_xml=.\data.xml
```
### Windows PowerShell
```bash
$env:path_to_xml=".\data.xml"
```


## Compile
```Bash
mvn package
```

## Install
```Bash
mvn install
```

## Run
```Bash
java -cp target/Lab6-1.0-SNAPSHOT.jar lab6.server.app.ServerMain
mvn exec:java -Dexec.mainClass="lab6.server.app.ServerMain"
java -cp target/Lab6-1.0-SNAPSHOT.jar lab6.client.app.ClientMain
mvn exec:java -Dexec.mainClass="lab6.client.app.ClientMain"
```

## Clean
```bash
mvn clean
```

## Generate JavaDoc
```bash
mvn javadoc:javadoc
The documentation will be available here: target/reports/apidocs/index.html
```


