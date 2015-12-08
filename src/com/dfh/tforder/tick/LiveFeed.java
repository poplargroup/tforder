package com.dfh.tforder.tick;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

public class LiveFeed {

	// private static final Logger LOG = Logger.getLogger(LiveFeed.class);

	static final char SOH = 0x01;
	static final char EOT = 0x04;
	static final char ETX = 0x03;

	private LiveFeedSocketReader reader;

	private InetAddress hostAddr;

	private int port;

	private AtomicInteger msgIndex = new AtomicInteger(10);
	// private AtomicInteger clientSequence = new AtomicInteger(1000);

	private Map<TradeableInstrument, Integer> symbolToSequence = new HashMap<TradeableInstrument, Integer>();

	private OutputStream output;

	@PostConstruct
	public void springInit() {
		try {
			Socket socket = new Socket(hostAddr, port);

			output = socket.getOutputStream();
			reader = new LiveFeedSocketReader();
			reader.setInputStream(socket.getInputStream());
			reader.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		LiveFeed feed = new LiveFeed();
		feed.setHost("172.16.47.17");
		feed.setPort(6999);
		feed.springInit();
		TradeableInstrumentImpl ins = new TradeableInstrumentImpl();
		ins.setSymbol("600399");
		ins.setExchange(Exchange.SHANG_HAI);
		try {
			feed.subscribeInternal(ins);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String formatLength(int length) {
		String result = Integer.toString(length);
		while (result.length() < 4) {
			result = "0" + result;
		}
		return result;
	}

	synchronized void reconnect() {
		while (true) {
			try {
				Socket socket = new Socket(hostAddr, port);
				output = socket.getOutputStream();

				reader.setInputStream(socket.getInputStream());
				break;
			} catch (IOException e) {
				// LOG.error("Live feed error " + e, e);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// LOG.info("Thread interuppted");
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
	}

	public void unsubscribeInternal() throws IOException {
		StringBuilder headerBuffer = new StringBuilder();
		int messageIndex = msgIndex.getAndIncrement();
		headerBuffer.append(SOH);
		headerBuffer.append(messageIndex).append(SOH); // message index
		headerBuffer.append(SOH); // reserve
		headerBuffer.append("0G4").append(SOH);
		headerBuffer.append("0").append(SOH); // columns
		headerBuffer.append("0").append(SOH); // rows
		headerBuffer.insert(0, formatLength(headerBuffer.length() + 4));

		StringBuilder dataBuffer = new StringBuilder();
		dataBuffer.append(SOH);
		dataBuffer.append(messageIndex).append(SOH); // message index
		dataBuffer.append(SOH); // reserve
		dataBuffer.append(SOH); // column1
		dataBuffer.append(EOT);
		dataBuffer.insert(0, formatLength(dataBuffer.length() + 4));

		System.out.println("======" + SOH);
		System.out.println(headerBuffer.toString() + dataBuffer.toString());
		output.write((headerBuffer.toString() + dataBuffer.toString()).getBytes());
		output.flush();
	}

	public void subscribeInternal(TradeableInstrument ti) throws IOException {
		StringBuilder headerBuffer = new StringBuilder();
		int messageIndex = msgIndex.getAndIncrement();
		headerBuffer.append(SOH);
		headerBuffer.append(messageIndex).append(SOH); // message index
		headerBuffer.append(SOH); // reserve
		headerBuffer.append("0G1").append(SOH);
		headerBuffer.append("3").append(SOH); // columns
		headerBuffer.append("1").append(SOH); // rows
		headerBuffer.insert(0, formatLength(headerBuffer.length() + 4));

		StringBuilder dataBuffer = new StringBuilder();
		dataBuffer.append(SOH);
		dataBuffer.append(messageIndex).append(SOH); // message index
		dataBuffer.append(SOH); // reserve

		switch (ti.getExchange()) {
		case SHANG_HAI:
			dataBuffer.append("H").append(SOH); // Exchange
			break;
		case SHEN_ZHEN:
			dataBuffer.append("S").append(SOH); // Exchange
			break;
		case INDEX_FUTURE:
			dataBuffer.append("F").append(SOH); // Exchange
			break;
		default:
			throw new RuntimeException("unknown exchange " + ti.getExchange());
		}
		dataBuffer.append(ti.getSymbol()).append(SOH); // symbol
		dataBuffer.append(symbolToSequence.get(ti)).append(SOH); // Client side
		// sequence
		// ID
		dataBuffer.append(EOT);

		dataBuffer.insert(0, formatLength(dataBuffer.length() + 4));
		System.out.println("======" + SOH);
		System.out.println(headerBuffer.toString() + dataBuffer.toString());
		output.write((headerBuffer.toString() + dataBuffer.toString()).getBytes());
		output.flush();
	}

	public void setHost(String host) {
		String[] byteString = host.split("\\.");

		try {
			if (byteString.length == 4) {
				byte[] bytes = new byte[4];
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = intToPseudoUnsignedByte(Integer.valueOf(byteString[i]));
				}
				hostAddr = InetAddress.getByAddress(bytes);
			} else {
				hostAddr = InetAddress.getByName(host);
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	private byte intToPseudoUnsignedByte(int n) {
		if (n < 128)
			return (byte) n;
		return (byte) (n - 256);
	}

	public void setPort(int port) {
		this.port = port;
	}
}
