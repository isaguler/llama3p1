package com.isaguler.llama3p1.stufftheprompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MeController {

    private final ChatClient chatClient;
    @Value("classpath:/prompts/isa-guler.st")
    private Resource isaResourcePrompt;
    @Value("classpath:/docs/isa-guler.txt")
    private Resource isaDocs;

    public MeController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/isa")
    public String get2024Olympics(@RequestParam(value = "message", defaultValue = "Who is Software Developer Isa Guler?") String message,
                                  @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit) {
        PromptTemplate promptTemplate = new PromptTemplate(isaResourcePrompt);

        Map<String, Object> map = new HashMap<>();
        map.put("question", message);

        if (stuffit) {
            map.put("context", isaDocs);
        } else {
            map.put("context", "");
        }

        Prompt prompt = promptTemplate.create(map);
        ChatClient.CallPromptResponseSpec responseSpec = chatClient.prompt(prompt).call();

        return responseSpec.content();
    }
}
