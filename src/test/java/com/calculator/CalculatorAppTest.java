package com.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class CalculatorAppTest {

    private static String run(String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        new CalculatorApp(new Scanner(input), out).run();
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static int count(String text, String part) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(part, index)) >= 0) {
            count++;
            index += part.length();
        }
        return count;
    }

    @Test
    public void testSuccessfulDivision() {
        String output = run("6\n3\n/\nн\n");

        Assertions.assertTrue(output.contains("Результат: 2"), output);
        Assertions.assertEquals(1, count(output, CalculatorApp.FINISHED_MESSAGE));
    }

    @Test
    public void testCommaAsDecimalSeparator() {
        String output = run("1,5\n2,5\n+\nн\n");

        Assertions.assertTrue(output.contains("Результат: 4"), output);
    }

    @Test
    public void testDivisionByZeroShowsMessageAndFinally() {
        String output = run("5\n0\n/\nн\n");

        Assertions.assertTrue(output.contains("Арифметична помилка: Ділення на нуль неможливе"), output);
        Assertions.assertEquals(1, count(output, CalculatorApp.FINISHED_MESSAGE));
    }

    @Test
    public void testTextInsteadOfNumberShowsMessageAndFinally() {
        String output = run("abc\nн\n");

        Assertions.assertTrue(output.contains("'abc' не є числом"), output);
        Assertions.assertEquals(1, count(output, CalculatorApp.FINISHED_MESSAGE));
    }

    @Test
    public void testTextInSecondNumber() {
        String output = run("5\nxyz\nн\n");

        Assertions.assertTrue(output.contains("'xyz' не є числом"), output);
    }

    @Test
    public void testEmptyInputIsNotANumber() {
        String output = run("\nн\n");

        Assertions.assertTrue(output.contains("не є числом"), output);
    }

    @Test
    public void testSqrtOfNegativeShowsCustomException() {
        String output = run("-4\n0\nsqrt\nн\n");

        Assertions.assertTrue(output.contains("Некоректні вхідні дані"), output);
        Assertions.assertTrue(output.contains("від'ємного"), output);
        Assertions.assertEquals(1, count(output, CalculatorApp.FINISHED_MESSAGE));
    }

    @Test
    public void testSqrtOfPositive() {
        String output = run("9\n0\nsqrt\nн\n");

        Assertions.assertTrue(output.contains("Результат: 3"), output);
    }

    @Test
    public void testNaNInputShowsCustomException() {
        String output = run("NaN\n1\n+\nн\n");

        Assertions.assertTrue(output.contains("Некоректні вхідні дані: Недопустиме значення"), output);
    }

    @Test
    public void testUnknownOperationShowsCustomException() {
        String output = run("1\n2\n%\nн\n");

        Assertions.assertTrue(output.contains("Невідома операція: '%'"), output);
    }

    @Test
    public void testFinallyRunsForEveryRequest() {
        String output = run("1\n1\n+\nт\nabc\nт\n8\n0\n/\nн\n");

        Assertions.assertEquals(3, count(output, CalculatorApp.FINISHED_MESSAGE));
    }

    @Test
    public void testEndOfInputDoesNotCrash() {
        String output = run("1\n");

        Assertions.assertTrue(output.contains("Введення завершено."), output);
        Assertions.assertEquals(1, count(output, CalculatorApp.FINISHED_MESSAGE));
    }
}
