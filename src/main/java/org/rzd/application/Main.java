package org.rzd.application;

import org.rzd.config.ApplicationConfig;
import org.rzd.model.TicketOptions;
import org.rzd.server.CatchersServerImpl;
import org.rzd.services.TicketCatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        TicketOptions ticket1 = new TicketOptions(
                "2000000",
                "2010001",
                "6.12.2024",
                "104Я",
                "Сид",
                2000L);

        TicketOptions ticket2 = new TicketOptions(
                "2000000",
                "2010001",
                "29.11.2024",
                "106Я",
                "Сид",
                2000L);

        CatchersServerImpl server = context.getBean(CatchersServerImpl.class);
        server.start();
        server.newCatcher(ticket1);
        server.botApi.allCatchers(519674552L);
//        server.newCatcher(ticket2);
//        server.newCatcher(ticket1);
//        server.activeCatchers();
//        server.allCatchers();
//        Thread.sleep(10000L);
//        System.out.println("Catcher stopped in 10s");
//        server.activeCatchers();
//        server.allCatchers();
//        Thread t1 = new TicketCatcher(context, ticket1);
//        Thread t2 = new TicketCatcher(context, ticket2);
//        t1.start();
//        t2.start();
//        TicketCatcher ticketCatcher = new TicketCatcher(context, ticketOptions);
//        TicketCatcher ticketCatcher = context.getBean("TicketCatcher", TicketCatcher.class);
//        ticketCatcher.catchTicket();
    }
}