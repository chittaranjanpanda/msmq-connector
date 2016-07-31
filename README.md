# msmq-connector
Operation on MSMQ using Apache Camel

Prerequisites
- Windows OS. You need to have MSMQ installed and running.for help follow       https://msdn.microsoft.com/en-us/library/aa967729(v=vs.110).aspx

- Install Microsoft Visual C++ 2013 Redistributable. for help follow https://www.microsoft.com/en-in/download/details.aspx?id=40784

- Install java7+ and maven    
    
- Change the host and queue name in msmq-connector.properties file present in /src/main/resources/ folder

- Move to the base directory where pom.xml is present and run following commands

To build this project use

    mvn install

To run this project with Maven use

    mvn camel:run
