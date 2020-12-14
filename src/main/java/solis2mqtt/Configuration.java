package solis2mqtt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "solis")
@Component
@Getter
@Setter
public class Configuration {

    private String inverterUrl;
    private String inverterUserName;
    private String inverterPassword;

    private String mqttAddress;
    private int mqttPort;
    private String mqttTopic;
    private String mqttIdentifier;
}
