package artchain.farmpro.ai;

import artchain.farmpro.ai.dto.ChatGptResponse;
import artchain.farmpro.ai.dto.GPTRecommendResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class ChatGptWebClient {

	private ChatGptConfiguration configuration;

	public Mono<ChatGptResponse> requestImageGenerate(String prompt) {
		HashMap<String, Object> body = setupImageBodyOutput(prompt);

		return WebClient.create()
				.post()
				.uri(configuration.getImageModelEndpoint())
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + configuration.getOpenaiKey())
				.bodyValue(body)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError,
						clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
				.bodyToMono(ChatGptResponse.class);
	}

	public GPTRecommendResponse requestTextGenerate(String prompt) {
		HashMap<String, Object> body = setupTextBodyOutput(prompt);

		return WebClient.create()
				.post()
				.uri(configuration.getTextModelEndpoint())
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + configuration.getOpenaiKey())
				.bodyValue(body)
				.retrieve()
				.bodyToMono(GPTRecommendResponse.class)
				.block();
	}

	public GPTRecommendResponse requestKeywordGenerate(String prompt) {
		HashMap<String, Object> body = setupTextBodyOutput(prompt);

		return WebClient.create()
				.post()
				.uri(configuration.getTextModelEndpoint())
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + configuration.getOpenaiKey())
				.bodyValue(body)
				.retrieve()
				.bodyToMono(GPTRecommendResponse.class)
				.block();
	}

	private HashMap<String, Object> setupImageBodyOutput(String prompt) {
		HashMap<String, Object> body = new HashMap<>();
		body.put("model", configuration.getImageModel());
		body.put("n", Integer.parseInt(configuration.getN()));
		body.put("size", configuration.getSize());
		body.put("prompt", prompt);
		return body;
	}

	private HashMap<String, Object> setupTextBodyOutput(String prompt) {
		HashMap<String, Object> body = new HashMap<>();
		body.put("model", configuration.getTextModel());
		List<HashMap<String, String>> messages = new ArrayList<>();
		HashMap<String, String> userMessage = new HashMap<>();
		userMessage.put("role", "user");
		userMessage.put("content", prompt);
		messages.add(userMessage);
		body.put("messages", messages);

		return body;
	}
}
