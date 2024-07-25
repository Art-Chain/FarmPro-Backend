package artchain.farmpro.content;

import artchain.farmpro.ai.ChatGptResponse;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ContentController {

	private ContentService contentService;

	@PostMapping("/contents")
	public ResponseEntity<List<ChatGptResponse>> createContent(@RequestBody ContentRequest request) {
		List<ChatGptResponse> responses = contentService.createContent(request);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/contents/feeds")
	public ResponseEntity<ContentResponses> getContentFeed() {
		ContentResponses contentFeed = contentService.getContentFeed();
		return ResponseEntity.ok(contentFeed);
	}
}
