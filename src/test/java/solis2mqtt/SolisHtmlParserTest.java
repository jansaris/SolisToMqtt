package solis2mqtt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import solis2mqtt.exceptions.SolisParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolisHtmlParserTest {

    private SolisHtmlParser parser;
    private List<String> statusHtml;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        parser = new SolisHtmlParser();
        Path file = Paths.get("src/test/resources/status.html");
        statusHtml = Files.readAllLines(file);
    }

    @Test
    void parse() throws SolisParserException {
        SolisModel model = parser.parse(statusHtml);

        assertTrue(model.getPower().isPresent());
        assertTrue(model.getYieldToday().isPresent());
        assertTrue(model.getYieldTotal().isPresent());

        assertEquals(200, model.getPower().get());
        assertEquals(1.8, model.getYieldToday().get());
        assertEquals(37.0, model.getYieldTotal().get());
    }

    @Test
    void parseShouldReturnIfAtLeastOneValueIsFound() throws SolisParserException {
        SolisModel model = parser.parse(new ArrayList<String>() { {
            add("Some html");
            add("var webdata_today_e = \"2.3\"");
            add("Other html");
        }});

        assertFalse(model.getPower().isPresent());
        assertTrue(model.getYieldToday().isPresent());
        assertFalse(model.getYieldTotal().isPresent());

        assertEquals(2.3, model.getYieldToday().get());
    }

    @Test
    void parseShouldNotFillYieldTodayIfItFailedToParseTheDoubleValue() throws SolisParserException {
        SolisModel model = parser.parse(new ArrayList<String>() { {
            add("Some html");
            add("var webdata_today_e = \"a.b\"");
            add("var webdata_total_e = \"37.0\";");
        }});

        assertFalse(model.getYieldToday().isPresent());
    }

    @Test
    void parseShouldThrowIfNoValueCouldBeFound() {
        SolisParserException ex = assertThrows(SolisParserException.class, () -> parser.parse(new ArrayList<>()));

        assertEquals("Nothing found in 0 html lines", ex.getMessage());
    }
}