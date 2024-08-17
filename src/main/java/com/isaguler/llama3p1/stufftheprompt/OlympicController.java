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
public class OlympicController {

    private final ChatClient chatClient;
    @Value("classpath:/prompts/olympic-sports.st")
    private Resource olymipSportsResource;
    @Value("classpath:/docs/olympic-sports.txt")
    private Resource olympicSportsDocs;

    public OlympicController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/2024")
    public String get2024Olympics(@RequestParam(value = "message", defaultValue = "What sports being included in the 2024 Summer Olympics?") String message,
                                  @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit) {
        PromptTemplate promptTemplate = new PromptTemplate(olymipSportsResource);

        Map<String, Object> map = new HashMap<>();
        map.put("question", message);

        if (stuffit) {
            map.put("context", olympicSportsDocs);
        } else {
            map.put("context", "");
        }

        Prompt prompt = promptTemplate.create(map);
        ChatClient.CallPromptResponseSpec responseSpec = chatClient.prompt(prompt).call();

        return responseSpec.content();
    }
}
