package com.isaguler.llama3p1.outputparserexamples;

import com.isaguler.llama3p1.model.Author;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BookController {

    private final ChatClient chatClient;

    public BookController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    // [BeanOutputConverter]

    @GetMapping("/by-author")
    public Author getBooksByAuthor(@RequestParam(value = "author", defaultValue = "Arthur Conan Doyle") String author) {
        String message = """
                Generate a list of books written by the author {author} .
                If you are not positive that a book belongs to this author please do not include it.
                
                {format}
                """;

        BeanOutputConverter<Author> authorBeanOutputConverter = new BeanOutputConverter<>(Author.class);
        String format = authorBeanOutputConverter.getFormat();

        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("author", author, "format", format));
        Prompt prompt = promptTemplate.create();

        ChatClient.CallPromptResponseSpec responseSpec = chatClient.prompt(prompt).call();
        String content = responseSpec.content();

        return authorBeanOutputConverter.convert(content);
    }

    // [MapOutputConverter]

    @GetMapping("/author/{author}")
    public Map<String, Object> getAuthorsSocialLinks(@PathVariable String author) {
        String message = """
                Generate a list of links for the author {author} .
                Include the authors name as the key and any social media network links as the object.
                
                {format}
                """;

        MapOutputConverter mapOutputConverter = new MapOutputConverter();
        String format = mapOutputConverter.getFormat();

        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("author", author, "format", format));
        Prompt prompt = promptTemplate.create();

        ChatClient.CallPromptResponseSpec responseSpec = chatClient.prompt(prompt).call();
        String content = responseSpec.content();

        return mapOutputConverter.convert(content);
    }

}
