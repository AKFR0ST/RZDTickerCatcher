package org.rzd.config;

import org.mockito.Mockito;
import org.rzd.bot.*;
import org.rzd.model.ApplicationOptions;
import org.rzd.server.CatchersServerImpl;
import org.rzd.services.LoaderTrainsImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

//@Profile("test")
@Configuration
@ComponentScan("org.rzd")
@PropertySource(value = "classpath:app.properties", encoding = "UTF-8")

public class ApplicationConfigTest {
    @Value("${app.urlApi}")
    private String urlApi;
    @Value("${app.layer_id}")
    private String layer_id;
    @Value("${app.timeout}")
    private Long timeout;
    @Value("${app.botId}")
    private String botId;
    @Value("${app.apiKey}")
    private String apiKey;

    @Bean
    public ApplicationOptions getApplicationOptions() {
        return new ApplicationOptions(urlApi, layer_id, timeout, botId, apiKey);
    }

    @Bean
    public BotInterface getBotInterface(CatchersServerImpl server, ApplicationOptions options, @Qualifier("MessageSenderImpl") MessageSender messageSender, MessageReceiver messageReceiver, LoaderTrainsImpl loaderTrains) {
        return Mockito.mock(BotInterface.class);
    }

}
