package com.loancalculator.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoanTest {

    @Test
    public void calculateMonthlyRepayment() {
        Loan loan = new Loan(1000.0, Loan.DEFAULT_TERM, 0.07);
        assertThat(loan.calculateMonthlyRepayment()).isEqualTo(34.25);
    }

    @Test
    public void calculateTotalRepayment() {
        Loan loan = new Loan(1000.0, Loan.DEFAULT_TERM, 0.07);
        assertThat(loan.calculateTotalRepayment()).isEqualTo(1232.93);
    }

    @Test
    public void getRoundedInterestRate() {
        Loan loan = new Loan(1000.0, Loan.DEFAULT_TERM, 0.07);
        assertThat(loan.getRoundedInterestRate()).isEqualTo(0.1);
    }

    @Test
    public void aggregateWithZeroRate() {
        Loan loan = new Loan(0.0, Loan.DEFAULT_TERM, 0.0);
        Loan aggregate = loan.aggregate(new Loan(1000.0, Loan.DEFAULT_TERM, 0.1));

        assertThat(aggregate.getInterestRate()).isEqualTo(0.1);
    }
    @Test
    public void aggregateWithWeightedAverageRate() {
        Loan loan = new Loan(1000.0, Loan.DEFAULT_TERM, 0.2);
        Loan aggregate = loan.aggregate(new Loan(2000.0, Loan.DEFAULT_TERM, 0.1));

        assertThat(aggregate.getInterestRate()).isEqualTo(0.4 / 3);
    }
}