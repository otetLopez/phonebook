package com.f20.phonebook;

public class Contact {
    private int id;
    private String fname;
    private String lname;
    private int phone;
    private String address;

    public Contact() {
        this.id = -1;
        this.fname = "";
        this.lname = "";
        this.phone = 0;
        this.address = "";
    }


    public Contact(int id, String fname, String lname, int phone, String address) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
    }

    public Contact(String fname, String lname, int phone, String address) {
        this.id = -1;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
