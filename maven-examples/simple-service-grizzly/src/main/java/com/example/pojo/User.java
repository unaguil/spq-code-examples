package com.example.pojo;

public class User {

    private int code;
    private String name;
    private String surname;

    // Default public constructor required for serialization
    public User() {
    }

    public User(int code) {
        this.code = code;
    }

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public User(int code, String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return String.format("%d - %s %s", code, name, surname);
    }
}
