package com.isaguler.llama3p1.simple;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeController {

    private final ChatClient chatClient;

    public JokeController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/joke")
    public String dadJoke() {
        return chatClient.prompt()
                .user("Tell me a dad joke about computers?")
                .call()
                .content();
    }
}
