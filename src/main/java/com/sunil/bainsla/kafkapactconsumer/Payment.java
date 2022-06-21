package com.sunil.bainsla.kafkapactconsumer;

import lombok.Data;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import javax.annotation.MatchesPattern;
import java.util.Date;

@Data
public class Payment {
    String id;
    String data;
    String paymentDate;
    int amount;
    String name;
    String accountNumber;
}
