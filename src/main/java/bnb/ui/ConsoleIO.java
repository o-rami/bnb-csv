package bnb.ui;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class ConsoleIO implements TextIO {

    private static final String INVALID_NUMBER = "[INVALID] Enter a valid number.";
    private static final String NUMBER_OUT_OF_RANGE = "[INVALID] Enter a number between %s and %s.";
    private static final String REQUIRED = "[INVALID] Value is required.";
    private static final String INVALID_DATE = "[INVALID] Enter a date in MM/dd/yyyy format.";

    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    @Override
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public String readRequiredString(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (!result.isBlank()) {
                return result;
            }
            println(REQUIRED);
        }
    }

    @Override
    public String readGUID(String prompt) {
        while (true) {
            String result = readString(prompt);
            try {
                if (UUID.fromString(result).toString().equals(result) && result.length() == 36 && !result.isBlank()) {
                    return result;
                }
            } catch (IllegalArgumentException ignored) {
            }
            println(REQUIRED);
        }
    }

    @Override
    public String readEmail(String prompt) { // Simple Regular Expression Validation
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        while (true) {
            String result = readString(prompt);
            try {
                if (Pattern.compile(regexPattern).matcher(result).matches()) {
                    return result;
                }
            } catch (IllegalArgumentException ignored) {
            }
            println(REQUIRED);
        }
    }

    @Override
    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readRequiredString(prompt));
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int result = readInt(prompt);
                if (result >= min && result <= max) {
                    return result;
                }
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    @Override
    public boolean readBoolean(String prompt) {
        while (true) {
            String input = readString(prompt);

            if (input.equalsIgnoreCase("y")) {
                return true;
            } else if (input.equalsIgnoreCase("n")) {
                return false;
            }
            println(INVALID_DATE);
        }
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        while (true) {
            String input = readString(prompt);
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ex) {
                println(INVALID_DATE);
            }
        }
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            String result = readRequiredString(prompt);
            try {
                return new BigDecimal(result);
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        while (true) {
            BigDecimal result = readBigDecimal(prompt);
            if (result.compareTo(min) >= 0 || result.compareTo(max) <= 0) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    @Override
    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String getInvalidDate() {
        return INVALID_DATE;
    }
}
