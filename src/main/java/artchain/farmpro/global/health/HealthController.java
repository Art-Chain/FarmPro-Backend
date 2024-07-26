package artchain.farmpro.global.health;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "서버 상태 확인 API")
@RestController
public class HealthController {
	@GetMapping("/health")
	public String health() {
		return "OK";
	}
}
