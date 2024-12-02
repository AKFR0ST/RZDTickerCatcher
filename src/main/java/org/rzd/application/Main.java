package org.rzd.application;

import org.rzd.config.ApplicationConfig;
import org.rzd.server.CatchersServer;
import org.rzd.server.CatchersServerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        CatchersServerImpl server = (CatchersServerImpl) context.getBean(CatchersServer.class);
        server.start();
    }
}