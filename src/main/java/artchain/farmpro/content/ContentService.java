package artchain.farmpro.content;

import artchain.farmpro.card.Card;
import artchain.farmpro.card.CardRepository;
import artchain.farmpro.card.CardRequest;
import artchain.farmpro.card.CardsRequest;
import artchain.farmpro.crop.CropRepository;
import artchain.farmpro.selectedcrop.SelectedCrop;
import artchain.farmpro.selectedcrop.SelectedCropRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class ContentService {

	private ContentRepository contentRepository;
	private CardRepository cardRepository;
	private CropRepository cropRepository;
	private SelectedCropRepository selectedCropRepository;

	@Transactional
	public Long createContent(ContentRequest request) {
		Content content = saveContent(request);
		saveSelectedCrops(request, content);
		saveCards(request, content);
		return content.getId();
	}

	private Content saveContent(ContentRequest request) {
		Content content = new Content(request.contentType(), request.contentPurpose(),
				request.mainText(), request.cardStyle());
		return contentRepository.save(content);
	}

	private void saveSelectedCrops(ContentRequest request, Content content) {
		List<SelectedCrop> selectedCrops = request.crops()
				.stream()
				.map(each -> cropRepository.searchByNameIs(each.name())
						.orElseThrow(IllegalArgumentException::new))
				.map(each -> new SelectedCrop(content, each))
				.toList();
		selectedCropRepository.saveAll(selectedCrops);
	}

	private void saveCard(CardRequest cardRequest, Content content, String cardStyle) {
		Card card = new Card(content, cardRequest.title(), cardRequest.keywords(), cardStyle);
		cardRepository.save(card);
	}

	private void saveCards(ContentRequest request, Content content) {
		CardsRequest cardRequests = request.cards();
		CardRequest rootCardRequest = cardRequests.root();

		saveCard(rootCardRequest, content, request.cardStyle());
		List<CardRequest> otherCardsRequest = cardRequests.others();
		for (CardRequest cardRequest : otherCardsRequest) {
			saveCard(cardRequest, content, request.cardStyle());
		}
	}

	public ContentResponses getContentFeed() {
		List<Content> contents = contentRepository.findTop3ByOrderByIdAsc();
		return ContentResponses.from(contents);
	}
}
