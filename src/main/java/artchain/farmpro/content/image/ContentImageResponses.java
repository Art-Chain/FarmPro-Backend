package artchain.farmpro.content.image;

import java.util.List;

public record ContentImageResponses(List<ContentImageResponse> images) {
	public static ContentImageResponses from(List<ContentImage> images) {
		List<ContentImageResponse> responses = images.stream()
				.map(ContentImageResponse::of)
				.toList();

		return new ContentImageResponses(responses);
	}
}

