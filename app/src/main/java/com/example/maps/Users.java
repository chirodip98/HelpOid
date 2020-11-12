package com.example.maps;

public class Users {
    String name,email,number,address,fname,fnumber,cname,cnumber;
    public Users() {

    }

    public String getName() {
        return name;
    }

    public Users(String name, String email, String number, String address, String fname, String fnumber, String cname, String cnumber) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
        this.fname = fname;
        this.fnumber = fnumber;
        this.cname = cname;
        this.cnumber = cnumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnum) {
        this.fnumber = fnum;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCnumber() {
        return cnumber;
    }

    public void setCnumber(String cnum) {
        this.cnumber = cnum;
    }
}

