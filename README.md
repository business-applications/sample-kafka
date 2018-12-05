# Business Applications by jBPM - Kafka Current Weather app

This is a demo business application built with https://start.jbpm.org and expanded
to create kafka and websocket weather station app. 

![Sample of demo](img/kafka-demo-main.png?raw=true)


This demo application is driven by the newly added jBPM Kafka Workitem which is able to push messages
onto a running Kafka server topic. Once user enters in the city and country names, the apps
business process retrieves the current weather information (using the jBPM OpenWeatherMap workitem)
and then passes it to the Kafka workitem which produces the message onto a configurable topic. 
The application has a Kafka consumer implementation which listens to messages on the same topic,
receives the weather info and posts it to a websocket destination. This is then 
picked up by our UI (using JavaScript) and the weather information is dynamically (without page refresh)
updated on the page. 

## Getting Started 
1. Clone and build the needed kie server thymeleaf dialect project:
```
git clone https://github.com/tsurdilo/thymeleaf-kie-server-dialect.git
cd thymeleaf-kie-server-dialect
mvn clean install
```
2. Clone this repository locally:

```
git clone https://github.com/business-applications/sample-websocket-terminal.git
cd sample-websocket-terminal
cd sample-websocket-terminal-service
chmod 755 launch.sh (only needed for unix environments , use launch.bat for windows)
```

3. Download and set up Apache Kafka:
For quick and easy setup follow instructions on https://kafka.apache.org/quickstart.
a) Start the ZooKeeper server:
```
bin/zookeeper-server-start.sh config/zookeeper.properties
```
b) Start the Kafka server:
```
bin/kafka-server-start.sh config/server.properties
```

c) Create the Kafka topic called "livewxdata"
```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic livewxdata

```

The name of the topic is configurable in your applications.properties file. If you change 
its name there you also need to create it with the command above


4. Setup an account for OpenWeatherMap (https://openweathermap.org/price) to get an access key for the free
offer they have (Click on the "Get Api key an Start" button). 
Once you have you api key update the OpenWeatherMap Workitem Handler config in your apps deployment descriptor.
In in sample-kafka-kjar/src/main/resources/META-INF/kie-deployment-descriptor.xml 
replace the YOUR_API_KEY_HERE:
```xml
<work-item-handler>
      <resolver>mvel</resolver>
      <identifier>new org.jbpm.process.workitem.owm.CurrentWeatherWorkitemHandler("YOUR_API_KEY_HERE")</identifier>
      <parameters/>
      <name>CurrentWeather</name>
    </work-item-handler>
```
with the real api key you get after registering on the OpenWeatherMap.

4. Start your Business Application:
In your business app service module run:
```
./launch.sh clean install(or launch.bat clean install for windows)
```

5. Access your business application by going to localhost:8090

Your demo app is a consumer of Kafka messages posted on the livewxdata topic. 
You can also add more consumers, for example commandline in your kafka download directory run:
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic livewxdata --from-beginning
```
to also consume all messages that the Kafka workitem is posting during business process execution.

You could even create your own producer, for example with:

```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic livewxdata
```

and try producing some of your own weather information. The info has format
```
CURRENT_TEMP,MAX_TEMP,MIN_TEMP
```
so for example if your produce 
```
10,15,7
```
and press enter you will see your apps chart update to refelect those numbers :)
