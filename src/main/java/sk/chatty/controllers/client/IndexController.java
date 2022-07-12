package sk.chatty.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "templates/index.html";
    }

    @GetMapping("/chat")
    public String chat(){
        return "templates/chat.html";
    }



    @GetMapping("/api/v1/docs")
    public String docs(){
        return "templates/swagger-ui.html";
    }

    @GetMapping("/api/v1/openapi.yaml")
    public String OpenApiV1() {
        return "docs/v1.yaml";
    }
}
