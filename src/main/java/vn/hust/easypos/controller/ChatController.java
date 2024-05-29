package vn.hust.easypos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.hust.easypos.service.dto.ChatDTO;

@Controller
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;


    /**
     * Những request đến /app/sendMessage sẽ vào đc hàm này.
     * Vì cài đặt /app ở trong WebsocketConfig router đến @MessageMapping
     *
     * @return
     */
    @MessageMapping("/sendMessage")
//    @SendTo("/topic/messages")
    public ChatDTO sendMessage(@Payload ChatDTO chatDTO) {
        this.template.convertAndSend("/topic/messages/" + chatDTO.getUserId(), chatDTO);// gửi cho chủ quán
        this.template.convertAndSend("/topic/messages/" + chatDTO.getUserId() + "/" + chatDTO.getTableId(), chatDTO); // gửi cho khách
        return chatDTO;
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor) {
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
}
