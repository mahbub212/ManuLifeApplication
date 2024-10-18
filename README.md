# ManuLifeApplication
Testing Manu Life Application 

Explain the code
This code explain using CRUD with java spring boot as backend and vaadin as frontend and report using jasper report

Depedencies
For more details on what dependencies are used, you can see the pom.xml file, but what you need attention to is the dependencies for Java Version, 
Vaadin and Jasper Report.


Information
1. Click button Report not working
2. Open browser http://localhot:8080/user-report

Instruction for Deployment
1.  mvn clean package -DskipTests
2.  cd \target
3.  copy ManuLifeApplication-0.0.1-SNAPSHOT.jar to server (OS Linux) folder for example:  /opt/services/ManuLifeApplication-0.0.1-SNAPSHOT.jar 
4.  running application on background proccess : nohup java -jar /opt/services/ManuLifeApplication-0.0.1-SNAPSHOT.jar > /opt/log/ManuLifeApplication-0.0.1-SNAPSHOT.jar.log 2>&1 &
5.  Open browser http://server_ip:8080
