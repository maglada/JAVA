package com.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OperationTest {

    @Test
    public void testFromSymbolIsCaseAndSpaceInsensitive() {
        Assertions.assertEquals(Operation.SQRT, Operation.fromSymbol("  SQRT ").orElseThrow());
        Assertions.assertEquals(Operation.ADD, Operation.fromSymbol("+").orElseThrow());
    }

    @Test
    public void testUnknownSymbolIsEmpty() {
        Assertions.assertTrue(Operation.fromSymbol("^").isEmpty());
    }

    @Test
    public void testApplyUsesCalculator() throws InvalidInputException {
        Assertions.assertEquals(12.0, Operation.MULTIPLY.apply(3.0, 4.0), 0.0001);
        Assertions.assertEquals(3.0, Operation.SQRT.apply(9.0, 100.0), 0.0001);
    }
}
