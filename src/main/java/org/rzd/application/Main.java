package org.rzd.application;

import org.rzd.config.ApplicationConfig;
import org.rzd.core.LoaderTrains;
import org.rzd.core.TicketCatcher;
import org.rzd.model.Train;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        TicketCatcher ticketCatcher = context.getBean("TicketCatcher", TicketCatcher.class);
        ticketCatcher.catchTicket();
//        LoaderTrains loaderTrains = new LoaderTrains(context);
//        List<Train> trlst =  loaderTrains.getTrainList();
//        for (Train train : trlst) {
//            System.out.print(train);
//        }

    }
}