package com.example.yourroom;

public class User {
    private String id, email, pass, phone, name, person_name, age, sex;

    public User() {
    }

    public User(String email, String pass, String phone, String name, String person_name, String age, String sex) {
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.name = name;
        this.person_name = person_name;
        this.age = age;
        this.sex = sex;
    }

    public String getIdPerson() {
        return id;
    }

    public void setIdPerson(String id) {
        this.id = id;
    }

    public String getuName() {
        return name;
    }

    public void setuName(String name) {
        this.name = name;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
