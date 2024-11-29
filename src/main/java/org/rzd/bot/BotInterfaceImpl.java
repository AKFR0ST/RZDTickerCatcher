package org.rzd.bot;

import org.json.JSONObject;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.TicketOptions;
import org.rzd.server.CatchersServer;
import org.rzd.server.CatchersServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@Component ("BotInterfaceImpl")
public class BotInterfaceImpl implements BotInterface {

    ApplicationContext applicationContext;
    CatchersServer server;
    ApplicationOptions options;
    public Long offset;
    MessageSender messageSender;

    public BotInterfaceImpl() {

    }

//    @Autowired
    public BotInterfaceImpl(CatchersServer server, ApplicationOptions options, MessageSender messageSender) {
//        this.applicationContext = applicationContext;
        offset = 0L;
        this.server = server;
        this.options = options;
        this.messageSender = messageSender;

//        server = applicationContext.getBean(CatchersServerImpl.class);
//        options = applicationContext.getBean(ApplicationOptions.class);
//        messageSender = applicationContext.getBean(MessageSender.class);
    }

    private String readOneMessage() {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://api.telegram.org/bot%s:%s/getUpdates?limit=1&offset=%d";
        String url = String.format(baseUrl, options.getBotId(), options.getApiKey(), offset);
        String response;
        JSONObject jsonObject;

        do {
            response = restTemplate.getForEntity(url, String.class).getBody();
            if(response==null){
                throw new RuntimeException("Response is null");
            }
            jsonObject = new JSONObject(response);
        } while (jsonObject.getJSONArray("result").isEmpty());

        offset = jsonObject.getJSONArray("result").getJSONObject(0).getLong("update_id") + 1;
        return response;
    }

    @Override
    public void start() {
        String message = "";
        do {
            String response = readOneMessage();
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("ok") && !jsonObject.getJSONArray("result").isEmpty()) {

                Long chatId = jsonObject.
                        getJSONArray("result").
                        getJSONObject(0).
                        getJSONObject("message").
                        getJSONObject("chat").
                        getLong("id");

                message = jsonObject.
                        getJSONArray("result").
                        getJSONObject(0).
                        getJSONObject("message").
                        getString("text");

                switch (message) {
                    case ("/all"):
                        allCatchers(chatId);
                        break;
                    case ("/kill"):
                        killCatcher(chatId);
                        break;
                    case ("/active"):
                        activeCatchers(chatId);
                        break;
                    case ("/new"):
                        newCatcher(chatId);
                        break;
                    case ("/stop"):
                        stopServer();
                        break;
                }
            }
        } while (!message.equals("/stop")) ;
    }

    @Override
    public String allCatchers(Long chatId) {
        return messageSender.sendMessage(chatId, server.allCatchers()).toString();

    }

    @Override
    public String activeCatchers(Long chatId) {
      return   messageSender.sendMessage(chatId, server.activeCatchers()).toString();
    }

    @Override
    public String killCatcher(Long chatId) {
        messageSender.sendMessage(chatId, "Выберете кэтчер для остановки:\n"+server.activeCatchers());
        String response = readOneMessage();
        JSONObject jsonObject = new JSONObject(response);
        String idCatcherToKill = jsonObject.getJSONArray("result").getJSONObject(0).getJSONObject("message").getString("text");
        int result = server.killCatcherById(Long.parseLong(idCatcherToKill));
        String resultMessage = "";
        if(result==0){
            resultMessage = "Catcher with id " + idCatcherToKill + "killed";
        }
        if(result==1){
            resultMessage = "Catcher with id " + idCatcherToKill + "not killed";
        }
        if(result==2){
            resultMessage = "Catcher with id " + idCatcherToKill + "not found";
        }
        return messageSender.sendMessage(chatId, resultMessage).toString();
    }

    @Override
    public String newCatcher(Long chatId) {
        TicketOptions ticketOptions = new TicketOptions();
        messageSender.sendMessage(chatId, "Введите код станции отправления");
        ticketOptions.setCode0(getTextMessage(readOneMessage()));
        messageSender.sendMessage(chatId, "Введите код станции назначения");
        ticketOptions.setCode1(getTextMessage(readOneMessage()));
        messageSender.sendMessage(chatId, "Введите дату отправления");
        ticketOptions.setDt0(getTextMessage(readOneMessage()));
        messageSender.sendMessage(chatId, "Введите номер поезда");
        ticketOptions.setNumber(getTextMessage(readOneMessage()));
        messageSender.sendMessage(chatId, "Введите тип вагона");
        ticketOptions.setType(getTextMessage(readOneMessage()));
        messageSender.sendMessage(chatId, "Введите максимальную цену билета");
        ticketOptions.setMaxPrice(Long.parseLong(getTextMessage(readOneMessage())));
        return server.newCatcher(ticketOptions, chatId);
    }

    private void stopServer(){
        server.stop();
    }

    private String getTextMessage(String response) {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getJSONArray("result").getJSONObject(0).getJSONObject("message").getString("text");
    }

}
