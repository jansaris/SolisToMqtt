package solis2mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solis2mqtt.exceptions.SolisReaderException;

import javax.xml.ws.http.HTTPException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolisReader {

    private final Configuration configuration;

    public List<String> getStatus() throws SolisReaderException {
        log.info("Try to retrieve HTML from '{}'", configuration.getInverterUrl());
        try {
            List<String> html = ExecuteRequest();
            log.debug("HTML: {}", String.join(System.lineSeparator(), html));
            return html;
        } catch(HTTPException ex) {
            String error = String.format("Got invalid response from Solis (%d)", ex.getStatusCode());
            throw new SolisReaderException(error, ex);
        } catch(IOException ex) {
            String error = String.format("Failed to open the connection: %s", ex.getMessage());
            throw new SolisReaderException(error, ex);
        }
    }

    private List<String> ExecuteRequest() throws IOException {
        URL url = new URL(configuration.getInverterUrl());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(100);
        addAuthorization(con);
        log.debug("Open the connection");
        int responseCode = con.getResponseCode();
        log.info("Received {} from {}", responseCode, configuration.getInverterUrl());
        if (responseCode != 200) {
            throw new HTTPException(responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        ArrayList<String> content = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            content.add(inputLine);
        }
        in.close();
        return content;
    }

    private void addAuthorization(HttpURLConnection connection) {
        String user = configuration.getInverterUserName();
        String password = configuration.getInverterPassword();
        if(user == null || user.length() == 0) return;
        log.debug("Add user '{}' and it's password to the connection", user);
        String auth = user + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);
        connection.setRequestProperty("Authorization", authHeaderValue);
    }
}
