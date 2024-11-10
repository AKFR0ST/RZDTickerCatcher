package org.rzd.config;


import org.rzd.core.LoaderTrains;
import org.rzd.model.TicketOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.rzd")
@PropertySource("classpath:app.properties")

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


    //  Загрузить из конфига параметры поиска
    @Bean
    public TicketOptions getTicketOptions(){
        return new TicketOptions(urlApi, layer_id, code0, code1, dt0);
    }

    @Bean
    public LoaderTrains loadLib() {
        return new LoaderTrains();
    }

    public void setOptions() {

    }
}
