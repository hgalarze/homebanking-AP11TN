package com.ap.homebanking.services.implement;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.services.CardService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Random;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientService clientService;

    private static final int MAX_CARDS_BY_CLIENT = 3;
    private static final int MAX_CARD_VALID_YEARS = 5;

    @Override
    public void createCard(Authentication authentication, CardType cardType, CardColor cardColor) throws Exception {

        Client currentClient = clientService.findByEmail(authentication.getName());

        if (currentClient == null) {
            throw new EntityNotFoundException("Client not found");
        }

        if (currentClient.getCards().size() >= MAX_CARDS_BY_CLIENT) {
            throw new LimitExceededException("The user has reached the maximum number of cards");
        }

        String cardHolder = this.getCardHolder(currentClient);
        String cardNumber = this.generateCardNumber();
        int cvv = this.generateCvv();
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(MAX_CARD_VALID_YEARS);

        Card newCard = new Card(cardHolder, cardType, cardColor, cardNumber, cvv, thruDate, fromDate);

        currentClient.addCard(newCard);
        cardRepository.save(newCard);
    }

    private String getCardHolder(Client client) {
        return String.format("%s %s", client.getFirstName(), client.getLastName());
    }

    private String generateCardNumber() {

        Random random = new Random(System.currentTimeMillis());

        int cardNumber1 = 1000 + random.nextInt(9000);
        int cardNumber2 = 1000 + random.nextInt(9000);
        int cardNumber3 = 1000 + random.nextInt(9000);
        int cardNumber4 = 1000 + random.nextInt(9000);

        return String.format("%s-%s-%s-%s", cardNumber1, cardNumber2, cardNumber3, cardNumber4);
    }

    private int generateCvv() {

        Random random = new Random(System.currentTimeMillis());
        return 100 + random.nextInt(900);
    }
}
