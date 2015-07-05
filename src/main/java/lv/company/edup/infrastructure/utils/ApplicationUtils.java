package lv.company.edup.infrastructure.utils;

import lv.company.edup.infrastructure.utils.amount.NumberToWords;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class ApplicationUtils {

    public static String getAmountValue(Long amount) {
        return new BigDecimal(amount).movePointLeft(2).toString();
    }

    public static  String getValue(String value) {
        return StringUtils.isNotEmpty(value) ? value : "";
    }

    public static String getValue(Long value) {
        return value != null ? String.valueOf(value) : "";
    }

    public static  String formatAmount(Long amount) {
        Long cents = amount % 100;
        Long euros = (amount - cents) / 100;
        return String.format("(%s EUR) %s eiro un %s %s",
                getAmountValue(amount),
                NumberToWords.LATVIAN_LOCALE.convert(euros),
                NumberToWords.LATVIAN_LOCALE.convert(cents),
                StringUtils.endsWith(String.valueOf(cents), "1") ? "cents" : "centi");
    }

}
