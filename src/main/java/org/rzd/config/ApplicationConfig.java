package org.rzd.config;

import org.rzd.model.ApplicationOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
}
