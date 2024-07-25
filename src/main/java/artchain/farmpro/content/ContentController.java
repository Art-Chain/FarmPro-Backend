package artchain.farmpro.content;

import artchain.farmpro.ai.ChatGptResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Content", description = "마케팅 컨텐츠 단위별 API")
@AllArgsConstructor
@RestController
public class ContentController {

	private ContentService contentService;

	@Operation(summary = "컨텐츠 생성")
	@PostMapping("/contents")
	public ResponseEntity<List<ChatGptResponse>> createContent(@RequestBody ContentRequest request) {
		return ResponseEntity.ok(contentService.createContent(request));
	}

	@Operation(summary = "컨텐츠 목록 중 상위 3개 조회")
	@GetMapping("/contents/feeds")
	public ResponseEntity<ContentResponses> getContentFeed() {
		ContentResponses contentFeed = contentService.getContentFeed();
		return ResponseEntity.ok(contentFeed);
	}

	@Operation(summary = "컨텐츠 목록 조회")
	@GetMapping("/contents/lists")
	public ResponseEntity<ContentResponses> getContentList() {
		ContentResponses contentList = contentService.getContentList();
		return ResponseEntity.ok(contentList);
	}

	@Operation(summary = "컨텐츠 상세 조회")
	@GetMapping("/contents/{contentId}")
	public ResponseEntity<ContentResponse> getContentDetail(@PathVariable Long contentId) {
		ContentResponse contentDetail = contentService.getContentDetail(contentId);
		return ResponseEntity.ok(contentDetail);
	}

	@Operation(summary = "장표별 제목과 키워드를 추천")
	@PostMapping("/contents/recommend")
	public ResponseEntity<ContentRecommendResponse> recommendContent(@RequestBody ContentRecommendRequest request) {
		String response = contentService.generateRecommendPrompt(request);
		return ResponseEntity.ok(new ContentRecommendResponse(response, response));
	}
}
