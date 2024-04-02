package org.example.tentrilliondollars.kakaopay;

import lombok.Getter;

@Getter
public class Amount {
    private int total;
    private int tax_free;
    private int tax;
}