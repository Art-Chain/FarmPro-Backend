package artchain.farmpro.ai;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ChatGptConfiguration {

    @Value("${openai.key}")
    private String openaiKey;
    @Value("${openai.image-generation.endpoint}")
    private String endPoint;
    @Value("${openai.image-generation.model}")
    private String model;
    @Value("${openai.image-generation.n}")
    private String n;
    @Value("${openai.image-generation.size}")
    private String size;
}
