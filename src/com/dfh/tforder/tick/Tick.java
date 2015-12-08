package com.dfh.tforder.tick;

import java.util.concurrent.atomic.AtomicBoolean;

public class Tick {

	public enum Type {
		BID, ASK, TRADE, LEVEL_ONE, START_DAY, END_DAY, END_RUN
	}

	private Type type;
	private float price;
	private long sentTime;
	private long receivedTime;
	private long timestamp1;
	private long timestamp2;

	private AtomicBoolean isCaughtUp = new AtomicBoolean(false);

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public long getSentTime() {
		return sentTime;
	}

	public void setSentTime(long sentTime) {
		this.sentTime = sentTime;
	}

	public long getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(long receivedTime) {
		this.receivedTime = receivedTime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isCaughtUp() {
		return isCaughtUp.get();
	}

	public void setCaughtUp(boolean isCaughtUp) {
		this.isCaughtUp.set(isCaughtUp);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		// result.append("Tick [instrument=" + instrument);

		result.append(", receivedTime=" + receivedTime + ", sentTime=" + sentTime + ", price=" + price + ", type=" + type + ", caughtup=" + isCaughtUp.get() + "]");

		return result.toString();

	}

	public long getTimestamp1() {
		return timestamp1;
	}

	public void setTimestamp1(long timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public long getTimestamp2() {
		return timestamp2;
	}

	public void setTimestamp2(long timestamp2) {
		this.timestamp2 = timestamp2;
	}
}