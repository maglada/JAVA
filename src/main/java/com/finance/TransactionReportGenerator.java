package com.finance;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class TransactionReportGenerator {
    public static final double CHART_UNIT = 1000.0;
    private static final String CURRENCY = "грн";

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println(topExpenses.size() + " найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExtremeExpensesReport(LocalDate from, LocalDate to,
                                                  Optional<Transaction> largest,
                                                  Optional<Transaction> smallest) {
        System.out.println("Витрати за період " + from.format(TransactionAnalyzer.DATE_FORMATTER)
                + " - " + to.format(TransactionAnalyzer.DATE_FORMATTER) + ":");
        System.out.println("  Найбільша: " + describe(largest));
        System.out.println("  Найменша: " + describe(smallest));
    }

    public static void printExpensesByCategoryReport(Map<String, Double> expensesByCategory) {
        System.out.println("Витрати по категоріях (1 * = " + (int) CHART_UNIT + " " + CURRENCY + "):");
        System.out.println(buildBarChart(expensesByCategory, CHART_UNIT));
    }

    public static void printExpensesByMonthReport(Map<YearMonth, Double> expensesByMonth) {
        Map<String, Double> labeled = new LinkedHashMap<>();
        expensesByMonth.forEach((month, total) ->
                labeled.put(month.format(TransactionAnalyzer.MONTH_YEAR_FORMATTER), total));
        System.out.println("Витрати по місяцях (1 * = " + (int) CHART_UNIT + " " + CURRENCY + "):");
        System.out.println(buildBarChart(labeled, CHART_UNIT));
    }

    public static String buildBarChart(Map<String, Double> data, double unit) {
        int width = data.keySet().stream().mapToInt(String::length).max().orElse(0);
        String format = "%-" + Math.max(width, 1) + "s | %s %.2f " + CURRENCY;
        StringBuilder chart = new StringBuilder();
        data.forEach((label, amount) -> {
            if (chart.length() > 0) {
                chart.append("\n");
            }
            chart.append(String.format(Locale.US, format, label, bar(amount, unit), amount));
        });
        return chart.toString();
    }

    public static String bar(double amount, double unit) {
        int stars = (int) Math.round(amount / unit);
        if (amount > 0 && stars == 0) {
            stars = 1;
        }
        return "*".repeat(Math.max(stars, 0));
    }

    private static String describe(Optional<Transaction> transaction) {
        return transaction
                .map(t -> t.getDate() + " " + t.getDescription() + ": " + t.getAmount())
                .orElse("немає витрат за цей період");
    }
}
