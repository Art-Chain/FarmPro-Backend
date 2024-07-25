package artchain.farmpro.content;

import artchain.farmpro.ai.ChatGptResponse;
import artchain.farmpro.ai.ChatGptWebClient;
import artchain.farmpro.ai.prompt.PromptContext;
import artchain.farmpro.card.Card;
import artchain.farmpro.card.CardRepository;
import artchain.farmpro.card.CardRequest;
import artchain.farmpro.card.CardsRequest;
import artchain.farmpro.content.image.ContentImage;
import artchain.farmpro.crop.CropRepository;
import artchain.farmpro.selectedcrop.SelectedCrop;
import artchain.farmpro.selectedcrop.SelectedCropRepository;
import java.util.ArrayList;
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
	public ContentCreateResponse createContent(ContentRequest request) {
		Content content = saveContent(request);
		saveSelectedCrops(request, content);
		List<Card> cards = saveCards(request, content);

		List<ChatGptResponse> chatGptResponses = sendContentImagePrompt(cards);
		List<ContentImage> contentImages = new ArrayList<>();
		for (int i = 0; i < chatGptResponses.size(); i++) {
//			String cardTitle = cards.get(i).getTitle();
			String cardTitle = sendImageTitlePrompt(request);
			String imageUrl = chatGptResponses.get(i).data().get(0).url();
			contentImages.add(new ContentImage(cardTitle, imageUrl, content));
		}
		String generatedTitle = sendContentTextPrompt(request);
		String generatedMainText = sendMainTextPrompt(request);
		content.setImages(contentImages);
		content.setGeneratedTitle(generatedTitle);
		content.setGeneratedMainText(generatedMainText);
		return ContentCreateResponse.of(content, chatGptResponses);
	}

	private ContentRecommendResponse createRecommendContent(ContentRecommendRequest request) {
		return new ContentRecommendResponse(sendRecommendTitlePrompt(request), sendRecommendKeywordPrompt(request));
	}

	private List<ChatGptResponse> sendContentImagePrompt(List<Card> cards) {
		List<String> prompts = cards.stream()
				.map(this::generateImagePrompt)
				.toList();

		Flux<ChatGptResponse> responseFlux = Flux.fromIterable(prompts)
				.flatMap(prompt -> chatGptWebClient.requestImageGenerate(prompt));

		return responseFlux.collectList().block();
	}

	private String sendMainTextPrompt(ContentRequest request) {
		String prompt = generateMainTextPrompt(request);
		return chatGptWebClient.requestTextGenerate(prompt).getContent();
	}

	private String sendContentTextPrompt(ContentRequest request) {
		String prompt = generateContentTextPrompt(request);
		return chatGptWebClient.requestTextGenerate(prompt).getContent();
	}

	private String sendRecommendTitlePrompt(ContentRecommendRequest request) {
		String prompt = generateRecommendPrompt(request);
		return chatGptWebClient.requestTextGenerate(prompt).getContent();
	}

	private String sendImageTitlePrompt(ContentRequest request) {
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

	private String generateImagePrompt(Card card) {
		return String.format("""
				     You're an image expert at grocery picture generator and descriptor.
				     Create fruit, vegetable picture reference below information.
				     image has some feature that result must referenced.
				     - image`s main object is titled %s.
				     - image's keywords are %s. 
				     - image has a style of %s.
				     - (Optional) The card has a content like %s.
				     Card:
				""", card.getTitle(), card.getKeywords(), card.getCardStyle(), "");
	}

	private String generateContentTextPrompt(ContentRequest request) {
		return String.format("""
						You're an korean expert at creating grocery marketing content.
						Must written in Korean in 50 word. Only provide title result with no explanation.
						Create short slogan for the grocery marketing card.
						result : "당도 200퍼센트 딸기의 계절, 그린팜으로 오세요!"
						The content has some reference:
						- crop name: %s.
						- farm description: %s.
						- The crop detail name: %s.
						- The growing method: %s.
						- price :%s.
						- The contact info: %s.
						- The purpose is %s.
						""",
				request.projectInfo().cropCategoryName(),
				request.projectInfo().plantDescription(),
				request.projectInfo().cropDetailName(),
				request.projectInfo().growMethod(),
				request.projectInfo().cropPrice(),
				request.projectInfo().plantContactInfo(),
				request.contentPurpose()
		);
	}

	private String generateMainTextPrompt(ContentRequest request) {
		return String.format("""
						You're an korean expert at creating grocery marketing content.
						Must written in Korean. must length in 200 word.
						Create must length in 200 word slogan for the grocery marketing card.
						The content has some characteristics:
						- The crop name is %s.
						- The farm description is %s.
						- The crop detail name is %s.
						- The growing method is %s.
						- The price is %s.
						- The contact info is %s.
						- The purpose is %s.
						""",
				request.projectInfo().cropCategoryName(),
				request.projectInfo().plantDescription(),
				request.projectInfo().cropDetailName(),
				request.projectInfo().growMethod(),
				request.projectInfo().cropPrice(),
				request.projectInfo().plantContactInfo(),
				request.contentPurpose()
		);
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

	public String generateRecommendPrompt(ContentRecommendRequest request) {
		return String.format(""" 
						    You're an korean expert at creating titles from descriptions. result must written in Korean.
						    The content has some characteristics:
						              - The crop name is %s.
						              - The farm description is %s.
						              - The crop detail name is %s.
						              - The growing method is %s.
						              - The price is %s.
						              - The contact info is %s.
						              - The purpose is %s.
						    Based on the above information, please generate appropriate titles and recommended keywords for result: 깨끗한 환경에서 자란 홍로사과 - 유기농, 무농약
						""",
				request.projectInfo().cropCategoryName(),
				request.projectInfo().plantDescription(),
				request.projectInfo().cropDetailName(),
				request.projectInfo().growMethod(),
				request.projectInfo().cropPrice(),
				request.projectInfo().plantContactInfo(),
				request.contentPurpose());
	}

	public String generateRecommendPrompt(ContentRequest request) {
		return String.format(""" 
						You're an expert at creating titles from descriptions. result must written in Korean.
						result must 1 line slogan for the grocery marketing card. Must written in Korean.
						title is less than 50 words.
						Appear farm information in the slogan as you can.
												    example : 깨끗한 환경에서 자란 홍로사과 - 유기농, 무농약
						The content has some characteristics:
						          - The crop name is %s.
						          - The farm description is %s.
						          - The crop detail name is %s.
						          - The growing method is %s.
						          - The price is %s.
						          - The contact info is %s.
						          - The purpose is %s.""",
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
						    You're an korean expert at creating keyword generator from descriptions. result must written in Korean.
						    example : 유기농, 홍로사과, 무농약, 깨끗한 환경, 신선한 과일
							result must length in 50 word.
						    The content has some characteristics:
						              - The crop name is %s.
						              - The farm description is %s.
						              - The crop detail name is %s.
						              - The growing method is %s.
						              - The price is %s.
						              - The contact info is %s.
						              - The purpose is %s.
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
