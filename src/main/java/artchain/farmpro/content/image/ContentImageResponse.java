package artchain.farmpro.content.image;

public record ContentImageResponse(Long id, String title, String imageUrl) {

	public static ContentImageResponse of(ContentImage contentImage) {
		return new ContentImageResponse(contentImage.getId(), contentImage.getTitle(), contentImage.getImageUrl());
	}
}
