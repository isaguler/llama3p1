package com.isaguler.llama3p1.outputparserexamples;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SongsController {

    private final ChatClient chatClient;

    public SongsController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    // [ListOutputConverter]

    @GetMapping("/songs")
    public List<String> getSongsByArtist(@RequestParam(value = "artist", defaultValue = "Imagine Dragons") String artist) {
        String message = """
                Please give mea list of top 10 songs for the artist {artist} .
                Content of our response should be only song names.
                If you don't know the answer just say I don't know.
                
                {format}
                """;

        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("artist", artist, "format", listOutputConverter.getFormat()));
        Prompt prompt = promptTemplate.create();

        return listOutputConverter.convert(chatClient.prompt(prompt).call().content());
    }
}
