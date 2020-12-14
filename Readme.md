# Solis to MQTT

This program sends information from the Solis Power Inverter to MQTT.
It reaches out to the local wifi dongle and extracts information.
Then it sends the values to an MQTT topic

## Solis
It gets the status.html web page from the solis inverter.  
It will use the credentials provided in the application.properties

Then it extracts:
- Current Power in W
- Yield today in kWh
- Yield total in kWh

Settings in the application.properties
```
solis.inverterUrl=http://[IP-ADDRESS]/status.html
solis.inverterUserName=admin
solis.inverterPassword=admin
```

## Mqtt
It will send the data which it could extract to an MQTT topic  
Settings in the application.properties
```
solis.mqttAddress=[IP-ADDRESS]
solis.mqttPort=1883
solis.mqttTopic=tele/solis/SENSOR
solis.mqttIdentifier=solis2mqtt
```
 
It will send a json message like:
```json
{"power":123,"today":1.3,"total":33.4}
```

## Docker
Maven will build the docker container automatically.

For the manual steps:  
- Build: 
`docker build -t com.homeautomation/solis2mqtt:0.0.1-SNAPSHOT .`  
- Run: 
`docker run -v c:/temp:/logs -e "SOLIS_INVERTERURL=http://solisinverter/status.html" -e "SOLIS_MQTTADDRESS=127.0.0.1" com.homeautomation/solis2mqtt:0.0.1-SNAPSHOT`  
- Stop:
`docker stop com.homeautomation/solis2mqtt:0.0.1-SNAPSHOT`  
- Save:
`docker save -o c:\temp\solis2mqtt.tar com.homeautomation/solis2mqtt:0.0.1-SNAPSHOT`  
- Load:
`docker load -i solis2mqtt.tar`

## Docker compose example
```
version: '2'
services:
  solis2mqtt:
    container_name: Solis2mqtt
    image: com.homeautomation/solis2mqtt:0.0.1-SNAPSHOT
    user: "1000:1000"
    environment:
      - SOLIS_INVERTERURL=http://192.168.1.253/status.html
      - SOLIS_MQTTADDRESS=192.168.1.252
    volumes:
      - /home/nuc/solis2mqtt:/logs
    restart: always
```