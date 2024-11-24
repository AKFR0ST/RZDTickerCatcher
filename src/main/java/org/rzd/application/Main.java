package org.rzd.application;

import org.rzd.config.ApplicationConfig;
import org.rzd.model.TicketOptions;
import org.rzd.services.TicketCatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        TicketOptions ticketOptions = new TicketOptions(
                "2000000",
                "2010001",
                "25.11.2024",
                "106Я",
                "Сид",
                2000L);
        TicketCatcher ticketCatcher = new TicketCatcher(context, ticketOptions);
//        TicketCatcher ticketCatcher = context.getBean("TicketCatcher", TicketCatcher.class);
        ticketCatcher.catchTicket();
    }
}