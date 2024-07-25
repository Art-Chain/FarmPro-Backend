package artchain.farmpro.content;

import artchain.farmpro.ai.ChatGptResponse;
import artchain.farmpro.content.image.ContentImageResponses;
import java.util.List;

public record ContentCreateResponse(
		Long id,
		ProjectInfoResponse projectInfo,
		ContentType contentType,
		ContentPurpose contentPurpose,
		String title,
		String generatedTitle,
		String mainText,
		String textStyle,
		ContentImageResponses userUploadImages,
		List<ChatGptResponse> chatGptResponses

) {
	public static ContentCreateResponse of(Content content, List<ChatGptResponse> chatGptResponses) {
		ContentImageResponses images = ContentImageResponses.from(content.getImages());
		return new ContentCreateResponse(
				content.getId(),
				null,
				content.getContentType(),
				content.getContentPurpose(),
				content.getTitle(),
				content.getGeneratedTitle(),
				content.getMainText(),
				content.getTextStyle(),
				images,
				chatGptResponses
		);
	}

	ContentCreateResponse(Long id, ContentType contentType, ContentPurpose contentPurpose, String mainText,
	                      String title, String generatedTitle,
	                      String textStyle, ContentImageResponses images, List<ChatGptResponse> chatGptResponses) {
		this(id, null, contentType, contentPurpose, title, generatedTitle, mainText, textStyle, images,
				chatGptResponses);
	}
}
