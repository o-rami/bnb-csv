package bnb.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface TextIO {

    void print(String message);

    void println(String message);

    void printf(String format, Object... values);

    String readString(String prompt);

    String readRequiredString(String prompt);

    String readGUID(String prompt);

    String readEmail(String prompt);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    boolean readBoolean(String prompt);

    LocalDate readLocalDate(String prompt);

    BigDecimal readBigDecimal(String prompt);

    BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max);

    DateTimeFormatter getFormatter();

    String getInvalidDate();

}
