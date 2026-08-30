package com.donut.hoardkeeper.domain.dice;

import java.util.List;

public record DiceRoll(String rawInput, List<Integer> individualRolls, int modifier, int total) {

}
