package com.donut.hoardkeeper.dice;

import java.util.List;

public record DiceRollInput(List<DiceComponent> components, String rawExpression) {}

