package com.ap.homebanking;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardUtilsTests {

    @Test
    public void cardNumberIsCreated(){

        // Act
        String cardNumber = CardUtils.getCardNumber();

        // Assert
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardHolderIsNotNullOrEmpty(){

        // Arrange
        Client testClient = new Client("My", "Test", "mytest@testing.com", null);

        // Act
        String cardHolder = CardUtils.getCardHolder(testClient);

        // Assert
        assertThat(cardHolder,is(not(emptyOrNullString())));
        assertThat(cardHolder,is(equalTo("My Test")));
    }

    @Test
    public void cvvIsCreated(){

        // Act
        int cvv = CardUtils.getCvv();

        // Assert
        assertThat(cvv,is(greaterThanOrEqualTo(100)));
        assertThat(cvv, is(lessThanOrEqualTo(999)));
    }
}
