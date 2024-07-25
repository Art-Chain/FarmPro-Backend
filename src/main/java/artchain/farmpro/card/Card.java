package artchain.farmpro.card;

import artchain.farmpro.content.Content;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Card {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "content_id")
	private Content content;

	@Column(length = 1000)
	private String title;

	@Column(nullable = false)
	private String keywords;

	private CardStyle cardStyle;

	@Column(length = 1000)
	private String imageUrl;

	public Card(Content content, String title, String keywords, CardStyle cardStyle, String imageUrl) {
		this.content = content;
		this.title = title;
		this.keywords = keywords;
		this.cardStyle = cardStyle;
		this.imageUrl = imageUrl;
	}
}
