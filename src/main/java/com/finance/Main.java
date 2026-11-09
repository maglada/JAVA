package com.finance;

import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class Main {
    private static final String DEFAULT_SOURCE = "https://informer.com.ua/dut/java/pr2.csv";
    private static final String REPORT_MONTH = "01-2024";

    public static void main(String[] args) {
        String source = args.length > 0 ? args[0] : DEFAULT_SOURCE;

        List<Transaction> transactions;
        try {
            transactions = TransactionCSVReader.readTransactions(source);
        } catch (UncheckedIOException e) {
            System.err.println(e.getMessage() + " (" + e.getCause() + ")");
            return;
        }

        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        int count = TransactionAnalyzer.countTransactionsByMonth(transactions, REPORT_MONTH);
        TransactionReportGenerator.printTransactionsCountByMonth(REPORT_MONTH, count);

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);

        YearMonth month = YearMonth.parse(REPORT_MONTH, TransactionAnalyzer.MONTH_YEAR_FORMATTER);
        LocalDate from = month.atDay(1);
        LocalDate to = month.atEndOfMonth();
        TransactionReportGenerator.printExtremeExpensesReport(
                from, to,
                TransactionAnalyzer.findLargestExpense(transactions, from, to),
                TransactionAnalyzer.findSmallestExpense(transactions, from, to));

        TransactionReportGenerator.printExpensesByCategoryReport(
                TransactionAnalyzer.calculateExpensesByCategory(transactions));
        TransactionReportGenerator.printExpensesByMonthReport(
                TransactionAnalyzer.calculateExpensesByMonth(transactions));
    }
}
