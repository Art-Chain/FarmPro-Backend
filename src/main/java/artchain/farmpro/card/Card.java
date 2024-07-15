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

    private String title;

    @Column(nullable = false)
    private String keywords;

    private String cardStyle;

    public Card(Content content, String title, String keywords, String cardStyle) {
        this.content = content;
        this.title = title;
        this.keywords = keywords;
        this.cardStyle = cardStyle;
    }
}
