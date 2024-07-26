package artchain.farmpro.card.dto;

import java.util.List;

public record CardsRequest(CardRequest root, List<CardRequest> others) {
}
