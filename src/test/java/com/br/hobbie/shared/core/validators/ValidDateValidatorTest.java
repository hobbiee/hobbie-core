package com.br.hobbie.shared.core.validators;

import com.br.hobbie.shared.factory.FakeDateTimeResolver;
import com.br.hobbie.shared.factory.ValidDateValidorTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidDateValidatorTest {
    private ValidDateValidator validDateValidator;
    private FakeDateTimeResolver fakeDateTimeResolver;


    @BeforeEach
    void setUp() {
        validDateValidator = new ValidDateValidator();
    }

    @Test
    @DisplayName("Should return true when date is valid")
    void shouldReturnTrue_WhenDateIsValid() {
        // GIVEN
        fakeDateTimeResolver = ValidDateValidorTestFactory.validDate();

        // WHEN
        boolean isValid = validDateValidator.isValid(fakeDateTimeResolver, null);

        // THEN
        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when date is valid and starts at 23 hours")
    void shouldReturnFalse_WhenDateIsValidAndStartsAt23Hours() {
        // GIVEN
        fakeDateTimeResolver = ValidDateValidorTestFactory.validDateStartsAt23Hours();

        // WHEN
        boolean isValid = validDateValidator.isValid(fakeDateTimeResolver, null);

        // THEN
        Assertions.assertFalse(isValid);
    }


    @Test
    @DisplayName("Should return false when date is null")
    void shouldReturnFalse_WhenDateIsNull() {
        // WHEN
        boolean isValid = validDateValidator.isValid(null, null);

        // THEN
        Assertions.assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when date starts after 23 hours")
    void shouldReturnFalse_WhenDateStartsAfter23Hours() {
        // GIVEN
        fakeDateTimeResolver = ValidDateValidorTestFactory.invalidDateStartsAtMidnight();

        // WHEN
        boolean isValid = validDateValidator.isValid(fakeDateTimeResolver, null);

        // THEN
        Assertions.assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when date ends before start")
    void shouldReturnFalse_WhenDateEndsBeforeStart() {
        // GIVEN
        fakeDateTimeResolver = ValidDateValidorTestFactory.invalidDateEndsBeforeStart();

        // WHEN
        boolean isValid = validDateValidator.isValid(fakeDateTimeResolver, null);

        // THEN
        Assertions.assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when date is tomorrow and starts after 23 hours")
    void shouldReturnFalse_WhenDateIsTomorrowAndStartsAfter23Hours() {
        // GIVEN
        fakeDateTimeResolver = ValidDateValidorTestFactory.invalidDateTomorrowStartingAfterElevenPM();

        // WHEN
        boolean isValid = validDateValidator.isValid(fakeDateTimeResolver, null);

        // THEN
        Assertions.assertFalse(isValid);
    }
}
