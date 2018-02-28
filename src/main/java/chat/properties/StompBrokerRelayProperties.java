package chat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;

import static java.util.concurrent.TimeUnit.SECONDS;


@Data
@ConfigurationProperties(prefix = "stomp-broker-relay")
public class StompBrokerRelayProperties {

    private boolean autoStartup = true;

    private boolean relaySsl = false;

    @NotEmpty
    private String relayHost = "127.0.0.1";
    private int relayPort = 61613;

    @NotEmpty
    private String clientLogin = "guest";
    @NotEmpty
    private String clientPasscode = "guest";

    @NotEmpty
    private String systemLogin = "guest";
    @NotEmpty
    private String systemPasscode = "guest";

    private long systemHeartbeatSendInterval = SECONDS.toMillis(10);
    private long systemHeartbeatReceiveInterval = SECONDS.toMillis(10);

    @Nullable
    private String virtualHost;
    @Nullable
    private String userDestinationBroadcast;
    @Nullable
    private String userRegistryBroadcast;
}
