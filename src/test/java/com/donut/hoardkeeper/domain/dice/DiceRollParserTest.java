package com.donut.hoardkeeper.domain.dice;

import com.donut.hoardkeeper.domain.dice.DiceComponent;
import com.donut.hoardkeeper.domain.dice.DiceRollInput;
import com.donut.hoardkeeper.domain.dice.DiceRollParser;
import com.donut.hoardkeeper.domain.dice.RollMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class DiceRollParserTest {

    private DiceRollParser parser;

    @BeforeEach
    void setup() {
        parser = new DiceRollParser();
    }

    @Test
    void shouldParseSimpleDiceCountAndModifier() {
        String input = "2d6+3";

        DiceRollInput result = parser.parse(input);

        assertThat(result.components()).hasSize(2);
        assertThat(result.components().getFirst()).isEqualTo(new DiceComponent.DiceGroup(2, 6, RollMode.NORMAL));
        assertThat(result.components().get(1)).isEqualTo(new DiceComponent.StaticModifier(3));
    }

    @Test
    void shouldParseDiceCountAndNegativeModifier() {
        String input = "d20 - 5 + 4d4";

        DiceRollInput result = parser.parse(input);

        assertThat(result.components()).hasSize(3);
        assertThat(result.components().getFirst()).isEqualTo(new DiceComponent.DiceGroup(1, 20, RollMode.NORMAL));
        assertThat(result.components().get(1)).isEqualTo(new DiceComponent.StaticModifier(-5));
        assertThat(result.components().get(2)).isEqualTo(new DiceComponent.DiceGroup(4, 4, RollMode.NORMAL));
    }

    @Test
    void shouldParseNegativeDiceCount() {
        String input = "-4d20";

        DiceRollInput result = parser.parse(input);

        assertThat(result.components()).hasSize(1);
        assertThat(result.components().getFirst()).isEqualTo(new DiceComponent.DiceGroup(-4, 20, RollMode.NORMAL));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "abc", "+++"})
    void shouldThrowExceptionForInvalidInput(String invalidInput) {
        assertThatThrownBy(() -> parser.parse(invalidInput)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2d0", "1d-20", "d-3"})
    void shouldThrowExceptionForInvalidDiceSides(String invalidInput) {
        assertThatThrownBy(() -> parser.parse(invalidInput))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Liczba ścianek musi być większa od 0");
    }
}
