package com.donut.hoardkeeper.domain.dice;

import java.util.List;

public record DiceRollInput(List<DiceComponent> components, String rawExpression) {}

