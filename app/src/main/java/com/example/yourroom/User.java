package com.example.yourroom;

public class User {
    private String id, name, sec_name, age, sex, email, phone;

    public User() {
    }

    public User(String id, String name, String sec_name, String age, String sex, String email, String phone) {
        this.id = id;
        this.name = name;
        this.sec_name = sec_name;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
    }

    public String getIdPerson() {
        return id;
    }

    public void setIdPerson(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSec_name() {
        return sec_name;
    }

    public void setSec_name(String sec_name) {
        this.sec_name = sec_name;
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
}
