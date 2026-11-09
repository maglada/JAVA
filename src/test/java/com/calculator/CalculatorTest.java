package com.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorTest {
    private static final double DELTA = 0.0001;

    @Test
    public void testAdd() throws InvalidInputException {
        Assertions.assertEquals(5.5, Calculator.add(2.0, 3.5), DELTA);
        Assertions.assertEquals(-1.0, Calculator.add(2.0, -3.0), DELTA);
    }

    @Test
    public void testSubtract() throws InvalidInputException {
        Assertions.assertEquals(-1.5, Calculator.subtract(2.0, 3.5), DELTA);
    }

    @Test
    public void testMultiply() throws InvalidInputException {
        Assertions.assertEquals(7.0, Calculator.multiply(2.0, 3.5), DELTA);
        Assertions.assertEquals(0.0, Calculator.multiply(0.0, 99.0), DELTA);
    }

    @Test
    public void testDivide() throws InvalidInputException {
        Assertions.assertEquals(2.5, Calculator.divide(5.0, 2.0), DELTA);
        Assertions.assertEquals(-2.0, Calculator.divide(4.0, -2.0), DELTA);
    }

    @Test
    public void testDivideByZeroThrowsArithmeticException() {
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class,
                () -> Calculator.divide(10.0, 0.0));
        Assertions.assertEquals("Ділення на нуль неможливе", exception.getMessage());
    }

    @Test
    public void testDivideByNegativeZeroThrowsArithmeticException() {
        Assertions.assertThrows(ArithmeticException.class, () -> Calculator.divide(10.0, -0.0));
    }

    @Test
    public void testZeroDividedByNumberIsZero() throws InvalidInputException {
        Assertions.assertEquals(0.0, Calculator.divide(0.0, 5.0), DELTA);
    }

    @Test
    public void testSqrt() throws InvalidInputException {
        Assertions.assertEquals(4.0, Calculator.sqrt(16.0), DELTA);
        Assertions.assertEquals(0.0, Calculator.sqrt(0.0), DELTA);
    }

    @Test
    public void testSqrtOfNegativeThrowsInvalidInputException() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class,
                () -> Calculator.sqrt(-4.0));
        Assertions.assertTrue(exception.getMessage().contains("від'ємного"));
    }

    @Test
    public void testNaNThrowsInvalidInputExceptionInEveryOperation() {
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.add(Double.NaN, 1.0));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.subtract(1.0, Double.NaN));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.multiply(Double.NaN, 1.0));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.divide(1.0, Double.NaN));
        Assertions.assertThrows(InvalidInputException.class, () -> Calculator.sqrt(Double.NaN));
    }

    @Test
    public void testInfinityThrowsInvalidInputException() {
        Assertions.assertThrows(InvalidInputException.class,
                () -> Calculator.add(Double.POSITIVE_INFINITY, 1.0));
        Assertions.assertThrows(InvalidInputException.class,
                () -> Calculator.divide(1.0, Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testOverflowThrowsArithmeticException() {
        Assertions.assertThrows(ArithmeticException.class,
                () -> Calculator.multiply(Double.MAX_VALUE, 2.0));
        Assertions.assertThrows(ArithmeticException.class,
                () -> Calculator.add(Double.MAX_VALUE, Double.MAX_VALUE));
    }
}
