package org.rzd.bot;

import org.rzd.model.ApplicationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("MessageSenderImpl")
public class MessageSenderImpl implements MessageSender {
    ApplicationOptions options;

    @Autowired
    MessageSenderImpl(ApplicationContext applicationContext) {
        options = applicationContext.getBean(ApplicationOptions.class);
    }

    @Override
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
