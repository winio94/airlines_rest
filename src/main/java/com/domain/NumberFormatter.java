package com.domain;

public class NumberFormatter {

    enum Rome {
        M(1000), D(500), C(100), L(50), X(10), V(5), I(1);

        private int value;

        Rome(int value) {
            this.value = value;
        }

    }

    public String format(int input) {
        if (input < 1 || input > 3999) {
            throw new RuntimeException();
        }
        return "";
    }

    public String formatNumber(int input) {
        Rome[] values = Rome.values();
        for(Rome r: values) {
            if(r <= input) {

            }
        }
        return "";
    }

}
