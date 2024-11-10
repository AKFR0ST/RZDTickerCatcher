package org.rzd.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rzd.model.Car;
import org.rzd.model.TicketOptions;
import org.rzd.model.Train;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component("TicketLoader")
//  Loader загружает список поездов на нужную дату
public class LoaderTrains {
//    private String urlLink;
    ApplicationContext context;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    BasicCookieStore cookieStore;
    TicketOptions ticketOptions;
    private Long rid;
    //  Линк для запроса здесь лежать будет
    //  Съедает конфиг со станциями, датой и


    public LoaderTrains() {

    }

    public LoaderTrains(ApplicationContext applicationContext){
        context = applicationContext;
        ticketOptions = context.getBean("getTicketOptions", TicketOptions.class);
        cookieStore = new BasicCookieStore();
    }

    public void LoadContext(ApplicationContext applicationContext){
        context = applicationContext;
        ticketOptions = context.getBean("getTicketOptions", TicketOptions.class);
    }



    public List<Train> getTrainList() throws IOException, InterruptedException, JSONException {
        rid = getRid();
        Thread.sleep(1000);
        List<Train> trainList = new ArrayList<>();
//        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = getHttpGet(rid);
        JSONObject jsonObject = null;

        HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {
            @Override
            public String handleResponse(ClassicHttpResponse classicHttpResponse) throws HttpException, IOException {
                int statusCode = classicHttpResponse.getCode();
                if (statusCode >= 300) {
                    throw new IOException("Unexpected response status: " + statusCode);
                }
                HttpEntity entity = classicHttpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
        };

        try {
            String responseBody = httpclient.execute(httpGet, responseHandler);
            jsonObject = new JSONObject(responseBody);
//            System.out.println(responseBody);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
//        assert jsonObject != null;
//        jsonObject.
        assert jsonObject != null;
        JSONArray trainListJson = jsonObject.getJSONArray("tp").getJSONObject(0).getJSONArray("list");
        for (int i = 0; i < trainListJson.length(); i++) {
            JSONObject obj = trainListJson.getJSONObject(i);
            List<Car> carList = new ArrayList<Car>();
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

//        System.out.println("\n>>>"+jsonObject.getJSONArray("tp").getJSONObject(0).getJSONArray("list").toString());
//        List<Train> listCar = objectMapper.readValue(jsonObject.getJSONArray("tp"), new TypeReference<List<Train>>(){});

        return trainList;


    }


    //  TODO добавить обработку исключения по ошибке url связанным с параметрами
    public Long getRid() throws IOException {
        Long rid = 0L;

//        CloseableHttpClient httpclient = HttpClients.createDefault();
      CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = getHttpGet(0L);

        HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {
            @Override
            public String handleResponse(ClassicHttpResponse classicHttpResponse) throws HttpException, IOException {
                int statusCode = classicHttpResponse.getCode();
                if (statusCode >= 300) {
                    throw new IOException("Unexpected response status: " + statusCode);
                }
                HttpEntity entity = classicHttpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
        };

        try {
            String responseBody = httpclient.execute(httpGet, responseHandler);
//            System.out.println(responseBody);
            JSONObject jsonObject = new JSONObject(responseBody);
            rid = jsonObject.getLong("RID");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//    System.out.println(rid);
    return rid;
    }

    private HttpGet getHttpGet(Long rid) {
        String getUrl = ticketOptions.getUrlApi() //"https://pass.rzd.ru/timetable/public/ru?layer_id=5827&dir=0&tfl=3&checkSeats=1"
                +"?layer_id="+ticketOptions.getLayer_id()
                +"&dir=0"
                +"&tfl=3"
                +"&checkSeats=0"
                +"&code0="+ticketOptions.getCode0()
                +"&dt0="+ticketOptions.getDt0()
                +"&code1="+ticketOptions.getCode1();
        if(rid!=0L){
            getUrl += "&rid="+rid.toString();
//            getUrl += "&rid=36401423436";
        }
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        return httpGet;
    }
}
