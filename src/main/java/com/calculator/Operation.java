package com.calculator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Operation {
    ADD("+", false),
    SUBTRACT("-", false),
    MULTIPLY("*", false),
    DIVIDE("/", false),
    SQRT("sqrt", true);

    private final String symbol;
    private final boolean unary;

    public static Optional<Operation> fromSymbol(String symbol) {
        String normalized = symbol.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(operation -> operation.symbol.equals(normalized))
                .findFirst();
    }

    public double apply(double first, double second) throws InvalidInputException {
        return switch (this) {
            case ADD -> Calculator.add(first, second);
            case SUBTRACT -> Calculator.subtract(first, second);
            case MULTIPLY -> Calculator.multiply(first, second);
            case DIVIDE -> Calculator.divide(first, second);
            case SQRT -> Calculator.sqrt(first);
        };
    }
}
