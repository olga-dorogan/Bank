#Bank app#

##Description##
Web application actions as a small bank.
It saves info about clients, client accounts and transactions between accounts.

##Setup##
###Prerequisites###
* JDK 1.8 
* Maven 3.
* (optionally) MySQL 5.5 or 5.6 to be able to use the an external database
* (optionally) Tomcat 7+ to be able to run the application on a standalone Tomcat server

###Download project###
```git clone https://github.com/olga-dorogan/Bank.git```

###Run the app (core module with in-memory database)###
```mvn -Pdev clean package tomcat7:run```<br>
After executing the command, app is available at [app](http://localhost:8080/core)


###Prepare MySql Database (optionally)###
1. Install the MySql Server version 5.5 or above.
2. Prepare development database with the sql-maven-plugin: <br>
* Change the username/password in the [pom.xml](sql/pom.xml) corresponding to your root user, configured at installation time: <br>
```xml
<configuration>
  <driver>com.mysql.jdbc.Driver</driver>
  <url>jdbc:mysql://localhost:3306</url>
  <username>root</username><!-- use your installation admin user-->
  <password>root</password><!-- user your installation admin user's password-->
</configuration>
```
* Prepare the "bank-clerk" user and the bank_db with the following maven command<br>
```mvn install -Pprepare-db```
* Import the data into the database<br>
```mvn install -Pimport-db```

###Run the app with MySql (optionally)###
```mvn -Pprod clean package tomcat7:run```
