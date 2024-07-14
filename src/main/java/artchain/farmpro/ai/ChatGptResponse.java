package artchain.farmpro.ai;

import java.util.List;

public record ChatGptResponse(List<UrlResponse> data) {

    record UrlResponse(String url, String revisedPrompt) {

    }
}
