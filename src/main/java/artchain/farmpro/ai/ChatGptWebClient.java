package artchain.farmpro.ai;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
@Component
public class ChatGptWebClient {

    private ChatGptConfiguration configuration;

    public ChatGptResponse requestWithPrompt(String prompt) {
        HashMap<String, Object> body = setupBody(prompt);

        return WebClient.create()
                .post()
                .uri(configuration.getEndPoint())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + configuration.getOpenaiKey())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ChatGptResponse.class)
                .block();
    }

    private HashMap<String, Object> setupBody(String prompt) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("model", configuration.getModel());
        body.put("n", Integer.parseInt(configuration.getN()));
        body.put("size", configuration.getSize());
        body.put("prompt", prompt);
        return body;
    }
}
