package artchain.farmpro.content;

import artchain.farmpro.ai.ChatGptResponse;
import artchain.farmpro.ai.ChatGptWebClient;
import artchain.farmpro.card.Card;
import artchain.farmpro.card.CardRepository;
import artchain.farmpro.card.CardRequest;
import artchain.farmpro.card.CardsRequest;
import artchain.farmpro.crop.CropRepository;
import artchain.farmpro.selectedcrop.SelectedCrop;
import artchain.farmpro.selectedcrop.SelectedCropRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

    @Transactional
    public List<ChatGptResponse> createContent(ContentRequest request) {
        Content content = saveContent(request);
        saveSelectedCrops(request, content);
        List<Card> cards = saveCards(request, content);
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

    public ContentResponses getContentFeed() {
        List<Content> contents = contentRepository.findTop3ByOrderByIdAsc();
        return ContentResponses.from(contents);
    }
}
