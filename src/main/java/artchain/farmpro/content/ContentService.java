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
	public ContentResponse createContent(ContentRequest request) {
		Content content = saveContent(request);
		saveSelectedCrops(request, content);
		List<Card> cards = saveCards(request, content);

//		String parlancePrompt = request.parlanceStyle().getDescription();
//		String cardStylePrompt = request.cardStyle().getDescription();

		List<ChatGptResponse> imageResponses = sendContentImagePrompt(cards);
		String contentText = sendContentTextPrompt(request);
		ContentImageResponses contentImageResponses = ContentImageResponses.fromGptResponses(imageResponses);
		return ContentResponse.of(content, contentImageResponses);
	}

	private ContentRecommendResponse createRecommendContent(ContentRecommendRequest request) {
		return new ContentRecommendResponse(sendRecommendTitlePrompt(request), sendRecommendKeywordPrompt(request));
	}

	private List<ChatGptResponse> sendContentImagePrompt(List<Card> cards) {
		List<String> prompts = cards.stream()
				.map(this::generatePrompt)
				.toList();

		Flux<ChatGptResponse> responseFlux = Flux.fromIterable(prompts)
				.flatMap(prompt -> chatGptWebClient.requestImageGenerate(prompt));

		return responseFlux.collectList().block();
	}

	private String sendContentTextPrompt(ContentRequest request) {
		String prompt = generateContentTextPrompt(request);
		return chatGptWebClient.requestTextGenerate(prompt).getContent();
	}

	private String sendRecommendTitlePrompt(ContentRecommendRequest request) {
		String prompt = generateRecommendPrompt(request);
		return chatGptWebClient.requestTextGenerate(prompt).getContent();
	}

	private String sendRecommendKeywordPrompt(ContentRecommendRequest request) {
		String prompt = generateRecommendKeywordPrompt(request);
		return chatGptWebClient.requestKeywordGenerate(prompt).getContent();
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

	private Card saveCard(CardRequest cardRequest, Content content, String cardStyle, String imageUrl) {
		Card card = new Card(content, cardRequest.title(), cardRequest.keywords(), cardStyle, imageUrl);
		return cardRepository.save(card);
	}

	private List<Card> saveCards(ContentRequest request, Content content) {
		CardsRequest cardRequests = request.cards();
		CardRequest rootCardRequest = cardRequests.root();
		saveCard(rootCardRequest, content, request.cardStyle(), rootCardRequest.url());

		List<Card> cards = cardRequests.others().stream()
				.map(each -> saveCard(each, content, request.cardStyle(), each.url()))
				.collect(Collectors.toList());
		cards.add(saveCard(rootCardRequest, content, request.cardStyle(), rootCardRequest.url()));

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

	private String generateContentTextPrompt(ContentRequest request) {
		return String.format("""
						You're an expert at creating grocery marketing content.
						The content has some characteristics:
						- The crop name is %s.
						- The farm description is %s.
						- The crop detail name is %s.
						- The growing method is %s.
						- The price is %s.
						- The contact info is %s.
						- The purpose is %s.
						Based on the above information, please generate appropriate content for the following format:
						%s
						""",
				request.projectInfo().cropCategoryName(),
				request.projectInfo().plantDescription(),
				request.projectInfo().cropDetailName(),
				request.projectInfo().growMethod(),
				request.projectInfo().cropPrice(),
				request.projectInfo().plantContactInfo(),
				request.contentPurpose(),
				"");
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

	public String generateRecommendPrompt(ContentRecommendRequest request) {
		return String.format(""" 
						    You're an expert at creating titles from descriptions. result must written in Korean.
						    The content has some characteristics:
						              - The crop name is %s.
						              - The farm description is %s.
						              - The crop detail name is %s.
						              - The growing method is %s.
						              - The price is %s.
						              - The contact info is %s.
						              - The purpose is %s.
						    Based on the above information, please generate appropriate titles and recommended keywords for each result in the following format:
						            깨끗한 환경에서 자란 홍로사과 - 유기농, 무농약
						             Example:
						             Crop Name: Apple
						             Farm Description: Organic apples grown in pristine conditions
						             Crop Detail Name: Hongro Apple
						             Growing Method: Pesticide-free cultivation
						             Price: $10 per box
						             Contact Info: 010-1234-5678
						             Purpose: PROMOTION
						             Generated Title : Korean Title(Must be in Korean): "깨끗한 환경에서 자란 홍로사과 - 유기농, 무농약",
						""",
				request.projectInfo().cropCategoryName(),
				request.projectInfo().plantDescription(),
				request.projectInfo().cropDetailName(),
				request.projectInfo().growMethod(),
				request.projectInfo().cropPrice(),
				request.projectInfo().plantContactInfo(),
				request.contentPurpose());
	}

	public String generateRecommendKeywordPrompt(ContentRecommendRequest request) {
		return String.format(""" 
						    You're an expert at creating titles from descriptions. result must written in Korean.
						    The content has some characteristics:
						              - The crop name is %s.
						              - The farm description is %s.
						              - The crop detail name is %s.
						              - The growing method is %s.
						              - The price is %s.
						              - The contact info is %s.
						              - The purpose is %s.
						    Based on the above information, please generate appropriate titles and recommended keywords for each result in the following format:
						            유기농, 홍로사과, 무농약, 깨끗한 환경, 신선한 과일
						             Example:
						             Crop Name: Apple
						             Farm Description: Organic apples grown in pristine conditions
						             Crop Detail Name: Hongro Apple
						             Growing Method: Pesticide-free cultivation
						             Price: $10 per box
						             Contact Info: 010-1234-5678
						             Purpose: PROMOTION
						             Generated Result Keywords: 유기농, 홍로사과, 무농약, 깨끗한 환경, 신선한 과일,
						""",
				request.projectInfo().cropCategoryName(),
				request.projectInfo().plantDescription(),
				request.projectInfo().cropDetailName(),
				request.projectInfo().growMethod(),
				request.projectInfo().cropPrice(),
				request.projectInfo().plantContactInfo(),
				request.contentPurpose());
	}
}
