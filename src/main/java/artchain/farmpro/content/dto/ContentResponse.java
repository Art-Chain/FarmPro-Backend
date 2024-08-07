package artchain.farmpro.content.dto;

import artchain.farmpro.ai.dto.ChatGptResponse;
import artchain.farmpro.content.Content;
import artchain.farmpro.content.ContentPurpose;
import artchain.farmpro.content.ContentType;
import artchain.farmpro.content.ParlanceStyle;
import artchain.farmpro.content.image.dto.ContentImageResponses;
import java.util.List;

public record ContentResponse(
		Long id,
		ProjectInfoResponse projectInfo,
		ContentType contentType,
		ContentPurpose contentPurpose,
		String title,
		String mainText,
		ParlanceStyle parlanceStyle,
		ContentImageResponses images,
		List<ChatGptResponse> chatGptResponses
) {
	public static ContentResponse of(Content content) {

		ContentImageResponses images = ContentImageResponses.from(content.getImages());
		return new ContentResponse(
				content.getId(),
				content.getContentType(),
				content.getContentPurpose(),
				content.getTitle(),
				content.getMainText(),
				content.getParlanceStyle(),
				images, null);
	}

	public static ContentResponse of(Content content, ContentImageResponses images) {
		return new ContentResponse(
				content.getId(),
				content.getContentType(),
				content.getContentPurpose(),
				content.getTitle(),
				content.getMainText(),
				content.getParlanceStyle(),
				images, null);
	}

	public static ContentResponse of(Content content, List<ChatGptResponse> chatGptResponses) {
		ContentImageResponses images = ContentImageResponses.from(content.getImages());
		return new ContentResponse(
				content.getId(),
				content.getContentType(),
				content.getContentPurpose(),
				content.getTitle(),
				content.getMainText(),
				content.getParlanceStyle(),
				images,
				chatGptResponses
		);
	}

	ContentResponse(Long id, ContentType contentType, ContentPurpose contentPurpose, String mainText, String title,
	                ParlanceStyle parlanceStyle, ContentImageResponses images, List<ChatGptResponse> chatGptResponses) {
		this(id, null, contentType, contentPurpose, title, mainText, parlanceStyle, images, chatGptResponses);
	}
}
