package com.dfh.tforder.tick;


public class QuoteFactory {

	private static Quote QuoteA;
	private static Quote QuoteB;

	public static Quote getQuoteA() {
		return QuoteA;
	}

	public static Quote getQuoteB() {
		return QuoteB;
	}

	public static void setQuoteA(Quote a) {
		QuoteA = a;
	}

	public static void setQuoteB(Quote b) {
		QuoteB = b;
	}

}
