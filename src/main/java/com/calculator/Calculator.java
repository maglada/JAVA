package com.calculator;

public abstract class Calculator {

    public static double add(double a, double b) throws InvalidInputException {
        requireFinite(a, b);
        return checkResult(a + b);
    }

    public static double subtract(double a, double b) throws InvalidInputException {
        requireFinite(a, b);
        return checkResult(a - b);
    }

    public static double multiply(double a, double b) throws InvalidInputException {
        requireFinite(a, b);
        return checkResult(a * b);
    }

    public static double divide(double a, double b) throws InvalidInputException {
        requireFinite(a, b);
        if (b == 0) {
            throw new ArithmeticException("Ділення на нуль неможливе");
        }
        return checkResult(a / b);
    }

    public static double sqrt(double a) throws InvalidInputException {
        requireFinite(a);
        if (a < 0) {
            throw new InvalidInputException("Квадратний корінь з від'ємного числа неможливий: " + a);
        }
        return Math.sqrt(a);
    }

    private static void requireFinite(double... values) throws InvalidInputException {
        for (double value : values) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new InvalidInputException("Недопустиме значення: " + value);
            }
        }
    }

    private static double checkResult(double result) {
        if (Double.isInfinite(result)) {
            throw new ArithmeticException("Результат виходить за межі допустимого діапазону double");
        }
        return result;
    }
}
