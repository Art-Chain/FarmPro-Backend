package artchain.farmpro.ai.prompt;

public class SnsContentPromptStrategy implements PromptStrategy {
	private final String PROMPT = "SNS content prompt";

	@Override
	public String createParsedPrompt(PromptPropertyParser promptPropertyParser) {
		return String.format(PROMPT, promptPropertyParser.objectName(), promptPropertyParser.contentPurpose(),
				promptPropertyParser.parlanceStyle(), promptPropertyParser.mainText());
	}
}
