package artchain.farmpro.crop;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class CropController {

    private CropService cropService;

    @GetMapping("/keywords")
    public ResponseEntity<CropResponses> getCropsWithKeywords(@RequestParam String keyword) {
        CropResponses response = cropService.findCrop(keyword);
        return ResponseEntity.ok(response);
    }
}
