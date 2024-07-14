package artchain.farmpro.content;

import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ContentController {

    private ContentService contentService;

    @PostMapping("/contents")
    public ResponseEntity<Void> createContent(@RequestBody ContentRequest request) {
        Long contentId = contentService.createContent(request);
        return ResponseEntity.created(URI.create("/content/" + contentId))
                .build();
    }
}
