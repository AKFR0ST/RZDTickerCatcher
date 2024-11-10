package org.rzd.application;

import org.rzd.config.ApplicationConfig;
import org.rzd.core.LoaderTrains;
import org.rzd.model.Train;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
//        System.out.println("Hello world!");
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        LoaderTrains loaderTrains = new LoaderTrains(context);
        List<Train> trlst =  loaderTrains.getTrainList();
//        System.out.println(trlst.toString());
        for (Train train : trlst) {
            System.out.print(train);
        }
    }
}