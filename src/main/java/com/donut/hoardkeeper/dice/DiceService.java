package com.donut.hoardkeeper.dice;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DiceService {

    private final DiceRollParser parser;

    public DiceService(DiceRollParser parser) {
        this.parser = parser;
    }

    public DiceRoll roll(String rawExpression) {
        DiceRollInput input = parser.parse(rawExpression);
        List<Integer> individualRolls = new ArrayList<>();
        int staticModifierTotal = 0;
        int rollTotal = 0;

        for (DiceComponent component : input.components()) {
            if (component instanceof DiceComponent.DiceGroup group) {
                int groupTotal = rollDiceGroup(group, individualRolls);
                rollTotal += groupTotal;
            }
            else if (component instanceof DiceComponent.StaticModifier(int value)) {
                staticModifierTotal += value;
                rollTotal += value;
            }
        }

        return new DiceRoll(rawExpression, individualRolls, staticModifierTotal, rollTotal);
    }

    public int rollDiceGroup(DiceComponent.DiceGroup group, List<Integer> individualRolls) {
        int groupTotal = 0;
        int count = Math.abs(group.diceCount());
        boolean isNegative = group.diceCount() < 0;

        for (int i = 0; i < count; i++) {
            int roll = ThreadLocalRandom.current().nextInt(1, group.dieSides() + 1);
            int finalRoll = isNegative ? -roll : roll;

            individualRolls.add(finalRoll);
            groupTotal += finalRoll;
        }
        return groupTotal;
    }

}
