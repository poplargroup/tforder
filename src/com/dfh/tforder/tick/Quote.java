package com.dfh.tforder.tick;

public class Quote {

	String code;
	Float ask1Price;
	Float ask1Volume;
	Float bid1Price;
	Float bid1Volume;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Float getAsk1Price() {
		return ask1Price;
	}

	public void setAsk1Price(Float ask1Price) {
		this.ask1Price = ask1Price;
	}

	public Float getAsk1Volume() {
		return ask1Volume;
	}

	public void setAsk1Volume(Float ask1Volume) {
		this.ask1Volume = ask1Volume;
	}

	public Float getBid1Price() {
		return bid1Price;
	}

	public void setBid1Price(Float bid1Price) {
		this.bid1Price = bid1Price;
	}

	public Float getBid1Volume() {
		return bid1Volume;
	}

	public void setBid1Volume(Float bid1Volume) {
		this.bid1Volume = bid1Volume;
	}

}
