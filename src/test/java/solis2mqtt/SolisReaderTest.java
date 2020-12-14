package solis2mqtt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import solis2mqtt.exceptions.SolisReaderException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@Ignore
class SolisReaderTest {

    private SolisReader reader;

    @Mock
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reader = new SolisReader(configuration);
        when(configuration.getInverterUrl()).thenReturn("http://solis-inverter/status.html");
    }

    //@Test
    void getStatus() throws SolisReaderException {
        when(configuration.getInverterUserName()).thenReturn("admin");
        when(configuration.getInverterPassword()).thenReturn("admin");

        List<String> status = reader.getStatus();

        assertThat(status.size() > 0).isTrue();
    }

    //@Test
    void getStatusShouldThrow401() {
        // When no username and password is set
        // Then an exception is thrown.
        SolisReaderException ex = assertThrows(SolisReaderException.class, () -> reader.getStatus());
        assertEquals("Got invalid response from Solis (401)", ex.getMessage());
    }

    //@Test
    void getShouldThrowTimeoutIfTheInverterIsOff() {
        when(configuration.getInverterUrl()).thenReturn("http://solis-inverter/status.html");

        // Unknown address expect SocketTimeoutException
        SolisReaderException ex = assertThrows(SolisReaderException.class, () -> reader.getStatus());
        assertEquals("Failed to open the connection: connect timed out", ex.getMessage());
    }
}