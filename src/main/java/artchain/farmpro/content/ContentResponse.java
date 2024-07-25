package artchain.farmpro.content;

import artchain.farmpro.content.image.ContentImageResponses;

public record ContentResponse(
		Long id,

		ProjectInfoResponse projectInfo,

		ContentType contentType,
		ContentPurpose contentPurpose,
		String title,
		String mainText,
		String textStyle,
		ContentImageResponses images
) {
	public static ContentResponse of(Content content) {

		ContentImageResponses images = ContentImageResponses.from(content.getImages());
		return new ContentResponse(
				content.getId(),
				content.getContentType(),
				content.getContentPurpose(),
				content.getTitle(),
				content.getMainText(),
				content.getTextStyle(),
				images);
	}

	public static ContentResponse of(Content content, ContentImageResponses images) {
		return new ContentResponse(
				content.getId(),
				content.getContentType(),
				content.getContentPurpose(),
				content.getTitle(),
				content.getMainText(),
				content.getTextStyle(),
				images);
	}

	ContentResponse(Long id, ContentType contentType, ContentPurpose contentPurpose, String mainText, String title,
	                String textStyle, ContentImageResponses images) {
		this(id, null, contentType, contentPurpose, title, mainText, textStyle, images);
	}
}
