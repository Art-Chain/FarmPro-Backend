package artchain.farmpro.ai.prompt;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class PromptContext {
	private final List<PromptStrategy> promptStrategies = new ArrayList<>();

	@PostConstruct
	void initSocialLoginContext() {
		promptStrategies.add(new SnsImagePromptStrategy());
		promptStrategies.add(new SnsContentPromptStrategy());
	}

	public String executePrompt(final PromptPropertyParser request) throws BadRequestException {
		for (PromptStrategy strategy : promptStrategies) {
			return strategy.createParsedPrompt(request);
		}
		throw new BadRequestException("잘못된 형식의 모델 요청입니다.");
	}
}