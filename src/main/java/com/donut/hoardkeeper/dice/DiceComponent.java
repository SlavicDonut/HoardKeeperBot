package com.donut.hoardkeeper.dice;

public sealed interface DiceComponent {
    record DiceGroup(int diceCount, int dieSides, RollMode mode) implements DiceComponent {}
    record StaticModifier(int value) implements DiceComponent {}
}
