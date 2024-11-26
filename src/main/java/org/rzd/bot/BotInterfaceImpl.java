package org.rzd.bot;

import org.json.JSONObject;
import org.rzd.model.ApplicationOptions;
import org.rzd.server.CatchersServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.lang.Thread.sleep;

@Component ("BotApiImpl")
public class BotInterfaceImpl implements BotInterface {

    ApplicationContext applicationContext;
    CatchersServerImpl server;
    ApplicationOptions options;
    public Long offset;

    public BotInterfaceImpl() {
//        server =



    }

    @Autowired
    public BotInterfaceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        server = applicationContext.getBean(CatchersServerImpl.class);
        options = applicationContext.getBean(ApplicationOptions.class);
        offset = 0L;
//        lastUpdateId = getLastUpdateIdFromBot();
//        System.out.println("lastUpdateId: " + lastUpdateId);

    }

    public void start(){
        while (true){
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.telegram.org/bot6820154944:AAFxfz8Wb3QOnYNCCqV7jpo0pkoeG0--3uE/getUpdates?limit=1&offset=" + offset;
            String response = restTemplate.getForEntity(url, String.class).getBody();
            assert response != null;
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("ok")&&!jsonObject.getJSONArray("result").isEmpty()){
                offset = jsonObject.getJSONArray("result").getJSONObject(0).getLong("update_id")+1;
                Long chatId = jsonObject.
                        getJSONArray("result").
                        getJSONObject(0).
                        getJSONObject("message").
                        getJSONObject("chat").
                        getLong("id");

                String  message = jsonObject.
                        getJSONArray("result").
                        getJSONObject(0).
                        getJSONObject("message").
                        getString("text");

                switch (message) {
                    case ("/all"): allCatchers(chatId); break;
                }
            }

//            https://api.telegram.org/bot6820154944:AAFxfz8Wb3QOnYNCCqV7jpo0pkoeG0--3uE/getUpdates?offset=75
            //  В цикле запрашиваем сообщеньки
            //  Если есть новая то
            //  Счетчик последней прочитанной++
            //  Запускаем обработчик команды
            //

        }
    }

    private Long getLastUpdateIdFromBot(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.telegram.org/bot6820154944:AAFxfz8Wb3QOnYNCCqV7jpo0pkoeG0--3uE/getUpdates?limit=1";
        String response = restTemplate.getForEntity(url, String.class).getBody();
        assert response != null;
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getJSONArray("result").getJSONObject(0).getLong("update_id");
//        System.out.println(jsonObject.getBoolean("ok"));
//        int len = jsonObject.getJSONArray("result").length();
//        return jsonObject.getJSONArray("result").getJSONObject(len-1).getJSONObject("message").getLong("message_id");
    }



    @Override
    public void allCatchers(Long chatId) {
        sendMessage(chatId, server.allCatchers());
    }

    @Override
    public void killCatcher() {
        //  (лонг) - успех/фэйл
    }

    @Override
    public void newCatcher() {
        //  (5стр+лонг) - запущен/нет
    }

    public void sendMessage(Long chatId, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.telegram.org/bot"+
                options.getBotId()+
                ":"+
                options.getApiKey()+
                "/sendMessage?chat_id="+
                chatId+
                "&text="+
                message;
        restTemplate.getForEntity(url, String.class);
    }
}
