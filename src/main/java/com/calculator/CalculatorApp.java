package com.calculator;

import lombok.RequiredArgsConstructor;

import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

@RequiredArgsConstructor
public class CalculatorApp {
    public static final String FINISHED_MESSAGE = "Обробку запиту завершено.";

    private final Scanner scanner;
    private final PrintStream out;

    public void run() {
        out.println("Калькулятор. Операції: + - * / sqrt (для sqrt береться лише перше число)");
        try {
            do {
                processRequest();
            } while (askToContinue());
        } catch (NoSuchElementException e) {
            out.println("Введення завершено.");
        }
        out.println("До побачення!");
    }

    public void processRequest() {
        try {
            double first = readNumber("Введіть перше число: ");
            double second = readNumber("Введіть друге число: ");
            Operation operation = readOperation();
            double result = operation.apply(first, second);
            out.println("Результат: " + format(result));
        } catch (NumberFormatException e) {
            out.println("Помилка введення: " + e.getMessage());
        } catch (InvalidInputException e) {
            out.println("Некоректні вхідні дані: " + e.getMessage());
        } catch (ArithmeticException e) {
            out.println("Арифметична помилка: " + e.getMessage());
        } finally {
            out.println(FINISHED_MESSAGE);
        }
    }

    private double readNumber(String prompt) {
        out.print(prompt);
        String text = scanner.nextLine().trim();
        try {
            return Double.parseDouble(text.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("'" + text + "' не є числом");
        }
    }

    private Operation readOperation() throws InvalidInputException {
        out.print("Введіть операцію (+, -, *, /, sqrt): ");
        String symbol = scanner.nextLine();
        return Operation.fromSymbol(symbol)
                .orElseThrow(() -> new InvalidInputException("Невідома операція: '" + symbol.trim() + "'"));
    }

    private boolean askToContinue() {
        out.print("Виконати ще одну операцію? (т/н): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.startsWith("т") || answer.startsWith("y");
    }

    private static String format(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
