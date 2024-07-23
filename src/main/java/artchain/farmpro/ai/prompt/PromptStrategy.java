package artchain.farmpro.ai.prompt;

public interface PromptStrategy {
	String createParsedPrompt(PromptPropertyParser promptPropertyParser);
}
