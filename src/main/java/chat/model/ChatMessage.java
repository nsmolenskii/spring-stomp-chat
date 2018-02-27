package chat.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;


@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("message")
public class ChatMessage {

    private final String type = "message";
    private String payload;
    private String username;

}
