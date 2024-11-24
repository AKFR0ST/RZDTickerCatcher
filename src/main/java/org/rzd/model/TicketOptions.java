package org.rzd.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TicketOptions {
//    private String urlApi;
//    private String layer_id;
    private String code0;
    private String code1;
    private String dt0;
    private String number;
    private String type;
    private Long maxprice;

    public TicketOptions() {

    }


    public TicketOptions(String code0, String code1, String dt0, String number, String type, Long maxprice) {
//        this.urlApi = urlApi;
//        this.layer_id = layer_id;
        this.code0 = code0;
        this.code1 = code1;
        this.dt0 = dt0;
        this.number = number;
        this.type = type;
        this.maxprice = maxprice;
    }

//    public String getUrlApi() {
//        return urlApi;
//    }
//
//    public void setUrlApi(String urlApi) {
//        this.urlApi = urlApi;
//    }

//    public String getLayer_id() {
//        return layer_id;
//    }
//
//    public void setLayer_id(String layer_id) {
//        this.layer_id = layer_id;
//    }

    public String getCode0() {
        return code0;
    }

    public void setCode0(String code0) {
        this.code0 = code0;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getDt0() {
        return dt0;
    }

    public void setDt0(String dt0) {
        this.dt0 = dt0;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(Long maxprice) {
        this.maxprice = maxprice;
    }


}
