package org.rzd.core;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rzd.model.Car;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("LoaderTrains")
//  Loader загружает список поездов на нужную дату
public class LoaderTrains {

    ApplicationContext context;
    CloseableHttpClient httpclient;
    BasicCookieStore cookieStore;
    TicketOptions ticketOptions;
    HttpClientResponseHandler<String> responseHandler;

    public LoaderTrains() {

    }

    @Autowired
    public LoaderTrains(ApplicationContext applicationContext) {
        httpclient = HttpClients.createDefault();
        context = applicationContext;
        ticketOptions = context.getBean("getTicketOptions", TicketOptions.class);
        cookieStore = new BasicCookieStore();
        responseHandler = addResponseHandler();
    }

    public void LoadContext(ApplicationContext applicationContext) {
        httpclient = HttpClients.createDefault();
        context = applicationContext;
        ticketOptions = context.getBean("getTicketOptions", TicketOptions.class);
        cookieStore = new BasicCookieStore();
    }


    public List<Train> getTrainList()  {
        Long rid = getRid();
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException e){
            System.err.println("Interrupted Exception");
        }
        List<Train> trainList = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = getHttpGet(rid);
        JSONObject jsonObject = null;

        try {
            String responseBody = httpclient.execute(httpGet, responseHandler);
            jsonObject = new JSONObject(responseBody);
        } catch (Exception e) {
            System.err.println("Error while reading response");
        }

        assert jsonObject != null;
        JSONArray trainListJson = jsonObject.getJSONArray("tp").getJSONObject(0).getJSONArray("list");
        for (int i = 0; i < trainListJson.length(); i++) {
            JSONObject obj = trainListJson.getJSONObject(i);
            List<Car> carList = new ArrayList<>();
            obj.getJSONArray("cars");
            for (int j = 0; j < obj.getJSONArray("cars").length(); j++) {
                String type = obj.getJSONArray("cars").getJSONObject(j).getString("type");
                Long freeSeats = obj.getJSONArray("cars").getJSONObject(j).getLong("freeSeats");
                Long tariff = obj.getJSONArray("cars").getJSONObject(j).getLong("tariff");
                Car car = new Car(type, freeSeats, tariff);
                carList.add(car);
            }
            String time0 = obj.getString("time0");
            String time1 = obj.getString("time1");
            String number = obj.getString("number");
            trainList.add(new Train(number, time0, time1, carList));
        }
        return trainList;
    }

    public Long getRid() {
        long rid = 0L;

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = getHttpGet(0L);
        try {
            String responseBody = httpclient.execute(httpGet, responseHandler);
            JSONObject jsonObject = new JSONObject(responseBody);
            rid = jsonObject.getLong("RID");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rid;
    }

    private HttpClientResponseHandler<String> addResponseHandler() {
        return classicHttpResponse -> {
            int statusCode = classicHttpResponse.getCode();
            if (statusCode >= 300) {
                throw new IOException("Unexpected response status: " + statusCode);
            }
            HttpEntity entity = classicHttpResponse.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        };
    }

    private HttpGet getHttpGet(Long rid) {
        String getUrl = ticketOptions.getUrlApi()
                + "?layer_id=" + ticketOptions.getLayer_id()
                + "&dir=0"
                + "&tfl=3"
                + "&checkSeats=0"
                + "&code0=" + ticketOptions.getCode0()
                + "&dt0=" + ticketOptions.getDt0()
                + "&code1=" + ticketOptions.getCode1();
        if (rid != 0L) {
            getUrl += "&rid=" + rid.toString();
        }
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        return httpGet;
    }
}
