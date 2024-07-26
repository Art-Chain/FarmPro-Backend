package artchain.farmpro.ai.dto;

import java.util.List;

public record GPTRecommendResponse(
		String id,
		String object,
		long created,
		String model,
		String system_fingerprint,
		List<Choice> choices,
		Usage usage
) {
	public String getContent() {
		return choices.get(0).message().content();
	}

	public record Choice(
			int index,
			Message message,
			Object logprobs,
			String finish_reason
	) {}

	public record Message(
			String role,
			String content
	) {}

	public record Usage(
			int prompt_tokens,
			int completion_tokens,
			int total_tokens
	) {}
}