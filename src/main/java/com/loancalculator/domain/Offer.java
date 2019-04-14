package com.loancalculator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public class Offer implements Comparable<Offer> {
    private String lenderName;
    private Double rate;
    private Double availableFunds;


    @Override
    public int compareTo(Offer offer) {
        return Comparator.comparingDouble(Offer::getRate)
           .thenComparingDouble(Offer::getAvailableFunds)
           .thenComparing(Offer::getLenderName)
                .compare(this, offer);
    }
}
