package org.rzd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.rzd.config.ApplicationConfig;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.rzd.services.LoaderTrains;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CatcherTest {

    ApplicationContext context;
    LoaderTrains loaderTrains;
    TicketOptions ticketOptions;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//        ticketOptions = context.getBean("getTicketOptions", TicketOptions.class);
        ticketOptions = new TicketOptions(
                "2000000",
                "2010001",
                "25.11.2024",
                "106Я",
                "Сид",
                2000L);
        loaderTrains = new LoaderTrains(context, ticketOptions);
    }

    @Test
    public void testConnection() {
        LocalDateTime nextDate = LocalDateTime.now().plusDays(1L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = nextDate.format(formatter);
        ticketOptions.setDt0(formattedDate);
        List<Train> trainList =  loaderTrains.getTrainList();
        Assertions.assertNotNull(trainList);
    }

}
