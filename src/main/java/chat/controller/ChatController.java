package chat.controller;

import chat.annotations.Username;
import chat.model.ChatHistory;
import chat.model.ChatMessage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;


@Controller
@AllArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final Map<ChatMessage, Void> messages = new LRUCache<>(10);

    @MessageMapping("/app/chat.{chat-id}")
    @SendTo("/topic/chat.{chat-id}")
    public ChatMessage postMessage(
            @DestinationVariable("chat-id") String chatId,
            @Username String username,
            ChatMessage message
    ) {
        final ChatMessage chatMessage = message.withUsername(username);
        messages.put(chatMessage, null);
        return chatMessage;
    }

    @SubscribeMapping("/topic/chat.{chat-id}")
    public ChatHistory readMessages(@DestinationVariable("chat-id") String chatId, @Username String username) {
        return ChatHistory.builder()
                .payload(new ChatMessage(username + ", welcome back to " + chatId, "SYSTEM"))
                .payload(messages.keySet())
                .build();
    }

}

class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int cacheSize;

    public LRUCache(int cacheSize) {
        super(16, 0.75f, true);
        this.cacheSize = cacheSize;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() >= cacheSize;
    }
}
