# Distributed_Hospital_design

</h1>About Project</h1>
This project contains 3 modules that simulate an hospital design. The three components contains </br>
<b>1) Policy Server:</b> The policy server contains the policy based on which the Hazelcast distributed cache is updated. It exposes two RESTful API's to update and get the policy from the policy server.</br>
<b>2) The Main Server:</b> The main server is integrated with <i>Cassandra Database</i> which stores the patient table (in hospital keyspace). It also contains a Hazelcast cache which stores the patient records based on the status(policy) obtained from the policy server. Then finally this server exposes the API's to delete, create, get and delete records.</br>
<b>3) The Activemq Consumer:</b> This server consumes the postprocessing messages written on the queues of Activemq by the main server. Based on the queue it either writes everything in the log or send an email to the patient about its status.</br> 

This repository contains all the files required to change the HappyPatients hospital from a monolith to an easy to understand and operate microservices architecture. <br>

<h1>Requirements </h1>
1 Java 1.8</br>
2 Cassandra 2.x </br>
3 ActiveMq </br>
4 HazelCast</br></br>


The different components used in this project include <br>

1. RESTful web service <br>
2. Cassandra database <br>
3. Hazelcast cache <br>
4. Messaging with Active MQ <br>
5. Apache Camel <br>
6. Java SPRING <br>
7. Spring Datastax cassandra for ORM model</br>
8. Tomcat Server</br>

The redesign of the HappyPatients hospital has the following architecture: <br>

![alt text](Architecture/architecture.PNG)


Some things to note before running the application is that the cassandra version used for this project is 2.2 and using the 3.x release might lead to unexpected behavior.<br> 


The main folder containing all the java packages to run a RESTful webservice connected to the cassandra database can be found inside DC Project<br>
All dependencies needed to run the RESTful webservice are speecified inside POM.xml of DC Project folder.<br>
The RESTful webservice is built using SPRING and the database is connected using Spring-data-cassandra. 

To execute this part of the project run perform <i>maven clean install</i> and copy the war file in the webapps folder of tomcat server. Make RESTful API calls using postman<br>

![alt text](Architecture/DC%20structure.PNG)

To execute the policy server run the file PolicyController.java inside PolicyServer-->src-->main-->java-->com-->java-->controller <br>


Both files from DC Project and Policy server must be run concurrently.<br> 
In order to execute the applications on the server , perform a ,maven build over the project. <br>
WAR files are not included in the repository because of their large size(>50MB), hence it is imperative to build project using maven. <br> 

Below is the schema used for cassandra database <br> 
keyspace name = hospital<br>
table name = patient<br>
![alt text](Architecture/Schema.png)

The folder :ActiveMQConsumer contains all java files to perform 2 actions : Send Email and Analytics. 
Every time an action is performed on the database a message is sent to the 2 queues and corresponding actions are performed. 
The main class for this is called Application.java. 


Feel free to contact me for any further clarifications.












