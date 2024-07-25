package artchain.farmpro.ai;

import java.util.List;

public record ChatGptResponse(List<UrlResponse> data) {

    public record UrlResponse(String url, String revisedPrompt) {

    }
}
