package com.loancalculator.service;

import com.loancalculator.domain.InvalidLoanException;
import com.loancalculator.domain.Loan;
import com.loancalculator.domain.Offer;
import com.loancalculator.repository.OfferRepository;

import java.util.ArrayList;
import java.util.Set;

public class LoanCalculatorService {

    private Set<Offer> offers;

    public LoanCalculatorService(OfferRepository offerRepository) throws Exception {
        offers = offerRepository.loadOffers();
    }

    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    public Loan calculateLoan(double principle) throws Exception {
        verifyPrinciple(principle);
        ArrayList<Loan> acceptedLoans = new ArrayList<>();
        double remaining = principle;
        for (Offer offer: offers) {
            if (remaining <= 0)
                break;

            if (offer.getAvailableFunds() > principle) {
                acceptedLoans.add(new Loan(principle, 36, offer.getRate()));
                break;
            } else {
                double amount = offer.getAvailableFunds() > remaining ? remaining : offer.getAvailableFunds();
                acceptedLoans.add(new Loan( amount, 36, offer.getRate()));
                remaining -= amount;
            }
        }
        return aggregate(acceptedLoans);
    }

    private Loan aggregate(ArrayList<Loan> acceptedLoans) {
        Loan aggregateLoan = new Loan(0.0, 36, 0.0);
        for (Loan loan : acceptedLoans) {
            aggregateLoan = aggregateLoan.aggregate(loan);
        }
        return aggregateLoan;
    }

    private void verifyPrinciple(double principle) throws Exception {
        if (principle % 100 != 0) {
            throw new InvalidLoanException("The requested principle is not a multiple of £100");
        }
        if (principle > offers.stream().mapToDouble(offer -> offer.getAvailableFunds()).sum()) {
            throw new InvalidLoanException("Market does not have enough value, it is not possible to provide a quote at this time.");
        }
    }
}
