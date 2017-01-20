package com.example.pavelrukavishnikov.myapplication;

public class Currency {
    public String result;
    public String toType;
    public String fromType;
    public Boolean valid;
    public String fromValue;
    public Currency () {}
    public Currency (String result, String toType, String fromType, String fromValue) {
        this.result = result;
        this.toType = toType;
        this.fromType = fromType;
        this.fromValue = fromValue;
    }
    public Currency (String result, String toType, String fromType, Boolean valid, String fromValue) {
        this.result = result;
        this.toType = toType;
        this.fromType = fromType;
        this.valid = valid;
        this.fromValue = fromValue;
    }
}
