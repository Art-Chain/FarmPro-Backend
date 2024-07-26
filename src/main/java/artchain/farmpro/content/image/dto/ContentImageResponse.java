package artchain.farmpro.content.image.dto;

import artchain.farmpro.content.image.ContentImage;

public record ContentImageResponse(Long id, String title, String imageUrl) {

	public static ContentImageResponse of(ContentImage contentImage) {
		return new ContentImageResponse(contentImage.getId(), contentImage.getTitle(), contentImage.getImageUrl());
	}
}
