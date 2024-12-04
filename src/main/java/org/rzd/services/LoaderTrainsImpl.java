package org.rzd.services;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rzd.model.ApplicationOptions;
import org.rzd.model.Car;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component("LoaderTrains")
public class LoaderTrainsImpl implements LoaderTrains {
    ApplicationOptions applicationOptions;
    CloseableHttpClient httpclient;
    BasicCookieStore cookieStore;
    HttpClientResponseHandler<String> responseHandler;

    public LoaderTrainsImpl() {

    }

    @Autowired
    public LoaderTrainsImpl(ApplicationOptions applicationOptions) {
        httpclient = HttpClients.createDefault();
        this.applicationOptions = applicationOptions;
        cookieStore = new BasicCookieStore();
        responseHandler = addResponseHandler();
    }

    @Override
    public List<String> getStationList(String mask) {
        List<String> stationList = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        String encodedMask = URLEncoder.encode(mask, StandardCharsets.UTF_8);
        HttpGet httpGet = new HttpGet("https://ticket.rzd.ru/api/v1/suggests?TransportType=rail&GroupResults=true&RailwaySortPriority=true&SynonymOn=1&Query=" + encodedMask);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        String responseBody = "";
        try {
            responseBody = httpclient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray jsonArray = jsonObject.getJSONArray("city");
        for (int i = 0; i < jsonArray.length(); i++) {
            stationList.add(jsonArray.getJSONObject(i).getString("name") + " " + jsonArray.getJSONObject(i).getString("expressCode"));
        }
        return stationList;
    }

    @Override
    public List<Train> getTrainList(TicketOptions ticketOptions) {
        Long rid;
        synchronized (this) {
            rid = getRid(ticketOptions);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Throw was interrupted");
        }
        List<Train> trainList = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = getHttpGet(rid, ticketOptions);
        JSONObject jsonObject = null;

        try {
            String responseBody = httpclient.execute(httpGet, responseHandler);
            jsonObject = new JSONObject(responseBody);
        } catch (IOException e) {
            System.err.println("Empty response");
        }

        assert jsonObject != null;
        JSONArray trainListJson = jsonObject.getJSONArray("tp").getJSONObject(0).getJSONArray("list");
        for (int i = 0; i < trainListJson.length(); i++) {
            JSONObject obj = trainListJson.getJSONObject(i);
            List<Car> carList = new ArrayList<>();
            obj.getJSONArray("cars");
            for (int j = 0; j < obj.getJSONArray("cars").length(); j++) {
                Long type = obj.getJSONArray("cars").getJSONObject(j).getLong("itype");
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

    public Long getRid(TicketOptions ticketOptions) {
        long rid = 0L;

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = getHttpGet(0L, ticketOptions);
        try {
            String responseBody = httpclient.execute(httpGet, responseHandler);
            JSONObject jsonObject = new JSONObject(responseBody);
            rid = jsonObject.getLong("RID");
        } catch (IOException e) {
            System.err.println("RID not found");
//            throw new RuntimeException(e);

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

    private HttpGet getHttpGet(Long rid, TicketOptions ticketOptions) {
        String getUrl = applicationOptions.getUrlApi()
                + "?layer_id=" + applicationOptions.getLayer_id()
                + "&dir=0"
                + "&tfl=3"
                + "&checkSeats=0"
                + "&code0=" + ticketOptions.getCode0()
                + "&dt0=" + ticketOptions.getDt0()
                + "&code1=" + ticketOptions.getCode1();
        if (rid != 0L) {
            getUrl += "&rid=" + rid;
        }
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        return httpGet;
    }
}