package org.rzd.model;

import java.util.List;

public class Train {
   private String number;
   private String time0;
   private String time1;
   private List<Car> carList;

   public Train(String number, String time0, String time1, List<Car> carList) {
       this.number = number;
       this.time0 = time0;
       this.time1 = time1;
       this.carList = carList;
   }

   public String getNumber() {
       return number;
   }

   public void setNumber(String number) {
       this.number = number;
   }

   public String getTime0() {
       return time0;
   }

   public void setTime0(String time0) {
       this.time0 = time0;
   }

   public String getTime1() {
       return time1;
   }

   public void setTime1(String time1) {
       this.time1 = time1;
   }

   public List<Car> getCarList() {
       return carList;
   }

   public void setCarList(List<Car> carList) {
       this.carList = carList;
   }

   @Override
    public String toString() {
       return number + "\t" + time0 + "\t" +  time1 + "\t" + carList + "\n";
   }

}
