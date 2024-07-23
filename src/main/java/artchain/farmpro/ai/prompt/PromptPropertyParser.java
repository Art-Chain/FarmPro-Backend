package artchain.farmpro.ai.prompt;

import artchain.farmpro.card.CardStyle;
import artchain.farmpro.content.ContentPurpose;
import artchain.farmpro.content.ContentType;
import artchain.farmpro.content.ParlanceStyle;

public record PromptPropertyParser(String objectName, ContentType contentType, ContentPurpose contentPurpose,
                                   String mainText, ParlanceStyle parlanceStyle, CardStyle cardStyle) {
	// TODO : 키워드 !!! List<String>
}
