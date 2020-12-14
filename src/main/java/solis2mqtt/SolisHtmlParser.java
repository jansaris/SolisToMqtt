package solis2mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solis2mqtt.exceptions.SolisParserException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SolisHtmlParser {

    private static final String POWER = "var webdata_now_p";
    private static final String YIELD_TODAY = "var webdata_today_e";
    private static final String YIELD_TOTAL = "var webdata_total_e";
    private static final Pattern PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)?");

    public SolisModel parse(List<String> html) throws SolisParserException {
        SolisModel model = new SolisModel();
        model.setPower(parseToInt(strip(find(html, POWER))));
        model.setYieldToday(parseToDouble(strip(find(html, YIELD_TODAY))));
        model.setYieldTotal(parseToDouble(strip(find(html, YIELD_TOTAL))));
        if(!model.getPower().isPresent() &&
           !model.getYieldToday().isPresent() &&
           !model.getYieldTotal().isPresent()) {
            throw new SolisParserException(String.format("Nothing found in %d html lines", html.size()));
        }
        log.info("Extracted '{}' from the html", model.toString());
        return model;
    }

    private String find(List<String> html, String value) {
        log.debug("Find '{}' in {} html lines",  value, html.size());
        return html.stream()
            .filter(s -> s.contains(value))
            .findFirst()
            .orElse("");
    }

    private String strip(String value) {
        if(value.equals("")) return value;
        log.debug("Strip the value from '{}'", value);
        Matcher match = PATTERN.matcher(value);
        if(match.find()) {
            return match.group();
        } else {
            log.warn("Failed to find a number in '{}'", value);
            return "";
        }
    }

    private Optional<Double> parseToDouble(String value) {
        if(value.equals("")) return Optional.empty();
        return Optional.of(Double.parseDouble(value));
    }

    private Optional<Integer> parseToInt(String value) {
        if(value.equals("")) return Optional.empty();
        return Optional.of(Integer.parseInt(value));
    }
}
