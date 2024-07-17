package artchain.farmpro.content;

import java.util.List;

public record ContentResponses(List<ContentResponse> contents) {

	public static ContentResponses from(List<Content> contents) {
		List<ContentResponse> responses = contents.stream()
				.map(ContentResponse::of)
				.toList();

		return new ContentResponses(responses);
	}
}
