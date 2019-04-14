package com.loancalculator;

import com.loancalculator.domain.Loan;
import com.loancalculator.repository.OfferRepositoryFile;
import com.loancalculator.service.LoanCalculatorService;

import java.nio.file.Path;

public class LoanCalculator {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: LoanCalculator.jar [market_file] [loan_amount]");
            System.exit(1);
        }

        try {
            LoanCalculatorService loanCalculatorService = new LoanCalculatorService(new OfferRepositoryFile(Path.of(args[0])));
            Loan loan = loanCalculatorService.calculateLoan(Double.valueOf(args[1]));
            System.out.println("Requested amount: " + loan.getPrinciple());
            System.out.println("Rate: " + loan.getRoundedInterestRate());
            System.out.println("Monthly repayment: " + loan.calculateMonthlyRepayment());
            System.out.println("Total repayment: " + loan.calculateTotalRepayment());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
