package cn.zm.ms.web;

import org.springframework.web.bind.annotation.*;


@RestController
public class PathVariableController {

    // http://127.0.0.1:8080/user/123/roles/222
    @GetMapping("/user/{userId}/roles/{roleId}")
    public String getLogin(@PathVariable("userId") String userId, @PathVariable("roleId") String roleId) {
        return "User Id : " + userId + " Role Id : " + roleId;
    }

    // http://127.0.0.1:8080/javabeat/somewords
    @GetMapping("/javabeat/{regexp1:[a-z-]+}")
    public String getRegExp(@PathVariable("regexp1") String regexp1) {
        return "URI Part : " + regexp1;
    }
}
