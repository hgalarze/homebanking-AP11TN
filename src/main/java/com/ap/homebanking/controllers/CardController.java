package com.ap.homebanking.controllers;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
public class CardController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {
        int MAX_CARDS_BY_CLIENT = 3;
        Client currentClient = clientRepository.findByEmail(authentication.getName()).orElse(null);

        if (currentClient != null) {
            if (currentClient.getCards().size() < MAX_CARDS_BY_CLIENT) {
                Random random = new Random(System.currentTimeMillis());
                int cvv = 100 + random.nextInt(900);
                int cardNumber1 = 1000 + random.nextInt(9000);
                int cardNumber2 = 1000 + random.nextInt(9000);
                int cardNumber3 = 1000 + random.nextInt(9000);
                int cardNumber4 = 1000 + random.nextInt(9000);
                Card newCard = new Card(currentClient.getFirstName() + " " + currentClient.getLastName(),
                        cardType,
                        cardColor,
                        String.format("%s-%s-%s-%s", cardNumber1, cardNumber2, cardNumber3, cardNumber4),
                        cvv,
                        LocalDate.now(),
                        LocalDate.now().plusYears(5));
                currentClient.addCard(newCard);
                cardRepository.save(newCard);
                return new ResponseEntity<>("The card has been created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("The user has reached the maximum number of cards", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
    }

}
