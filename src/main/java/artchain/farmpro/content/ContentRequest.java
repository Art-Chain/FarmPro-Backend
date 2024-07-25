package artchain.farmpro.content;

import artchain.farmpro.card.CardsRequest;
import artchain.farmpro.crop.CropRequest;
import java.util.List;

public record ContentRequest(
		ProjectInfoRequest projectInfo,
		List<CropRequest> crops,
		ContentType contentType,
		ContentPurpose contentPurpose,
		CardsRequest cards,
		String mainText,
		String parlanceStyle,
		String cardStyle) {
}
