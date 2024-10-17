# ManuLifeApplication
Testing Manu Life Application 

Instruction for Deployment
1.  mvn clean package -DskipTests
2.  cd \target
3.  copy ManuLifeApplication-0.0.1-SNAPSHOT.jar to server (OS Linux) folder for example:  /opt/services/ManuLifeApplication-0.0.1-SNAPSHOT.jar 
4.  running application on background proccess : nohup java -jar /opt/services/ManuLifeApplication-0.0.1-SNAPSHOT.jar > /opt/log/ManuLifeApplication-0.0.1-SNAPSHOT.jar.log 2>&1 &
5.  Open browser http://server_ip:8080
