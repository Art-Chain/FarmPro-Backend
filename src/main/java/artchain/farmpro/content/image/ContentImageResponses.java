package artchain.farmpro.content.image;

import artchain.farmpro.ai.ChatGptResponse;
import artchain.farmpro.ai.ChatGptResponse.UrlResponse;
import java.util.List;

public record ContentImageResponses(List<ContentImageResponse> images) {
	public static ContentImageResponses from(List<ContentImage> images) {
		List<ContentImageResponse> responses = images.stream()
				.map(ContentImageResponse::of)
				.toList();

		return new ContentImageResponses(responses);
	}

	public static ContentImageResponse of(ChatGptResponse imageResponse) {
		List<String> urls = imageResponse.data().stream().map((UrlResponse::url)).toList();
		return new ContentImageResponse(0L, urls.get(0));
	}

	public static ContentImageResponses fromGptResponses(List<ChatGptResponse> imageResponses) {
		List<ContentImageResponse> responses = imageResponses.stream()
				.map(ContentImageResponses::of)
				.toList();
		return new ContentImageResponses(responses);
	}
}

