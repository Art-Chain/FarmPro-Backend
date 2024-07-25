package artchain.farmpro.ai;

import java.util.List;

public record ChatGptResponse(List<UrlResponse> data) {

	public record UrlResponse(String url, String revisedPrompt) {
	}

	public static List<String> urlsFrom(List<ChatGptResponse> responses) {
		return responses.stream()
				.flatMap(response -> response.data().stream())
				.map(UrlResponse::url)
				.toList();
	}
}
