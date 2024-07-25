package artchain.farmpro.content;

import artchain.farmpro.content.image.ContentImageResponses;

public record ContentResponse(
		Long id,

		ProjectInfoResponse projectInfo,

		ContentType contentType,
		ContentPurpose contentPurpose,
		String mainText,
		String textStyle,
		ContentImageResponses images
) {
	public static ContentResponse of(Content content) {

		ContentImageResponses images = ContentImageResponses.from(content.getImages());
		return new ContentResponse(content.getId(), content.getContentType(), content.getContentPurpose(),
				content.getMainText(), content.getTextStyle(), images);
	}

	ContentResponse(Long id, ContentType contentType, ContentPurpose contentPurpose, String mainText,
	                String textStyle, ContentImageResponses images) {
		this(id, null, contentType, contentPurpose, mainText, textStyle, images);
	}
}
