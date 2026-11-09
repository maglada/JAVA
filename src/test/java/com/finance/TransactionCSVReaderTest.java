package com.finance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TransactionCSVReaderTest {

    @Test
    public void testParseReadsAllFields() throws IOException {
        String csv = "05-01-2024,-120.50,Продукти\n06-01-2024,25000,Зарплата\n";

        List<Transaction> transactions = TransactionCSVReader.parse(new StringReader(csv));

        Assertions.assertEquals(2, transactions.size());
        Assertions.assertEquals(new Transaction("05-01-2024", -120.50, "Продукти"), transactions.get(0));
        Assertions.assertEquals(25000.0, transactions.get(1).getAmount(), 0.0001);
    }

    @Test
    public void testParseSkipsHeaderBlankAndMalformedLines() throws IOException {
        String csv = "date,amount,description\n\n05-01-2024,-10,Їжа\nбитий рядок\n06-01-2024,abc,Помилка\n";

        List<Transaction> transactions = TransactionCSVReader.parse(new StringReader(csv));

        Assertions.assertEquals(1, transactions.size());
        Assertions.assertEquals("Їжа", transactions.get(0).getDescription());
    }

    @Test
    public void testParseKeepsCommasInDescription() throws IOException {
        List<Transaction> transactions = TransactionCSVReader.parse(new StringReader("05-01-2024,-10,Кафе, обід\n"));

        Assertions.assertEquals("Кафе, обід", transactions.get(0).getDescription());
    }

    @Test
    public void testReadTransactionsFromLocalFile() throws IOException {
        Path file = Files.createTempFile("transactions", ".csv");
        try {
            Files.writeString(file, "01-02-2024,-50,Транспорт\n02-02-2024,100,Дохід\n", StandardCharsets.UTF_8);

            List<Transaction> transactions = TransactionCSVReader.readTransactions(file.toString());

            Assertions.assertEquals(2, transactions.size());
            Assertions.assertEquals(50.0, TransactionAnalyzer.calculateTotalBalance(transactions), 0.0001);
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
