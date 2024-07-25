package artchain.farmpro.card;

import lombok.Getter;

@Getter
public enum CardStyle {

	LAVISH("Lavish",
			"Emphasizes vibrant and intense colors with lavish details."),
	SERENE("Serene",
			"Creates a calm and stable atmosphere with soft and tranquil tones."),
	EMOTIVE("Emotive",
			"Uses warm and emotive colors with gentle expressions."),
	MODERN("Modern",
			"Utilizes sleek and contemporary design elements with clean tones."),
	HUMOROUS("Humorous",
			"Employs cheerful and funny images with bright colors to evoke laughter.");

	private final String name;

	private final String description;

	CardStyle(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
