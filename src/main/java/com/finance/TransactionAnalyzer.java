package com.finance;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {
    public static final int DEFAULT_TOP_LIMIT = 10;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");

    public static double calculateTotalBalance(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        YearMonth target = YearMonth.parse(monthYear, MONTH_YEAR_FORMATTER);
        return (int) transactions.stream()
                .filter(t -> YearMonth.from(toDate(t)).equals(target))
                .count();
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return findTopExpenses(transactions, DEFAULT_TOP_LIMIT);
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions, int limit) {
        return transactions.stream()
                .filter(TransactionAnalyzer::isExpense)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static List<Transaction> filterByPeriod(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(t -> {
                    LocalDate date = toDate(t);
                    return !date.isBefore(from) && !date.isAfter(to);
                })
                .collect(Collectors.toList());
    }

    public static Optional<Transaction> findLargestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return filterByPeriod(transactions, from, to).stream()
                .filter(TransactionAnalyzer::isExpense)
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Optional<Transaction> findSmallestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return filterByPeriod(transactions, from, to).stream()
                .filter(TransactionAnalyzer::isExpense)
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Map<String, Double> calculateExpensesByCategory(List<Transaction> transactions) {
        Map<String, Double> totals = transactions.stream()
                .filter(TransactionAnalyzer::isExpense)
                .collect(Collectors.groupingBy(
                        Transaction::getDescription,
                        Collectors.summingDouble(t -> -t.getAmount())));
        return totals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));
    }

    public static Map<YearMonth, Double> calculateExpensesByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .filter(TransactionAnalyzer::isExpense)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(toDate(t)),
                        TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }

    public static LocalDate toDate(Transaction transaction) {
        return LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
    }

    public static boolean isExpense(Transaction transaction) {
        return transaction.getAmount() < 0;
    }
}
