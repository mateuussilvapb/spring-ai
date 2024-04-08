package io.github.mateuussilvapb.bookstore.ai.controllers;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/bookstore-assistant")
public class BookstoreAssistantController {

    private final OpenAiChatClient chatClient;

    public BookstoreAssistantController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /*
     * O método abaixo recebe uma String. Ele é útil e prático, porém não reutilizável, caso
     * haja a troca de modelos de chat.
     * */
//    @GetMapping("/informations")
//    public String bookstoreChatString(@RequestParam(value = "message",
//            defaultValue = "Quais são os livros best sellers dos últimos anos?", required = false) String message) {
//        return chatClient.call(message);
//    }

    /*
     * O método abaixo recebe uma string. Ele é reutilizável, caso haja a troca de modelos
     * de chat, pois o objeto Prompt é independente do modelo de chat.
     * */
    @GetMapping("/informations")
    public ChatResponse bookstoreChatPrompt(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos últimos anos?", required = false) String message) {
        return chatClient.call(new Prompt(message));
    }

    /*
     * O método abaixo recebe uma String. Ele é útil e prático, porém não reutilizável, caso
     * haja a troca de modelos de chat. Ao invés de retornar a própria mensagem, ele retorna um
     * fluxo de mensagens, o que pode ser útil para um chat em tempo real.
     * */
//    @GetMapping("/steam/informations")
//    public Flux<String> bookstoreChatStream(@RequestParam(value = "message", defaultValue = "Quais são os livros best sellers dos últimos anos?", required = false) String message) {
//        return chatClient.stream(message);
//    }

    /*
     * O método abaixo recebe uma String. Ele é reutilizável, caso haja a troca de modelos de chat.
     * Além disso, ele retorna um fluxo de mensagens, o que pode ser útil para um chat em tempo real.
     * */
    @GetMapping("/steam/informations")
    public Flux<ChatResponse> bookstoreChatStream(@RequestParam(value = "message", defaultValue =
            "Quais são" +
                    " os livros best sellers dos últimos anos?", required = false) String message) {
        return chatClient.stream(new Prompt(message));
    }

    /*
     * O método abaixo recebe uma string. Ele é reutilizável, caso haja a troca de modelos
     * de chat, pois o objeto PromptTemplate é independente do modelo de chat.
     * Além disso, o método também cria um prompt padrão para enviar ao modelo de chat.
     * */
    @GetMapping("/reviews")
    public String bookstoreChatReviews(@RequestParam(value = "book", defaultValue = "Dom Quixote", required = false) String book) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                Por favor, me forneça uma análise completa do livro {book}
                e também a biografia do seu autor.
                """);
        promptTemplate.add("book", book);
        return chatClient.call(promptTemplate.create()).getResult().getOutput().getContent();
    }
}
