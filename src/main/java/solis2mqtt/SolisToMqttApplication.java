package solis2mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import solis2mqtt.exceptions.SolisException;
import solis2mqtt.exceptions.SolisMqttException;
import solis2mqtt.exceptions.SolisParserException;
import solis2mqtt.exceptions.SolisReaderException;

import java.util.List;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class SolisToMqttApplication {

	private final SolisHtmlParser parser;
	private final SolisReader reader;
	private final SolisMqtt sender;

	public SolisToMqttApplication(SolisHtmlParser parser, SolisReader reader, SolisMqtt sender) {
		this.parser = parser;
		this.reader = reader;
		this.sender = sender;
	}

	public static void main(String[] args) {
		SpringApplication.run(SolisToMqttApplication.class, args);
	}

	//Run every 30 seconds
	@Scheduled(cron = "0/30 * * * * ?")
	public void TryExecute() {
		try {
			Execute();
		} catch(SolisException ex) {
			log.error("One step went wrong: {}", ex.getMessage());
		} catch(Exception ex) {
			log.error("Didn't expect this error: {}", ex.getMessage());
		}
	}

	public void Execute() throws SolisException {
		log.info("Execute start");
		List<String> html = reader.getStatus();
		SolisModel model = parser.parse(html);
		sender.send(model);
		log.info("Execute finish");
	}
}
