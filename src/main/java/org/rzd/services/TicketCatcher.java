package org.rzd.services;

import org.rzd.config.ApplicationConfig;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.Car;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

//@Component("TicketCatcher")
public class TicketCatcher extends Thread {
//    private final ApplicationConfig applicationConfig;
    ApplicationContext context; //  Настройки не из контекста, а при создании объекта
    LoaderTrains loaderTrains;
    ApplicationOptions applicationOptions;
    TicketOptions ticketOptions;


    public TicketCatcher(ApplicationContext applicationContext, TicketOptions ticketOptions) {
        context = applicationContext;
        applicationOptions = context.getBean("getApplicationOptions", ApplicationOptions.class);
        this.ticketOptions = ticketOptions;
        loaderTrains = new LoaderTrains(context, ticketOptions);


//        context = loaderTrains.context;
//        this.applicationConfig = applicationConfig;
    }

    public void run() {
        catchTicket();
    }

    public void catchTicket() {
        boolean gotcha = false;

        while (!gotcha) {
            List<Train> trainList = loaderTrains.getTrainList();
            for (Train train : trainList) {
                if(train.getNumber().equals(ticketOptions.getNumber())){
                    for (Car car : train.getCarList()) {
                        if(car.getType().equals(ticketOptions.getType())&&((car.getFreeSeats()>0)&&(car.getTariff()< ticketOptions.getMaxprice()))){
                            gotcha = true;

                            System.out.println("GOTCHA!!!");
//                            sendMessage("GOTCHA!!!\n" + "Train: " + train.getNumber() +"\nDeparture: " + train.getTime0() + "\nFree seats: " + car.getFreeSeats() + "\nTariff: " + car.getTariff());
                        }
                    }
                }
            }
            try {
                if(!gotcha)
                {
                    System.out.println("Ticket not found");
                    Thread.sleep(applicationOptions.getTimeout());
                }
            }
            catch (InterruptedException e) {
                System.err.println("Interrupted Exception");
            }
        }
    }

    public void sendMessage(String message) {

        String SendMessageCommand = "curl -s -d \"chat_id=519674552&disable_web_page_preview=1&text="+message+"\" https://api.telegram.org/bot6820154944:AAFxfz8Wb3QOnYNCCqV7jpo0pkoeG0--3uE/sendMessage";
        try {
            Runtime.getRuntime().exec(SendMessageCommand);
        }
        catch (IOException e) {
            System.err.println("IO Exception");
        }

    }
}
