package com.example.Text2Emoji;
import com.example.Text2Emoji.Textservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/text2emoji")
public class Controller {


    @Autowired
    private final Textservice serv;

    public Controller(Textservice serv) {
        this.serv = serv;
    }


    @PostMapping
    public ResponseEntity<String> convert(@RequestParam String txt) {
        String prmpt= "Convert as many words as possible in the text to emojis" +
                "if the word has no emoji leave it as it is.\n" +
                "only return transformed response " +
                "no comments no labels" +
                "The text is: " + txt;
        String response=serv.convert(prmpt);
        return ResponseEntity.ok(response);
    }
}
