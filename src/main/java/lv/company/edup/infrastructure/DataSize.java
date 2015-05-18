package lv.company.edup.infrastructure;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSize implements Comparable<DataSize> {
    private static final Pattern PATTERN = Pattern.compile("^\\s*(\\d+(?:\\.\\d+)?)\\s*([a-zA-Z]+)\\s*$");

    private final double value;
    private final Unit unit;

    public DataSize(double size, Unit unit) {
        this.value = size;
        this.unit = unit;
    }

    public long toBytes() {
        double bytes = getValue(Unit.BYTE);
        return (long) bytes;
    }

    public double getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public double getValue(Unit unit) {
        return value * (this.unit.getFactor() * 1.0 / unit.getFactor());
    }

    public long roundTo(Unit unit) {
        double rounded = Math.floor(getValue(unit) + 0.5d);
        return (long) rounded;
    }

    public DataSize convertTo(Unit unit) {
        return new DataSize(getValue(unit), unit);
    }

    public DataSize convertToMostSuccinctDataSize() {
        Unit unitToUse = Unit.BYTE;
        for (Unit unitToTest : Unit.values()) {
            if (getValue(unitToTest) >= 1.0) {
                unitToUse = unitToTest;
            } else {
                break;
            }
        }
        return convertTo(unitToUse);
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f%s", value, unit.getUnitString());
    }

    public static DataSize valueOf(String size)
            throws IllegalArgumentException {

        Matcher matcher = PATTERN.matcher(size);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("size is not a valid data size string: " + size);
        }

        double value = Double.parseDouble(matcher.group(1));
        String unitString = matcher.group(2);

        for (Unit unit : Unit.values()) {
            if (unit.getUnitString().equals(unitString)) {
                return new DataSize(value, unit);
            }
        }

        throw new IllegalArgumentException("Unknown unit: " + unitString);
    }

    @Override
    public int compareTo(DataSize o) {
        return Double.compare(getValue(Unit.BYTE), o.getValue(Unit.BYTE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataSize dataSize = (DataSize) o;

        return compareTo(dataSize) == 0;
    }

    @Override
    public int hashCode() {
        double value = getValue(Unit.BYTE);
        return Double.valueOf(value).hashCode();
    }

    public enum Unit {
        //This order is important, it should be in increasing magnitude.
        BYTE(1L, "B"),
        KILOBYTE(1L << 10, "kB"),
        MEGABYTE(1L << 20, "MB"),
        GIGABYTE(1L << 30, "GB"),
        TERABYTE(1L << 40, "TB"),
        PETABYTE(1L << 50, "PB");

        private final long factor;
        private final String unitString;

        Unit(long factor, String unitString) {
            this.factor = factor;
            this.unitString = unitString;
        }

        long getFactor() {
            return factor;
        }

        public String getUnitString() {
            return unitString;
        }
    }
}
