package com.fjsimon.uberweisung.finance;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Functional {

    public static final BigDecimal HUNDRED = new BigDecimal("100");

    public static void main(String[] args){
        printStocksOverHundred();
        findHighPriced(Tickers.symbols.stream());
    }

    public static void printStocksOverHundred() {
        System.out.println("Stocks priced over $100 are " +
                Tickers.symbols.stream()
                        .filter(symbol -> YahooFinance.getPrice().compareTo(HUNDRED) > 0)
                        .sorted().collect(Collectors.joining(", ")));
    }

    public static void findHighPriced(final Stream<String> symbols){
        final StockInfo highPriced =
                symbols.map(StockUtil::getPrice)
                        .filter(StockUtil.isPriceLessThan(500))
                        .reduce(StockUtil::pickHigh)
                        .get();
        System.out.println("High priced under $500 is " + highPriced);
    }
}
