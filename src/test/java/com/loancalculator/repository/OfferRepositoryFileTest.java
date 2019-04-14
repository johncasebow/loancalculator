package com.loancalculator.repository;

import com.loancalculator.domain.InvalidLoanException;
import com.loancalculator.domain.Offer;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class OfferRepositoryFileTest {

    @Test
    public void loadOffers() throws Exception {
        OfferRepository offerRepository = new OfferRepositoryFile(Path.of("src/test/resources/market.csv"));
        Set<Offer> offers = offerRepository.loadOffers();
        assertThat(offers.size()).isEqualTo(7);
    }

    @Test(expected = InvalidLoanException.class)
    public void loadNonExistentOffers() throws Exception {
        OfferRepository offerRepository = new OfferRepositoryFile(Path.of("src/test/resources/foo.csv"));
        Set<Offer> offers = offerRepository.loadOffers();
    }
}