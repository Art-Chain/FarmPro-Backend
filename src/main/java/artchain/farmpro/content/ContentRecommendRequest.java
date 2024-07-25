package artchain.farmpro.content;

public record ContentRecommendRequest(
		ProjectInfoRequest projectInfo,
		ContentPurpose contentPurpose
) {
}
