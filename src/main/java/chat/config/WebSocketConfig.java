package chat.config;

import chat.properties.StompBrokerRelayProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.broker.AbstractBrokerMessageHandler;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompBrokerRelayMessageHandler;
import org.springframework.messaging.simp.stomp.StompDecoder;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import reactor.ipc.netty.options.ClientOptions;

import java.util.function.Consumer;


@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(StompBrokerRelayProperties.class)
public class WebSocketConfig extends DelegatingWebSocketMessageBrokerConfiguration {

    private final StompBrokerRelayProperties properties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/");
        config.enableStompBrokerRelay("/topic")
                .setAutoStartup(properties.isAutoStartup())

                .setRelayHost(properties.getRelayHost())
                .setRelayPort(properties.getRelayPort())

                .setClientLogin(properties.getClientLogin())
                .setClientPasscode(properties.getClientPasscode())

                .setSystemLogin(properties.getSystemLogin())
                .setSystemPasscode(properties.getSystemPasscode())

                .setSystemHeartbeatSendInterval(properties.getSystemHeartbeatSendInterval())
                .setSystemHeartbeatReceiveInterval(properties.getSystemHeartbeatReceiveInterval())

                .setVirtualHost(properties.getVirtualHost())
                .setUserDestinationBroadcast(properties.getUserDestinationBroadcast())
                .setUserRegistryBroadcast(properties.getUserRegistryBroadcast());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws");
    }

    @Bean
    @Override
    public StompBrokerRelayMessageHandler stompBrokerRelayMessageHandler() {
        final AbstractBrokerMessageHandler messageHandler = super.stompBrokerRelayMessageHandler();
        final StompBrokerRelayMessageHandler handler = (StompBrokerRelayMessageHandler) messageHandler;
        handler.setTcpClient(new ReactorNettyTcpClient<>(clientOptionsBuilder(handler), codec(handler)));
        return handler;
    }

    private Consumer<ClientOptions.Builder<?>> clientOptionsBuilder(final StompBrokerRelayMessageHandler handler) {
        return builder -> {
            builder.host(handler.getRelayHost()).port(handler.getRelayPort());
            if (properties.isRelaySsl()) {
                builder.sslSupport();
            }
        };
    }

    private ReactorNettyCodec<byte[]> codec(final StompBrokerRelayMessageHandler handler) {
        final StompDecoder decoder = new StompDecoder();
        decoder.setHeaderInitializer(handler.getHeaderInitializer());
        return new StompReactorNettyCodec(decoder);
    }
}