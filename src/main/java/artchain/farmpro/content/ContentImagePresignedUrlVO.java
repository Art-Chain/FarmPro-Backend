package artchain.farmpro.content;

public record ContentImagePresignedUrlVO(
		String fileName,
		String url
) {
	public static ContentImagePresignedUrlVO of(String fileName, String url) {
		return new ContentImagePresignedUrlVO(fileName, url);
	}
}