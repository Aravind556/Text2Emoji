package com.example.Text2Emoji;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.util.Map;


@Service
public class Textservice {

    Logger log = LoggerFactory.getLogger(Textservice.class);

    private final WebClient client;

    public Textservice(WebClient.Builder builder) {
        this.client = builder.build();
        log.info("Webclient created");
    }

    @Value("${Gemini_url}")
    private String url;

    @Value("${Gemini_api}")
    private String key;


    public String convert(String text){

        Map<String,Object> body =Map.of("contents",new Object[]{
                Map.of("parts",new Object[]{
                        Map.of("text",text)
        })
        });


        String response= client.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("X-goog-api-key",key)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("response.json"), root);
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
