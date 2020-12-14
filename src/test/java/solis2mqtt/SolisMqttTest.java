package solis2mqtt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import solis2mqtt.exceptions.SolisMqttException;
import solis2mqtt.exceptions.SolisReaderException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
class SolisMqttTest {

    private SolisMqtt mqtt;

    @Mock
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mqtt = new SolisMqtt(configuration);
        when(configuration.getMqttAddress()).thenReturn("127.0.0.1");
        when(configuration.getMqttPort()).thenReturn(1883);
        when(configuration.getMqttTopic()).thenReturn("tele/solis/SENSOR");
        when(configuration.getMqttIdentifier()).thenReturn("solis2mqtt");
    }

    //@Test
    void send() throws SolisMqttException {
        SolisModel model = new SolisModel();
        model.setPower(Optional.of(200));
        model.setYieldToday(Optional.of(1.0));
        model.setYieldTotal(Optional.of(37.2));

        mqtt.send(model);
    }

    //@Test
    void sendShouldThrowIfTheAddressIsWrong() {
        SolisModel model = new SolisModel();
        when(configuration.getMqttAddress()).thenReturn("128.0.0.1");
        // Then an exception is thrown.
        SolisMqttException ex = assertThrows(SolisMqttException.class, () -> mqtt.send(model));
        assertEquals("Failed to send data to Mqtt", ex.getMessage());
    }
}