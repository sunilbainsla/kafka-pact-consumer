package com.sunil.bainsla.kafkapactconsumer;

import lombok.Data;

import java.util.Date;

@Data
public class Payment {
    String id;
    String data;
    String paymentDate;
    int amount;
}
