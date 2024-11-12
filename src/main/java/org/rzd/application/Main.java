package org.rzd.application;

import org.rzd.config.ApplicationConfig;
import org.rzd.services.TicketCatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        TicketCatcher ticketCatcher = context.getBean("TicketCatcher", TicketCatcher.class);
        ticketCatcher.catchTicket();
    }
}