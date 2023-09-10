package com.ap.homebanking.utils;

import com.ap.homebanking.models.Client;

import java.util.Random;

public final class CardUtils {

    private CardUtils() { }

    public static String getCardHolder(Client client) {
        return String.format("%s %s", client.getFirstName(), client.getLastName());
    }

    public static String getCardNumber() {

        Random random = new Random(System.currentTimeMillis());

        int cardNumber1 = 1000 + random.nextInt(9000);
        int cardNumber2 = 1000 + random.nextInt(9000);
        int cardNumber3 = 1000 + random.nextInt(9000);
        int cardNumber4 = 1000 + random.nextInt(9000);

        return String.format("%s-%s-%s-%s", cardNumber1, cardNumber2, cardNumber3, cardNumber4);
    }

    public static int getCvv() {

        Random random = new Random(System.currentTimeMillis());
        return 100 + random.nextInt(900);
    }
}
