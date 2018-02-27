package chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Wither;

import java.util.List;


@Data
@Wither
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistory {

    private final String type = "history";

    @Singular("payload")
    private List<ChatMessage> payload;
}
