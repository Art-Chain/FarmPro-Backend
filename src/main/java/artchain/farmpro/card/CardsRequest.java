package artchain.farmpro.card;

import java.util.List;

public record CardsRequest(CardRequest root, List<CardRequest> others) {
}
