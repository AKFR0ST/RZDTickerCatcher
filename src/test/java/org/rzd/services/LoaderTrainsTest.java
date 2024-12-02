package org.rzd.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rzd.config.ApplicationConfigTest;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoaderTrainsTest {
    LoaderTrains loaderTrains;

    @BeforeEach
    void setUp() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfigTest.class);
        loaderTrains = (LoaderTrains) applicationContext.getBean("LoaderTrains");
    }

    @Test
    void getStationList() {
        List<String> stationList = loaderTrains.getStationList("Ярославль");
        assertNotNull(stationList);
        assertEquals("Ярославль 2010000", stationList.getFirst());
    }

    @Test
    void getTrainList() {
        LocalDateTime nextDate = LocalDateTime.now().plusDays(1L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = nextDate.format(formatter);

        TicketOptions ticketOptions = new TicketOptions("2010001", "2000000", formattedDate, "0", 6L, 100L);
        List<Train> trainList = loaderTrains.getTrainList(ticketOptions);
        assertNotNull(trainList);
    }

}