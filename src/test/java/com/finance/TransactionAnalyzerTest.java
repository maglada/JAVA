package com.finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class TransactionAnalyzerTest {

    @Test
    public void testCalculateTotalBalance() {
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("03-01-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        double result = TransactionAnalyzer.calculateTotalBalance(transactions);

        Assertions.assertEquals(200.0, result, 0.0001, "Розрахунок загального балансу неправильний");
    }

    @Test
    public void testCalculateTotalBalanceOfEmptyList() {
        Assertions.assertEquals(0.0, TransactionAnalyzer.calculateTotalBalance(new ArrayList<>()), 0.0001);
    }

    @Test
    public void testCountTransactionsByMonth() {
        Transaction transaction1 = new Transaction("01-02-2023", 50.0, "Дохід");
        Transaction transaction2 = new Transaction("15-02-2023", -20.0, "Витрата");
        Transaction transaction3 = new Transaction("05-03-2023", 100.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        int countFeb = TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023");
        int countMar = TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023");
        int countApr = TransactionAnalyzer.countTransactionsByMonth(transactions, "04-2023");

        Assertions.assertEquals(2, countFeb, "Кількість транзакцій за лютий неправильна");
        Assertions.assertEquals(1, countMar, "Кількість транзакцій за березень неправильна");
        Assertions.assertEquals(0, countApr, "Кількість транзакцій за квітень неправильна");
    }

    @Test
    public void testCountTransactionsByMonthDistinguishesYears() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("10-02-2023", 1.0, "a"),
                new Transaction("10-02-2024", 1.0, "b"));

        Assertions.assertEquals(1, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2024"));
    }

    @Test
    public void testFindTopExpensesReturnsTenLargestSortedDescendingByAbsoluteValue() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            transactions.add(new Transaction("01-01-2024", -i * 10.0, "Витрата " + i));
        }
        transactions.add(new Transaction("02-01-2024", 5000.0, "Дохід"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(10, top.size(), "Має бути рівно 10 витрат");
        Assertions.assertEquals(-150.0, top.get(0).getAmount(), 0.0001, "Перша витрата має бути найбільшою");
        Assertions.assertEquals(-60.0, top.get(9).getAmount(), 0.0001, "Десята витрата неправильна");
        Assertions.assertTrue(top.stream().allMatch(t -> t.getAmount() < 0), "Доходи не мають потрапляти у список");
    }

    @Test
    public void testFindTopExpensesWithFewerExpensesThanLimit() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2024", -10.0, "a"),
                new Transaction("02-01-2024", 100.0, "b"));

        Assertions.assertEquals(1, TransactionAnalyzer.findTopExpenses(transactions).size());
    }

    @Test
    public void testLargestAndSmallestExpenseInPeriod() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("05-01-2024", -100.0, "Середня"),
                new Transaction("10-01-2024", -700.0, "Найбільша"),
                new Transaction("15-01-2024", -5.0, "Найменша"),
                new Transaction("20-01-2024", 900.0, "Дохід"),
                new Transaction("05-02-2024", -9999.0, "Поза періодом"));
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);

        Transaction largest = TransactionAnalyzer.findLargestExpense(transactions, from, to).orElseThrow();
        Transaction smallest = TransactionAnalyzer.findSmallestExpense(transactions, from, to).orElseThrow();

        Assertions.assertEquals("Найбільша", largest.getDescription());
        Assertions.assertEquals("Найменша", smallest.getDescription());
    }

    @Test
    public void testExpensesInPeriodAreEmptyWhenNoExpenses() {
        List<Transaction> transactions = Arrays.asList(new Transaction("05-01-2024", 100.0, "Дохід"));

        Assertions.assertTrue(TransactionAnalyzer
                .findLargestExpense(transactions, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).isEmpty());
    }

    @Test
    public void testPeriodBoundsAreInclusive() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2024", -1.0, "a"),
                new Transaction("31-01-2024", -2.0, "b"));

        List<Transaction> result = TransactionAnalyzer.filterByPeriod(
                transactions, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testCalculateExpensesByCategorySortedDescending() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2024", -100.0, "Їжа"),
                new Transaction("02-01-2024", -300.0, "Одяг"),
                new Transaction("03-01-2024", -50.0, "Їжа"),
                new Transaction("04-01-2024", 1000.0, "Зарплата"));

        Map<String, Double> result = TransactionAnalyzer.calculateExpensesByCategory(transactions);

        Assertions.assertEquals(2, result.size(), "Доходи не мають входити до категорій витрат");
        Assertions.assertEquals(List.of("Одяг", "Їжа"), new ArrayList<>(result.keySet()));
        Assertions.assertEquals(150.0, result.get("Їжа"), 0.0001);
    }

    @Test
    public void testCalculateExpensesByMonthIsChronological() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("10-03-2024", -30.0, "a"),
                new Transaction("10-01-2024", -10.0, "a"),
                new Transaction("20-01-2024", -15.0, "b"));

        Map<YearMonth, Double> result = TransactionAnalyzer.calculateExpensesByMonth(transactions);

        Assertions.assertEquals(List.of(YearMonth.of(2024, 1), YearMonth.of(2024, 3)), new ArrayList<>(result.keySet()));
        Assertions.assertEquals(25.0, result.get(YearMonth.of(2024, 1)), 0.0001);
    }
}
