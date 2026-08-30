package com.donut.hoardkeeper.dice;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DiceRollParser {
    public static final Pattern TOKEN_PATTERN = Pattern.compile("([+-]?\\d*d[+-]?\\d+|[+-]?\\d+)");

    public DiceRollInput parse(String rawExpression) {
        String cleanInput = rawExpression.replaceAll("\\s+", "").toLowerCase();

        if (cleanInput.isEmpty()) {throw new IllegalArgumentException("Wyrażenie nie może być puste!");}

        List<DiceComponent> components = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(cleanInput);

        while (matcher.find()) {
            String token = matcher.group();

            if (token.contains("d")) {components.add(parseDiceGroup(token));}
            else {components.add(parseStaticModifier(token));}
        }

        if (components.isEmpty()) {throw new IllegalArgumentException("Podano niepoprawne wyrażenie!");}

        return new DiceRollInput(components, rawExpression);
    }

    private DiceComponent parseDiceGroup(String token) {
        String[] parts = token.split("d");

        int count;
        if (parts[0].isEmpty() || parts[0].equals("+")) {count = 1;}
        else if (parts[0].equals("-")) {count = -1;}
        else {count = Integer.parseInt(parts[0]);}

        int sides = Integer.parseInt(parts[1]);
        if (sides <= 0) {
            throw new IllegalArgumentException("Liczba ścianek musi być większa od 0");
        }
        return new DiceComponent.DiceGroup(count, sides, RollMode.NORMAL);

    }

    private DiceComponent parseStaticModifier(String token) {
        int value = Integer.parseInt(token.startsWith("+") ? token.substring(1) : token);
        return new DiceComponent.StaticModifier(value);
    }
}
