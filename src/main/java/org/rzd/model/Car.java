package org.rzd.model;

public class Car {
    private String type;
    private Long freeSeats;
    private Long tariff;

    public Car(String type, Long freeSeats, Long tariff) {
        this.type = type;
        this.freeSeats = freeSeats;
        this.tariff = tariff;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(Long freeSeats) {
        this.freeSeats = freeSeats;
    }

    public Long getTariff() {
        return tariff;
    }

    public void setTariff(Long tariff) {
        this.tariff = tariff;
    }

    @Override
    public String toString(){
        return type + " " + freeSeats + " " + tariff;
    }
}
