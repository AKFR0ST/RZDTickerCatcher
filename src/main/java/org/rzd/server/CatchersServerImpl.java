package org.rzd.server;

import org.rzd.bot.BotInterface;
import org.rzd.model.Catcher;
import org.rzd.model.TicketOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("CatchersServerImpl")
public class CatchersServerImpl implements CatchersServer {

    private ArrayList<Catcher> catchers;
    private Long lastId;
    ApplicationContext context;
    public BotInterface botApi;

    public CatchersServerImpl() {

    }

    @Autowired
    public CatchersServerImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void start() {
        System.out.println("Starting server...");
        catchers = new ArrayList<>();
        lastId = 0L;
        botApi = context.getBean(BotInterface.class);

    }

    @Override
    public void stop() {
        killAllCatchers();
        catchers.clear();
        lastId = 0L;
    }

    @Override
    public void newCatcher(TicketOptions ticketOptions) {
        Catcher catcher = new Catcher(++lastId, ticketOptions, context);
        catchers.add(catcher);
    }

    @Override
    public void activeCatchers() {
        for (Catcher catcher : catchers) {
            if(catcher.getTicketCatcher().getState()!= Thread.State.TERMINATED) {
                System.out.println("Catcher " + catcher.getId() + " is already running");
            }
        }
    }

    @Override
    public String allCatchers() {
        StringBuilder sb = new StringBuilder();
        for (Catcher catcher : catchers) {
            sb.append("Catcher ").append(catcher.getId()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void killCatcherById(Long id) {
        for (Catcher catcher : catchers) {
            if(catcher.getId().equals(id)) {
                catcher.getTicketCatcher().interrupt();
            }
        }
    }

    @Override
    public void killAllCatchers() {
        for (Catcher catcher : catchers) {
                catcher.getTicketCatcher().interrupt();
        }
    }
}
