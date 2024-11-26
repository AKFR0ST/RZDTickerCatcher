package org.rzd.services;

import org.rzd.bot.BotInterfaceImpl;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.Car;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.springframework.context.ApplicationContext;
import java.util.List;

public class TicketCatcher extends Thread {
    ApplicationContext context;
    LoaderTrains loaderTrains;
    ApplicationOptions applicationOptions;
    TicketOptions ticketOptions;
    boolean receiveStopCommand;
    BotInterfaceImpl bot;
    Long chatId;

    public TicketCatcher(ApplicationContext applicationContext, TicketOptions ticketOptions, Long chatId) {
        context = applicationContext;
        applicationOptions = context.getBean("getApplicationOptions", ApplicationOptions.class);
        this.ticketOptions = ticketOptions;
        loaderTrains = new LoaderTrains(context, ticketOptions);
        bot = context.getBean("BotApiImpl", BotInterfaceImpl.class);
        this.chatId = chatId;
    }

    public void run() {
        catchTicket();
    }

    public TicketOptions getTicketOptions() {
        return ticketOptions;
    }

    public void sendStopCommand(){
        receiveStopCommand = true;
    }

    public void catchTicket() {
        boolean gotcha = false;
        receiveStopCommand = false;

        while (!gotcha&&!receiveStopCommand) {
            List<Train> trainList = loaderTrains.getTrainList();
            for (Train train : trainList) {
                if(train.getNumber().equals(ticketOptions.getNumber())){
                    for (Car car : train.getCarList()) {
                        if(car.getType().equals(ticketOptions.getType())&&((car.getFreeSeats()>0)&&(car.getTariff()< ticketOptions.getMaxPrice()))){
                            gotcha = true;
                            System.out.println("GOTCHA!!!");
                            bot.sendMessage(chatId, "The ticket is caught! \n Train: " + train.getNumber() +"\nDeparture: " + train.getTime0() + "\nFree seats: " + car.getFreeSeats() + "\nTariff: " + car.getTariff());
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
}
