package solis2mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;
import solis2mqtt.exceptions.SolisMqttException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolisMqtt {

    private final Configuration configuration;

    public void send(SolisModel model) throws SolisMqttException {
        log.debug("Send model broker using mqtt: '{}'", model.json());
        IMqttClient client = null;
        try {
            client = createClient();
            MqttMessage message = new MqttMessage(model.json().getBytes());
            log.info("Send '{}' to topic '{}'", model.json(), configuration.getMqttTopic());
            client.publish(configuration.getMqttTopic(), message);
        } catch(MqttException ex) {
            throw new SolisMqttException("Failed to send data to Mqtt", ex);
        }
        finally {
            if(client != null) {
                try {
                    client.close();
                } catch (MqttException ex) {
                    log.error("Failed to close the Mqtt connection: {}", ex.getMessage());
                }
            }
        }
    }

    private IMqttClient createClient() throws MqttException {
        String address = String.format("tcp://%s:%d", configuration.getMqttAddress(), configuration.getMqttPort());
        log.debug("Open connection to {}", address);
        IMqttClient publisher = new MqttClient(address, configuration.getMqttIdentifier());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(30);
        publisher.connect(options);
        return publisher;
    }
}
