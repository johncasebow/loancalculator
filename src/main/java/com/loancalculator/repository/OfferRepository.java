package com.loancalculator.repository;

import com.loancalculator.domain.Offer;

import java.util.Set;

public interface OfferRepository {
    Set<Offer> loadOffers() throws Exception;
}
