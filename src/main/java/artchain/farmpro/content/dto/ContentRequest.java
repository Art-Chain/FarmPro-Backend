package artchain.farmpro.content.dto;

import artchain.farmpro.card.CardStyle;
import artchain.farmpro.card.dto.CardsRequest;
import artchain.farmpro.content.ContentPurpose;
import artchain.farmpro.content.ContentType;
import artchain.farmpro.content.ParlanceStyle;
import artchain.farmpro.crop.dto.CropRequest;
import java.util.List;

public record ContentRequest(
		ProjectInfoRequest projectInfo,
		List<CropRequest> crops,
		ContentType contentType,
		ContentPurpose contentPurpose,
		CardsRequest cards,
		String title,
		String mainText,
		ParlanceStyle parlanceStyle,
		CardStyle cardStyle) {
}
