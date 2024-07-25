package artchain.farmpro.crop;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Crop", description = "선택 가능한 농작물 정보 API")
@AllArgsConstructor
@RestController
public class CropController {

	private CropService cropService;

	@Operation(summary = "키워드로 농작물 검색", description = "키워드로 농작물을 검색합니다.")
	@GetMapping("/crops/search")
	public ResponseEntity<CropResponses> getCropsWithKeywords(@RequestParam String keyword) {
		CropResponses response = cropService.findCrop(keyword);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "농작물 목록 조회", description = "컨텐츠로 선택 가능한 농작물 목록을 조회합니다.")
	@GetMapping("/crops")
	public ResponseEntity<CropResponses> getCrops() {
		CropResponses response = cropService.findAll();
		return ResponseEntity.ok(response);
	}

}
