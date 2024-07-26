package artchain.farmpro.content.dto;

import artchain.farmpro.content.ContentPurpose;

public record ContentRecommendRequest(
		ProjectInfoRequest projectInfo,
		ContentPurpose contentPurpose
) {
}
