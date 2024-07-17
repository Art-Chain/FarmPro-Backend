package artchain.farmpro.content;

public record ContentResponse(Long id, ContentType contentType, ContentPurpose contentPurpose,
                              String mainText, String textStyle) {

	public static ContentResponse of(Content content) {
		return new ContentResponse(content.getId(), content.getContentType(), content.getContentPurpose(),
				content.getMainText(), content.getTextStyle());
	}
}
