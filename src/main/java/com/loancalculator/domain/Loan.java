package com.loancalculator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@AllArgsConstructor
public class Loan {

    public static final int DEFAULT_TERM = 36;

    private Double principle;
    private Integer termInMonths;
    private Double interestRate;

    public Double calculateMonthlyRepayment() {
        return round(calculateTotalRepayment() / termInMonths, 2);
    }

    public Double calculateTotalRepayment() {
        return round(principle * Math.pow(1 + (0.07 / 12), termInMonths), 2);
    }

    public Double getRoundedInterestRate() {
        return round( interestRate, 1);
    }

    private Double round(double value, int places) {
        return new BigDecimal(value).setScale(places, RoundingMode.CEILING).doubleValue();
    }

    /*
     * Builds an aggregated Loan from two instances, with a principle equal to the sum of the two loans,
     * and a rate which is a weighted average of the two loan rates.
     */
    public Loan aggregate(Loan loan) {
        // Not sure if a weighted average is appropriate here, but it seemed the best to me
        double rate = interestRate == 0 ? loan.interestRate : calculateWeightedAverageRate(interestRate, principle, loan.interestRate, loan.principle);
        return new Loan(principle + loan.principle, termInMonths, rate);
    }

    private Double calculateWeightedAverageRate(Double rate1, Double weight1, Double rate2, Double weight2) {
        return ((rate1 * weight1) + (rate2 * weight2)) / (weight1 + weight2);
    }
}
