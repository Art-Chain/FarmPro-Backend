package artchain.farmpro.content;

import lombok.Getter;

@Getter
public enum ParlanceStyle {

	INFORMATIVE("Informative",
			"We provide information on the nutritional content and efficacy of our agricultural products."),
	HUMOROUS("Humorous",
			"This agricultural product will make you feel better when you eat it! No joke! :smile_face:"),
	EMOTIONAL("Emotional",
			"Agricultural products grown with care, one by one, add emotion to your dining table."),
	ATTRACTIVE("Attractive",
			"The best choice for maintaining health, buy our agricultural products now!"),
	STORYTELLER("Storyteller",
			"We will tell you the story of how our agricultural products grew up."),
	PROFESSIONAL("Professional",
			"Professional agricultural products that are good for your health.");

	private final String name;

	private final String description;

	ParlanceStyle(String name, String description) {
		this.name = name;
		this.description = description;
	}
}

