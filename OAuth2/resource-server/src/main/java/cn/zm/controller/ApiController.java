package cn.zm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Simon.ni
 */
@RestController
@RequestMapping("/api") // 使用 @RequestMapping 来定义类级别的基础路径
public class ApiController {

    @GetMapping("/messages")
    public String[] getMessages() {
        return new String[]{"Message 1", "Message 2", "Message 3 from Resource Server"};
    }
}