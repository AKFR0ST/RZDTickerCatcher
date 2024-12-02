package org.rzd.bot;

import org.json.JSONObject;
import org.rzd.model.ApplicationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("MessageReceiverImpl")
public class MessageReceiverImpl implements MessageReceiver {
    Long offset;
    ApplicationOptions  options;

    @Autowired
    public MessageReceiverImpl(ApplicationOptions applicationOptions) {
        offset = 0L;
        this.options = applicationOptions;
    }

    @Override
    public String messageReceive() {
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

    public String getTextMessage() {
        JSONObject jsonObject = new JSONObject(messageReceive());
        return jsonObject.getJSONArray("result").getJSONObject(0).getJSONObject("message").getString("text");
    }
}
