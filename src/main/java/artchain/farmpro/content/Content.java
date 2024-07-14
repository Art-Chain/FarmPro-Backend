package artchain.farmpro.content;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String mainText;
    private String textStyle;

    public Content(ContentType contentType, ContentPurpose contentPurpose, String mainText, String textStyle) {
        this.contentType = contentType;
        this.contentPurpose = contentPurpose;
        this.mainText = mainText;
        this.textStyle = textStyle;
    }
}

