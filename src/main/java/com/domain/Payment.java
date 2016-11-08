package com.domain;

/**
 * Created by Michał on 2016-11-07.
 */
public enum Payment {
    BANK_TRANSFER("Bank Transfer"), CASH("Cash"), PAY_PAL("Pay Pal");

    private String value;

    Payment(String value) {
        this.value = value;
    }
}
