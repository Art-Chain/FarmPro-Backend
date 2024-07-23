package artchain.farmpro.content;

import artchain.farmpro.card.CardsRequest;
import artchain.farmpro.crop.CropRequest;
import java.util.List;

public record ContentRequest(List<CropRequest> crops, ContentType contentType,
                             ContentPurpose contentPurpose, CardsRequest cards, String mainText,
                             ParlanceStyle parlanceStyle,
                             String cardStyle) {
}
