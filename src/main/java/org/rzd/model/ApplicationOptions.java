package org.rzd.model;

public class ApplicationOptions {
    private String urlApi;
    private String layer_id;
    private Long timeout;

    public ApplicationOptions(String urlApi, String layer_id, Long timeout){
        this.urlApi = urlApi;
        this.layer_id = layer_id;
        this.timeout = timeout;
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

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

}
