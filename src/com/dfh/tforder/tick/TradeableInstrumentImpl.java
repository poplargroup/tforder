package com.dfh.tforder.tick;

public class TradeableInstrumentImpl implements TradeableInstrument {

	private Exchange exchange;

	private String symbol = "FOO";

	@Override
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbal) {
		this.symbol = symbal;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	@Override
	public String toString() {
		if (exchange == Exchange.SHANG_HAI) {
			return symbol + ".SH";
		} else if (exchange == Exchange.SHEN_ZHEN) {
			return symbol + ".SZ";
		} else if (exchange == Exchange.INDEX_FUTURE) {
			return symbol + ".ZJ";
		} else {
			return symbol + ".CF";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = result + ((exchange == null) ? 0 : exchange.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeableInstrumentImpl other = (TradeableInstrumentImpl) obj;

		return (symbol.equals(other.symbol) && exchange == other.exchange);
	}

}
