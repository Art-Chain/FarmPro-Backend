package artchain.farmpro.ai.prompt;

public class SnsImagePromptStrategy implements PromptStrategy {

	private static final String PROMPT = "A photo of %s style %s."
			+ " The photo will be used for marketing promotion to attract customers."
			+ " The photo should be taken in a bright place. Object should be in the center of the photo.";

	@Override
	public String createParsedPrompt(PromptPropertyParser promptPropertyParser) {
		return String.format(PROMPT, promptPropertyParser.cardStyle(), promptPropertyParser.objectName());
	}
}
