package com.finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class TransactionReportGeneratorTest {

    @Test
    public void testBarScalesByUnit() {
        Assertions.assertEquals("***", TransactionReportGenerator.bar(3000.0, 1000.0));
        Assertions.assertEquals("***", TransactionReportGenerator.bar(2600.0, 1000.0));
    }

    @Test
    public void testSmallPositiveAmountStillGetsOneStar() {
        Assertions.assertEquals("*", TransactionReportGenerator.bar(100.0, 1000.0));
    }

    @Test
    public void testZeroAmountHasNoStars() {
        Assertions.assertEquals("", TransactionReportGenerator.bar(0.0, 1000.0));
    }

    @Test
    public void testBuildBarChartAlignsLabels() {
        Map<String, Double> data = new LinkedHashMap<>();
        data.put("Їжа", 2000.0);
        data.put("Оренда", 4000.0);

        String chart = TransactionReportGenerator.buildBarChart(data, 1000.0);

        String[] lines = chart.split("\n");
        Assertions.assertEquals(2, lines.length);
        Assertions.assertEquals("Їжа    | ** 2000.00 грн", lines[0]);
        Assertions.assertEquals("Оренда | **** 4000.00 грн", lines[1]);
    }
}
