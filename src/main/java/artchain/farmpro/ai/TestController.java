package artchain.farmpro.ai;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class TestController {

    private ChatGptWebClient chatGptWebClient;

    @GetMapping("/test")
    public ChatGptResponse test(@RequestParam String prompt) {
        return chatGptWebClient.requestWithPrompt(prompt);
    }
}
