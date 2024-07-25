package artchain.farmpro.content.image;

import java.util.Map;

public record ContentImagePresignedUrlVO(
		String fileName,
		Map<String, String> urls
) {
	public static ContentImagePresignedUrlVO of(String fileName, Map<String, String> urls) {
		return new ContentImagePresignedUrlVO(fileName, urls);
	}
}