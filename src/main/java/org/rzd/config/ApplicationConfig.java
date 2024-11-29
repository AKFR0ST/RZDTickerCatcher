package org.rzd.config;

import org.rzd.bot.*;
import org.rzd.model.ApplicationOptions;
import org.rzd.server.CatchersServer;
import org.rzd.server.CatchersServerImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

//@Profile("dev")
@Configuration
@ComponentScan("org.rzd")
@PropertySource(value = "classpath:app.properties", encoding = "UTF-8")

public class ApplicationConfig {
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
    public ApplicationOptions getApplicationOptions(){
        return new ApplicationOptions(urlApi, layer_id, timeout, botId, apiKey);
    }

    @Bean
    public MessageSender getMessageSender(ApplicationOptions applicationOptions){
        return new MessageSenderImpl(applicationOptions);
    }
    
    @Bean
    public BotInterface getBotInterface(CatchersServerImpl server, ApplicationOptions options, @Qualifier("MessageSenderImpl") MessageSender messageSender){
        return new BotInterfaceImpl(server, options, messageSender);
    }

    @Bean
    public CatchersServer getCatchersServer(ApplicationContext applicationContext){
        return new CatchersServerImpl(applicationContext);
    }




}

