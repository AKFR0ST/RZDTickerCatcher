package org.rzd.model;

import org.springframework.beans.factory.annotation.Value;

public class TicketOptions {
    private String urlApi;
    private String layer_id;
    private String code0;
    private String code1;
    private String dt0;

    public TicketOptions() {

    }

    public TicketOptions(String urlApi, String layer_id, String code0, String code1, String dt0) {
        this.urlApi = urlApi;
        this.layer_id = layer_id;
        this.code0 = code0;
        this.code1 = code1;
        this.dt0 = dt0;
    }

    public String getUrlApi() {
        return urlApi;
    }

    public void setUrlApi(String urlApi) {
        this.urlApi = urlApi;
    }

    public String getLayer_id() {
        return layer_id;
    }

    public void setLayer_id(String layer_id) {
        this.layer_id = layer_id;
    }

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


}
