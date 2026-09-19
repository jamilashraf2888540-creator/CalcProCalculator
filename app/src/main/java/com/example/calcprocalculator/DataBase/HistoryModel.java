package com.example.calcprocalculator.DataBase;

public class HistoryModel {

    String expression;
    String result;
    String time;

    public HistoryModel(String expression, String result, String time) {
        this.expression = expression;
        this.result = result;
        this.time = time;
    }
}
