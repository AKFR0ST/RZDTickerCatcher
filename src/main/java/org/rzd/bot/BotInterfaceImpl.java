package org.rzd.bot;

import org.json.JSONObject;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.rzd.server.CatchersServer;
import org.rzd.services.LoaderTrains;
import org.springframework.context.ApplicationContext;

import java.util.List;

//@Component ("BotInterfaceImpl")
public class BotInterfaceImpl implements BotInterface {

    ApplicationContext applicationContext;
    CatchersServer server;
    ApplicationOptions options;
    MessageSender messageSender;
    MessageReceiver messageReceiver;
    LoaderTrains loaderTrains;

    public BotInterfaceImpl() {

    }

//    @Autowired
    public BotInterfaceImpl(CatchersServer server, ApplicationOptions options, MessageSender messageSender, MessageReceiver messageReceiver, LoaderTrains loaderTrains) {
        this.server = server;
        this.options = options;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        this.loaderTrains = loaderTrains;
    }

    @Override
    public void start() {
        String message = "";
        do {
            String response = messageReceiver.messageReceive();
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
        String response = messageReceiver.messageReceive();
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

    private String askStationCode(Long chatId, boolean isDepartStation){

        if(isDepartStation){
            messageSender.sendMessage(chatId, "Введите станцию отправления");
        }
        else
        {
            messageSender.sendMessage(chatId, "Введите станцию назначения");
        }
        List<String> stationList = loaderTrains.getStationList(getTextMessage(messageReceiver.messageReceive()));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stationList.size(); i++) {
            stringBuilder.append(i+1);
            stringBuilder.append("\t");
            stringBuilder.append(stationList.get(i));
            stringBuilder.append("\n");
        }
        if(isDepartStation){
            messageSender.sendMessage(chatId, "Выберете станцию отправления\n"+stringBuilder.toString());
        }
        else {
            messageSender.sendMessage(chatId, "Выберете станцию назначения\n"+stringBuilder.toString());
        }
        String train = stationList.get(Integer.parseInt(getTextMessage(messageReceiver.messageReceive()))-1);
        return train.substring(train.lastIndexOf(" ")+1);
    }

    private String askTrainsNumber(Long chatId, TicketOptions ticketOptions){
        List<Train> trainList = loaderTrains.getTrainList(ticketOptions);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < trainList.size(); i++) {
            stringBuilder.append(i+1);
            stringBuilder.append("\t");
            stringBuilder.append(trainList.get(i));
        }
        messageSender.sendMessage(chatId, "Выберете поезд или введите его номер: \n"+stringBuilder.toString());
        String trainsNumber = getTextMessage(messageReceiver.messageReceive());
        if(trainsNumber.length()<2){
            trainsNumber = trainList.get(Integer.parseInt(trainsNumber)-1).getNumber();
        }
        return trainsNumber;
    }



    @Override
    public Long newCatcher(Long chatId) {
        TicketOptions ticketOptions = new TicketOptions();
        ticketOptions.setCode0(askStationCode(chatId, true));
        ticketOptions.setCode1(askStationCode(chatId, false));
        messageSender.sendMessage(chatId, "Введите дату отправления");
        ticketOptions.setDt0(getTextMessage(messageReceiver.messageReceive()));
        ticketOptions.setNumber(askTrainsNumber(chatId, ticketOptions));
        messageSender.sendMessage(chatId, "Введите тип вагона\n 1 - Плац \n 2 - Общ \n 3 - Сид \n 4 - Купе \n 5 - Мяг \n 6 - Люкс");
        ticketOptions.setType(Long.parseLong(getTextMessage(messageReceiver.messageReceive())));
        messageSender.sendMessage(chatId, "Введите максимальную цену билета");
        ticketOptions.setMaxPrice(Long.parseLong(getTextMessage(messageReceiver.messageReceive())));
        Long catcherId = server.newCatcher(ticketOptions, chatId);
        if( catcherId<0 ){
            messageSender.sendMessage(chatId, "adding catcher failed");
        }
        else
        {
            messageSender.sendMessage(chatId, "adding catcher with id "+catcherId+" successfully");
        }
        return catcherId;
    }

    private void stopServer(){
        server.stop();
    }

    private String getTextMessage(String response) {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getJSONArray("result").getJSONObject(0).getJSONObject("message").getString("text");
    }

}
