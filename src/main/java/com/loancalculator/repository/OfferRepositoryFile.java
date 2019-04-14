package com.loancalculator.repository;

import com.loancalculator.domain.InvalidLoanException;
import com.loancalculator.domain.Offer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class OfferRepositoryFile implements OfferRepository {

    private Path marketFile;

    public OfferRepositoryFile(Path marketFile) {
        this.marketFile = marketFile;
    }

    @Override
    public Set<Offer> loadOffers() throws Exception {
        Set<Offer> offers = new TreeSet<>();
        List<String> l = null;
        try {
            l = Files.readAllLines(marketFile);
            boolean first = true;
            for (String offerString : l) {
                if (first) {
                    first = false;
                    continue;
                }
                offers.add(OfferParser.parse(offerString));
            }
        } catch (IOException e) {
            throw new InvalidLoanException("Cannot load market data: " + e.toString());
        }
        return offers;
    }

    private static class OfferParser {
        public static Offer parse(String offerString) {
            String[] sections = offerString.split(",");
            return new Offer(sections[0], Double.valueOf(sections[1]), Double.valueOf(sections[2]));
        }
    }
}
