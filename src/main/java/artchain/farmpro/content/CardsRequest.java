package artchain.farmpro.content;

import java.util.List;

public record CardsRequest(CardRequest root, List<CardRequest> others) {
}
