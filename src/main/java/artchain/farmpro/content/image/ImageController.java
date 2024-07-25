package artchain.farmpro.content.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Image", description = "이미지 업로드 및 다운로드 API")
@AllArgsConstructor
@RestController
public class ImageController {
	
	private final ImageService imageService;

	@Operation(summary = "컨텐츠 이미지 업로드 위치 요청", description = "컨텐츠 이미지를 업로드할 수 있는 URL을 제공합니다.")
	@GetMapping("/image")
	public ResponseEntity<ContentImagePresignedUrlVO> getContentImage(
			@RequestBody ContentImageUploadRequest request) {
		return ResponseEntity
				.ok(ContentImagePresignedUrlVO.of(request.fileName(), imageService.getPresignedUrl(request)));
	}

	@Operation(summary = "컨텐츠 이미지 업로드 완료", description = "컨텐츠 이미지 업로드 완료를 알립니다.")
	@PostMapping("/image")
	public ResponseEntity<Void> notifyContentImageSaveSuccess(
			@RequestBody final NotifyContentImageSaveSuccessRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
