package cn.zm.kafka.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息对象
 *
 * @author Simon.ni
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterEvent {
    private Long userId;
    private String username;
    private String email;
}