package com.ap.homebanking.services;

import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;
import org.springframework.security.core.Authentication;

public interface CardService {

    void createCard(Authentication authentication, CardType cardType, CardColor cardColor) throws Exception;
}
