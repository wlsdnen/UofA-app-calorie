package com.example.jojinwoo.learn2caltest.Data;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * Created by jojinwoo on 2018-02-10.
 */

public class Image {

    private static final int INIT_VALUE = -1;

    private int iid;
    private int calorie;

    private int userAnswer;
    private int error;
    private int result;

    public Image(int iid, int calorie) {
        this.iid        = iid;
        this.calorie    = calorie;

        this.userAnswer = INIT_VALUE;
        this.error      = INIT_VALUE;
        this.result     = INIT_VALUE;
    }

    public int getImageId() {
        return this.iid;
    }

    public int getCalorie() {
        return this.calorie;
    }

    public int getError() {
        return this.error;
    }

    public int getResult() {
        return this.result;
    }

    public int getUserAnswer() {
        return this.userAnswer;
    }

    public void setUserAnswer(int userAnswer) { this.userAnswer = userAnswer; }

    public void setResult() {
        int actual = this.calorie;
        int estimate = this.userAnswer;

        int state = -1;
        double tolerance = 0;

        // Accuracy
        if(actual < 100) { tolerance = 0.0005 * pow(actual, 2) + 0.1 * actual + 5; }
        else { tolerance = 0.2 * actual; }

        if ((abs(actual - estimate) > tolerance) && ((actual - estimate) < 0)) { state = 1; }
        else if ((abs(actual - estimate) > tolerance) && ((actual - estimate) > 0)) { state = -1; }
        else { state = 0; }

        this.result = state;
    }

    public void submitAnswer(int estimate) {
        setUserAnswer(estimate);
        setError();
        setResult();
    }
    public void setError() { this.error = -1 * (this.calorie - this.userAnswer); }
}
