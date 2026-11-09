package com.finance;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
public abstract class TransactionCSVReader {
    private static final String SEPARATOR = ",";
    private static final int FIELDS_COUNT = 3;

    public static List<Transaction> readTransactions(String location) {
        try (Reader reader = openReader(location)) {
            return parse(reader);
        } catch (IOException e) {
            throw new UncheckedIOException("Не вдалося прочитати джерело даних: " + location, e);
        }
    }

    public static Reader openReader(String location) throws IOException {
        if (isUrl(location)) {
            return new InputStreamReader(URI.create(location).toURL().openStream(), StandardCharsets.UTF_8);
        }
        return Files.newBufferedReader(Path.of(location), StandardCharsets.UTF_8);
    }

    public static List<Transaction> parse(Reader reader) throws IOException {
        BufferedReader bufferedReader = reader instanceof BufferedReader br ? br : new BufferedReader(reader);
        List<Transaction> transactions = new ArrayList<>();
        int lineNumber = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lineNumber++;
            if (line.isBlank()) {
                continue;
            }
            Optional<Transaction> transaction = parseLine(line);
            if (transaction.isPresent()) {
                transactions.add(transaction.get());
            } else {
                log.warning("Рядок " + lineNumber + " пропущено: " + line);
            }
        }
        return transactions;
    }

    public static Optional<Transaction> parseLine(String line) {
        String[] values = line.replace("\uFEFF", "").split(SEPARATOR, FIELDS_COUNT);
        if (values.length < FIELDS_COUNT) {
            return Optional.empty();
        }
        try {
            double amount = Double.parseDouble(values[1].trim());
            return Optional.of(new Transaction(values[0].trim(), amount, clean(values[2])));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static boolean isUrl(String location) {
        return location.startsWith("http://") || location.startsWith("https://") || location.startsWith("file:");
    }

    private static String clean(String value) {
        String trimmed = value.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1).trim();
        }
        return trimmed;
    }
}
