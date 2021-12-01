package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {
    //prueba num tarjeta
    @Test
    public void cardNumberIsCreated(){
        String cardNumber = Utils.getCardNumber(1000, 9999);
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    //prueba CVV
    @Test
    public void numberCVV(){
        int cvv = Utils.getCVV(100, 999);
        assertThat(cvv, notNullValue(Integer.class));
    }
    @Test
    public void isNum(){
        int cvv = Utils.getCVV(100, 999);
        assertThat(cvv, instanceOf(Integer.class));
    }
}
