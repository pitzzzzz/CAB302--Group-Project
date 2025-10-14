package com.javaninjas.careerpathway.core.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Small utility for formatting salary / currency values in the UI.
 */
public final class CurrencyUtils {

    private CurrencyUtils() { }

    /**
     * Formats the provided integer amount as a US-style currency string with
     * grouping and no fractional digits (e.g. 12345 -> "$12,345").
     *
     * @param amount integer amount in dollars
     * @return formatted currency string
     */
    public static String formatCurrency(long amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(true);
        return nf.format(amount);
    }

    /**
     * Convenience overload for ints
     */
    public static String formatCurrency(int amount) {
        return formatCurrency((long) amount);
    }
}
