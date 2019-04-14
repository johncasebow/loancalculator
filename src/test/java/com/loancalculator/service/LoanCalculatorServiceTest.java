package com.loancalculator.service;

import com.loancalculator.domain.InvalidLoanException;
import com.loancalculator.domain.Loan;
import com.loancalculator.domain.Offer;
import com.loancalculator.repository.OfferRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LoanCalculatorServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Test
    public void calculate_loan_from_single_lender() throws Exception {
        Mockito.when(offerRepository.loadOffers()).thenReturn(Set.of(new Offer("Joe", 0.07, 1000.0)));
        LoanCalculatorService loanCalculatorService = new LoanCalculatorService(offerRepository);

        Loan loan = loanCalculatorService.calculateLoan(100.0);

        assertThat(loan).isEqualToComparingFieldByField(new Loan (100.0, Loan.DEFAULT_TERM, 0.07));
    }

    @Test
    public void calculate_loan_from_multiple_lenders() throws Exception {
        Mockito.when(offerRepository.loadOffers()).thenReturn(
                new TreeSet<>(Set.of(
                        new Offer("Joe", 0.07, 1000.0),
                        new Offer("Bob", 0.08, 1000.0))));
        LoanCalculatorService loanCalculatorService = new LoanCalculatorService(offerRepository);

        Loan loan = loanCalculatorService.calculateLoan(1500.0);

        assertThat(loan).isEqualToComparingFieldByField(new Loan (1500.0, Loan.DEFAULT_TERM, 0.22 / 3.0));
    }

    @Test
    public void loan_has_best_rate() throws Exception {
        Mockito.when(offerRepository.loadOffers()).thenReturn(
                new TreeSet(Set.of(
                        new Offer("Joe", 0.07, 1000.0),
                        new Offer("Bob", 0.06, 1000.0))));
        LoanCalculatorService loanCalculatorService = new LoanCalculatorService(offerRepository);

        Loan loan = loanCalculatorService.calculateLoan(100.0);

        assertThat(loan).isEqualToComparingFieldByField(new Loan (100.0, Loan.DEFAULT_TERM, 0.06));
    }

    @Test(expected = InvalidLoanException.class)
    public void loan_exceeds_market_value() throws Exception {
        Mockito.when(offerRepository.loadOffers()).thenReturn(
                new TreeSet<>(Set.of(
                        new Offer("Bob", 0.08, 1000.0))));
        LoanCalculatorService loanCalculatorService = new LoanCalculatorService(offerRepository);
        loanCalculatorService.addOffer(new Offer("Joe", 0.07, 1000.0));

        Loan loan = loanCalculatorService.calculateLoan(10000.0);
    }

    @Test(expected = InvalidLoanException.class)
    public void loan_not_valid_increment() throws Exception {
        LoanCalculatorService loanCalculatorService = new LoanCalculatorService(offerRepository);

        Loan loan = loanCalculatorService.calculateLoan(99.0);
    }

}