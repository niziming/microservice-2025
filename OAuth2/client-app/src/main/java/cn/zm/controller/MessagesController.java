package cn.zm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

/**
 * @author Simon.ni
 */

@Controller
public class MessagesController {

    @Autowired
    private WebClient webClient;

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/messages") // 确保你的链接是 /messages
    public String getMessages(Model model
                              // @RegisteredOAuth2AuthorizedClient("oidc-client") OAuth2AuthorizedClient authorizedClient
    ) {
        String[] messages = this.webClient
                .get()
                .uri("http://localhost:8081/api/messages")
                // .attributes(oauth2AuthorizedClient(authorizedClient)) // 确保这里有参数
                .retrieve()
                .bodyToMono(String[].class)
                .block();
        model.addAttribute("messages", messages);
        return "messages";
    }
}