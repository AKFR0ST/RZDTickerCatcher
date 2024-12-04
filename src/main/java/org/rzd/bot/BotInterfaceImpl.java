package org.rzd.bot;

import org.json.JSONException;
import org.json.JSONObject;
import org.rzd.exceptions.EnterOfDataException;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.rzd.server.CatchersServer;
import org.rzd.services.LoaderTrains;

import java.util.List;

//@Component("BotInterfaceImpl")
public class BotInterfaceImpl implements BotInterface {

    CatchersServer server;
    ApplicationOptions options;
    MessageSender messageSender;
    MessageReceiver messageReceiver;
    LoaderTrains loaderTrains;

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
        } while (!message.equals("/stop"));
    }

    @Override
    public String allCatchers(Long chatId) {
        return messageSender.sendMessage(chatId, server.allCatchers()).toString();

    }

    @Override
    public String activeCatchers(Long chatId) {
        return messageSender.sendMessage(chatId, server.activeCatchers()).toString();
    }

    @Override
    public String killCatcher(Long chatId) {
        messageSender.sendMessage(chatId, "Выберете кэтчер для остановки:\n" + server.activeCatchers());
        String idCatcherToKill = messageReceiver.getTextMessage();
        int result = server.killCatcherById(Long.parseLong(idCatcherToKill));
        String resultMessage = "";
        if (result == 0) {
            resultMessage = "Catcher with id " + idCatcherToKill + "killed";
        }
        if (result == 1) {
            resultMessage = "Catcher with id " + idCatcherToKill + "not found";
        }
        if (result == 2) {
            resultMessage = "Catcher with id " + idCatcherToKill + "not killed";
        }
        return messageSender.sendMessage(chatId, resultMessage).toString();
    }

    private String askStationCode(Long chatId, boolean isDepartStation) {

        if (isDepartStation) {
            messageSender.sendMessage(chatId, "Введите станцию отправления");
        } else {
            messageSender.sendMessage(chatId, "Введите станцию назначения");
        }
        List<String> stationList = loaderTrains.getStationList(messageReceiver.getTextMessage());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stationList.size(); i++) {
            stringBuilder.append(i + 1);
            stringBuilder.append("\t");
            stringBuilder.append(stationList.get(i));
            stringBuilder.append("\n");
        }
        if (isDepartStation) {
            messageSender.sendMessage(chatId, "Выберете станцию отправления\n" + stringBuilder);
        } else {
            messageSender.sendMessage(chatId, "Выберете станцию назначения\n" + stringBuilder);
        }
        try
        {
            String train = stationList.get(Integer.parseInt(messageReceiver.getTextMessage()) - 1);
            return train.substring(train.lastIndexOf(" ") + 1);
        }
        catch (NumberFormatException e)
        {
            messageSender.sendMessage(chatId, "Некорректный номер станции\n");
            return null;
        }

    }

    private String askTrainsNumber(Long chatId, TicketOptions ticketOptions) {
        List<Train> trainList = loaderTrains.getTrainList(ticketOptions);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < trainList.size(); i++) {
            stringBuilder.append(i + 1);
            stringBuilder.append("\t");
            stringBuilder.append(trainList.get(i));
        }
        messageSender.sendMessage(chatId, "Выберете поезд или введите его номер: \n" + stringBuilder);
        String trainsNumber = messageReceiver.getTextMessage();
        if (trainsNumber.length() < 2) {
            trainsNumber = trainList.get(Integer.parseInt(trainsNumber) - 1).getNumber();
        }
        return trainsNumber;
    }


    @Override
    public Long newCatcher(Long chatId) {
        Long catcherId = 0L;
        try
        {
            TicketOptions ticketOptions = new TicketOptions();
            String c0 = askStationCode(chatId, true);
            if (c0 == null) {
                throw new EnterOfDataException("Город отправления не найден");
            }
            ticketOptions.setCode0(c0);

            String c1 = askStationCode(chatId, false);
            if (c1 == null) {
                throw new EnterOfDataException("Город прибытия не найден");
            }
            ticketOptions.setCode1(c1);


            messageSender.sendMessage(chatId, "Введите дату отправления");
            ticketOptions.setDt0(messageReceiver.getTextMessage());
            ticketOptions.setNumber(askTrainsNumber(chatId, ticketOptions));
            messageSender.sendMessage(chatId, "Введите тип вагона\n 1 - Плац \n 2 - Общ \n 3 - Сид \n 4 - Купе \n 5 - Мяг \n 6 - Люкс");
            ticketOptions.setType(Long.parseLong(messageReceiver.getTextMessage()));
            if(ticketOptions.getType()<1||ticketOptions.getType()>6) {
                throw new EnterOfDataException("Данная категория не существует");
            }
            messageSender.sendMessage(chatId, "Введите максимальную цену билета");
            try{
                ticketOptions.setMaxPrice(Long.parseLong(messageReceiver.getTextMessage()));
            }
            catch (NumberFormatException e){
                throw new EnterOfDataException("Некорректное значение");
            }
            catcherId = server.newCatcher(ticketOptions, chatId);
            if (catcherId < 0) {
                messageSender.sendMessage(chatId, "adding catcher failed");
            } else {
                messageSender.sendMessage(chatId, "adding catcher with id " + catcherId + " successfully");
            }
        }
        catch (JSONException e){
            messageSender.sendMessage(chatId, "Ошибка обработки запроса\nadding catcher failed\"");
        }
        catch (EnterOfDataException | IndexOutOfBoundsException e){
            messageSender.sendMessage(chatId, e.getMessage()+"\nadding catcher failed");
        }
        return catcherId;
    }

    private void stopServer() {
        server.stop();
    }


}
