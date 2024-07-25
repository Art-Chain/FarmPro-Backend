package artchain.farmpro.content;

import artchain.farmpro.content.image.ContentImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	private ContentType contentType;

	@Enumerated(value = EnumType.STRING)
	private ContentPurpose contentPurpose;

	private String title;
	private String mainText;
	private String textStyle;

	@OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContentImage> images;

	public Content(ContentType contentType, ContentPurpose contentPurpose, String title, String mainText,
	               String textStyle) {
		this.contentType = contentType;
		this.contentPurpose = contentPurpose;
		this.title = title;
		this.mainText = mainText;
		this.textStyle = textStyle;
	}

	public void setImages(List<ContentImage> images) {
		this.images = images;
	}
}

