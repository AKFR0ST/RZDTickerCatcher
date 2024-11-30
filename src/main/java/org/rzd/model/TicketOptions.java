package org.rzd.model;

import org.springframework.stereotype.Component;

@Component
public class TicketOptions {
    private String code0;
    private String code1;
    private String dt0;
    private String number;
    private Long type;
    private Long maxPrice;

    public TicketOptions() {

    }


    public TicketOptions(String code0, String code1, String dt0, String number, Long type, Long maxPrice) {
        this.code0 = code0;
        this.code1 = code1;
        this.dt0 = dt0;
        this.number = number;
        this.type = type;
        this.maxPrice = maxPrice;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return dt0 +
                "\t" +
                number +
                "\t" +
                code0 +
                "\t->\t" +
                code1 +
                "\t" +
                type.toString() +
                "\n";
    }
}
