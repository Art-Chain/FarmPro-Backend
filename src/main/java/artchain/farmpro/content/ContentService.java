package artchain.farmpro.content;

import artchain.farmpro.ai.ChatGptResponse;
import artchain.farmpro.ai.ChatGptWebClient;
import artchain.farmpro.ai.prompt.PromptContext;
import artchain.farmpro.card.Card;
import artchain.farmpro.card.CardRepository;
import artchain.farmpro.card.CardRequest;
import artchain.farmpro.card.CardsRequest;
import artchain.farmpro.content.image.ContentImageResponses;
import artchain.farmpro.crop.CropRepository;
import artchain.farmpro.selectedcrop.SelectedCrop;
import artchain.farmpro.selectedcrop.SelectedCropRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class ContentService {

	private ContentRepository contentRepository;
	private CardRepository cardRepository;
	private CropRepository cropRepository;
	private SelectedCropRepository selectedCropRepository;

	private ChatGptWebClient chatGptWebClient;
	private PromptContext promptContext;

	@Transactional
	public List<ChatGptResponse> createContent(ContentRequest request) {
		Content content = saveContent(request);
		saveSelectedCrops(request, content);
		List<Card> cards = saveCards(request, content);

//		String parlancePrompt = request.parlanceStyle().getDescription();
//		String cardStylePrompt = request.cardStyle().getDescription();

		return sendPrompt(cards);
	}

	private List<ChatGptResponse> sendPrompt(List<Card> cards) {
		List<String> prompts = cards.stream()
				.map(this::generatePrompt)
				.toList();

		Flux<ChatGptResponse> responseFlux = Flux.fromIterable(prompts)
				.flatMap(prompt -> chatGptWebClient.requestWithPrompt(prompt));

		return responseFlux.collectList().block();
	}

	private Content saveContent(ContentRequest request) {
		Content content = new Content(request.contentType(), request.contentPurpose(), request.title(),
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

	private Card saveCard(CardRequest cardRequest, Content content, String cardStyle) {
		Card card = new Card(content, cardRequest.title(), cardRequest.keywords(), cardStyle);
		return cardRepository.save(card);
	}

	private List<Card> saveCards(ContentRequest request, Content content) {
		CardsRequest cardRequests = request.cards();
		CardRequest rootCardRequest = cardRequests.root();
		saveCard(rootCardRequest, content, request.cardStyle());

		List<Card> cards = cardRequests.others().stream()
				.map(each -> saveCard(each, content, request.cardStyle()))
				.collect(Collectors.toList());
		cards.add(saveCard(rootCardRequest, content, request.cardStyle()));

		return cards;
	}

	private String combinePrompt(String cardPrompt, String contentPrompt) {
		return String.format("""
				Card:
				%s
				Content:
				%s
				""", cardPrompt, contentPrompt);
	}

	private String generatePrompt(Card card) {
		// TODO : 여기를 더 구체화 -> 이건 이미지 만드는 프롬프트고, 본문 만드는 프롬프트 메소드를 따로 만들어서 generatePrompt에서 합쳐서 리턴하도록
		return String.format("""
				     You're an expert at creating card images from descriptions.
				     
				     The card has some characteristics.
				     - The card image is titled %s.
				     - The card's keywords are %s.
				     - The card has a style of %s.
				     - (Optional) The card has a content like %s.
				     
				     Card:
				""", card.getTitle(), card.getKeywords(), card.getCardStyle(), "");
	}

	private String generateContentTextPrompt(Content content) {
		return String.format("""
						     You're an expert at creating content from descriptions.
						     
						     The content has some characteristics.
						     - The content is for %s.
						     - The content has a purpose of %s.
						     - The content has a style of %s.
						     - The content has a main text like %s.
						     
						     Content:
						""", content.getContentType(), content.getContentPurpose(), content.getTextStyle(),
				content.getMainText());
	}

	public ContentResponses getContentFeed() {
		List<Content> contents = contentRepository.findTop3ByOrderByIdAsc();
		return ContentResponses.from(contents);
	}

	public ContentResponse getContentDetail(Long contentId) {
		Content content = contentRepository.findById(contentId)
				.orElseThrow(IllegalArgumentException::new);
		return ContentResponse.of(content);
	}

	public ContentResponses getContentList() {
		List<Content> contents = contentRepository.findAll();
		return ContentResponses.from(contents);
	}

	public ContentImageResponses getContentImages(Long contentId) {
		Content content = contentRepository.findById(contentId)
				.orElseThrow(IllegalArgumentException::new);
		return ContentImageResponses.from(content.getImages());
	}
}
