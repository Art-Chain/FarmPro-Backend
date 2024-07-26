package artchain.farmpro.content.dto;

import artchain.farmpro.ai.dto.ChatGptResponse;
import artchain.farmpro.card.CardStyle;
import artchain.farmpro.content.Content;
import artchain.farmpro.content.ContentPurpose;
import artchain.farmpro.content.ContentType;
import artchain.farmpro.content.ParlanceStyle;
import artchain.farmpro.content.image.dto.ContentImageResponses;
import java.util.List;

public record ContentCreateResponse(
		Long id,
		ProjectInfoResponse projectInfo,
		ContentType contentType,
		ContentPurpose contentPurpose,
		String title,
		String generatedTitle,
		String mainText,
		ParlanceStyle parlanceStyle,
		CardStyle cardStyle,
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
				images.images().get(0).title(),
				content.getGeneratedTitle(),
				content.getMainText(),
				content.getParlanceStyle(),
				content.getCardStyle(),
				images,
				chatGptResponses
		);
	}

	ContentCreateResponse(Long id, ContentType contentType, ContentPurpose contentPurpose, String mainText,
	                      String title, String generatedTitle,
	                      ParlanceStyle parlanceStyle, CardStyle cardStyle, ContentImageResponses images,
	                      List<ChatGptResponse> chatGptResponses) {
		this(id, null, contentType, contentPurpose, title, generatedTitle, mainText, parlanceStyle, cardStyle, images,
				chatGptResponses);
	}
}
