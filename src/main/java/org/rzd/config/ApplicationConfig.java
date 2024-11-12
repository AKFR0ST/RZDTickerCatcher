package org.rzd.config;

import org.rzd.model.TicketOptions;
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
    @Value("${app.code0}")
    private String code0;
    @Value("${app.code1}")
    public String code1;
    @Value("${app.dt0}")
    public String dt0;
    @Value("${app.number}")
    public String number;
    @Value("${app.type}")
    public String type;
    @Value("${app.maxprice}")
    public Long maxprice;


    //  Загрузить из конфига параметры поиска
    @Bean
    public TicketOptions getTicketOptions(){
        return new TicketOptions(urlApi, layer_id, code0, code1, dt0, number, type, maxprice);
    }

}
