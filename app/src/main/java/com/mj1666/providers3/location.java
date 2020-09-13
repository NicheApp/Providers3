package com.mj1666.providers3;

public class location {
   public String name;
  public   String  latitude,longitude;
   public String PhoneNo;
   public String address,occupation;
   public int jobs;


    public  location(){}
     public location(String name,String latitude,String longitude,String PhoneNo,String address,String occupation,int jobs){
this.name = name;
this.latitude = latitude;
this.longitude = longitude;
this.PhoneNo = PhoneNo;
this.address = address;
this.occupation = occupation;
this.jobs=jobs;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getAdress() {
        return address;
    }

    public int getJobs() {
        return jobs;
    }

    public void setJobs(int jobs) {
        this.jobs = jobs;
    }

    public void setAdharno(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
