package artchain.farmpro.content.image;

import artchain.farmpro.content.Content;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ContentImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "image_url", length = 1000)
	private String imageUrl;

	@ManyToOne
	@JoinColumn(name = "content_id")
	private Content content;

	public ContentImage(String imageUrl, Content content) {
		this.imageUrl = imageUrl;
		this.content = content;
	}
}
