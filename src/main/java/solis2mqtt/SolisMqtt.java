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
    private IMqttClient client;

    public void send(SolisModel model) throws SolisMqttException {
        log.debug("Send model broker using mqtt: '{}'", model.json());
        try {
            openClient();
            MqttMessage message = new MqttMessage(model.json().getBytes());
            log.info("Send '{}' to topic '{}'", model.json(), configuration.getMqttTopic());
            client.publish(configuration.getMqttTopic(), message);
        } catch(MqttException ex) {
            closeClient();
            throw new SolisMqttException("Failed to send data to Mqtt", ex);
        }
    }

    private void openClient() throws MqttException {
        if(client != null && client.isConnected()) return;
        if(client != null) closeClient();
        String address = String.format("tcp://%s:%d", configuration.getMqttAddress(), configuration.getMqttPort());
        log.debug("Open connection to {}", address);
        client = new MqttClient(address, configuration.getMqttIdentifier());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(30);
        client.connect(options);
    }

    private void closeClient() {
        if(client == null) return;
        try {
            client.disconnectForcibly();
            client.close();
        } catch(Exception ex) {
            log.error("Failed to close the Mqtt connection: {}", ex.getMessage());
        } finally {
            client = null;
        }
    }
}
