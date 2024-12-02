package org.rzd.model;

public class Car {
    private Long type;
    private Long freeSeats;
    private Long tariff;

    public Car(Long type, Long freeSeats, Long tariff) {
        this.type = type;
        this.freeSeats = freeSeats;
        this.tariff = tariff;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
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
    public String toString() {
        int itype = Math.toIntExact(type);
        String stringType = switch (itype) {
            case 1 -> "Плац";
            case 2 -> "Общ";
            case 3 -> "Сид";
            case 4 -> "Купе";
            case 5 -> "Мяг";
            case 6 -> "Люкс";
            default -> "";
        };
        return stringType + " " + freeSeats + " " + tariff + "р";
    }
}
