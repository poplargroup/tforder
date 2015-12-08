package com.dfh.tforder.tick;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import com.dfh.tforder.util.ConsoleUtil;
import com.dfh.tforder.util.PropertyFactory;

/*
 * A tick is considered to be complete only if lastTradeVolume, lastTradePrice, bidPrice1, bidVolume1, askPrice1, askVolume1 are present 
 */
public class LiveFeedSocketReader implements Runnable {

	// private static final Logger LOG =
	// Logger.getLogger(LiveFeedSocketReader.class);
	private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private static String today = dateFormat.format(new Date());

	// private LiveFeed liveFeed;

	@SuppressWarnings("unused")
	@Resource
	private BlockingQueue<Tick> tickQueue;

	@SuppressWarnings("unused")
	private int queueTimeoutSeconds;

	//
	// private TradeableInstrumentManager tradeableInstrumentManager;

	// Map of client sequence ID to full message
	private Map<String, String[]> fullMsgs = new HashMap<String, String[]>();

	private DataInputStream input;

	void start() {

		Thread thread = new Thread(this);
		thread.setDaemon(false);
		thread.setName("Live Feed Socket Reader");
		thread.start();
	}

	@Override
	public void run() {
		// LOG.info("Live feed socket reader thread started");
		System.out.println("Live feed socket reader thread started");
		try {
			runInternal();
		} catch (Throwable e) {
			// LOG.fatal(e, e);
			e.printStackTrace();
			System.exit(1);
		} finally {
			// LOG.info(Thread.currentThread().getName() + " died");
		}
	}

	private void runInternal() {

		byte[] lengthB = new byte[4];

		while (true) {
			try {

				// header
				input.readFully(lengthB);

				int length = Integer.valueOf(new String(lengthB).trim());

				byte[] headerBytes = new byte[length - 4];
				input.readFully(headerBytes);

				Header header = new Header();
				header.parse(length, headerBytes);

				// data
				Data dataO = new Data();
				boolean hasMore = false;
				do {
					input.readFully(lengthB);
					length = Integer.valueOf(new String(lengthB).trim());

					byte[] dataBytes = new byte[length - 4];
					input.readFully(dataBytes);

					hasMore = dataO.parse(header, length, dataBytes);

				} while (hasMore);

				process(header, dataO);
				// System.out.println(dataO.toString());

			} catch (IOException e) {
				e.printStackTrace();
				// LOG.error("Live feed read error " + e, e);
				// liveFeed.reconnect();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@SuppressWarnings("unused")
	private static String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1) + " ";
		}
		return result;
	}

	// Currently we only use 0G2 message
	private void process(Header header, Data data) throws InterruptedException {

		if (data.data.isEmpty())
			return;

		if ("0G2".equals(header.instruction)) { // stock full
			for (String[] row : data.data) {
				// fullMsgs.put(row[0], row);
				fileNewStockTick(row);
			}
		} else if ("0G3".equals(header.instruction)) { // stock update
			Set<String> symbolWithNewTicks = new HashSet<String>();
			for (String[] row : data.data) {
				String[] msg = fullMsgs.get(row[0]);
				if (msg == null) {
					// LOG.error("0G3 message references to non-existing 0G2 " +
					// row[0]);
					continue;
				}

				int column = Integer.valueOf(row[1]);
				msg[column - 1] = row[2];
				symbolWithNewTicks.add(row[0]);
			}

			for (String symbol : symbolWithNewTicks) {
				fileNewStockTick(fullMsgs.get(symbol));
			}
		} else if ("0G5".equals(header.instruction)) { // index future full
			for (String[] row : data.data) {
				// fullMsgs.put(row[0], row);
				fileNewIfTick(row);
			}
		} else if ("0G6".equals(header.instruction)) {
			Set<String> symbolWithNewTicks = new HashSet<String>();
			for (String[] row : data.data) {
				String[] msg = fullMsgs.get(row[0]);
				if (msg == null) {
					// LOG.error("0G6 message references to non-existing 0G5 " +
					// row[0]);
					continue;
				}

				int column = Integer.valueOf(row[1]);
				msg[column - 1] = row[2];
				symbolWithNewTicks.add(row[0]);

			}

			for (String symbol : symbolWithNewTicks) {
				fileNewIfTick(fullMsgs.get(symbol));
			}
		}
	}

	private void fileNewStockTick(String[] fields) throws InterruptedException {

		L1Tick result = new L1Tick();
		StringBuffer st = new StringBuffer();
		// Exchange exchange = null;
		if ("H".equals(fields[1])) {
			// exchange = Exchange.SHANG_HAI;
		} else if ("S".equals(fields[1])) {
			// exchange = Exchange.SHEN_ZHEN;
		} else if ("F".equals(fields[1])) {
			// exchange = Exchange.INDEX_FUTURE;
		} else {
			// throw new RuntimeException();
			// LOG.error("Unknown exchange code " + fields[1]);
			return;
		}
		// result.setExchange(exchange);

		result.setType(Tick.Type.LEVEL_ONE);

		// result.setInstrument(tradeableInstrumentManager.getOrCreate(fields[2],
		// exchange));
		result.setCaughtUp(true);

		try {
			result.setYesterdayClosePrice(parseFloat(fields[3]));
			result.setPrice(parseFloat(fields[9]));
			result.setTradeVolume(parseDouble(fields[8]));
			result.setRMBVolume(parseDouble(fields[5]));

			result.setAsk1Price(parseFloat(fields[20]));
			result.setAsk2Price(parseFloat(fields[21]));
			result.setAsk3Price(parseFloat(fields[22]));
			result.setAsk4Price(parseFloat(fields[23]));
			result.setAsk5Price(parseFloat(fields[24]));

			result.setAsk1Size(parseFloat(fields[25]));
			result.setAsk2Size(parseFloat(fields[26]));
			result.setAsk3Size(parseFloat(fields[27]));
			result.setAsk4Size(parseFloat(fields[28]));
			result.setAsk5Size(parseFloat(fields[29]));

			result.setBid1Price(parseFloat(fields[10]));
			result.setBid2Price(parseFloat(fields[11]));
			result.setBid3Price(parseFloat(fields[12]));
			result.setBid4Price(parseFloat(fields[13]));
			result.setBid5Price(parseFloat(fields[14]));

			result.setBid1Size(parseFloat(fields[15]));
			result.setBid2Size(parseFloat(fields[16]));
			result.setBid3Size(parseFloat(fields[17]));
			result.setBid4Size(parseFloat(fields[18]));
			result.setBid5Size(parseFloat(fields[19]));

			result.setYesterdayNAV(parseFloat(fields[31]));
			result.setNAV(parseFloat(fields[32]));

			result.setReceivedTime(new Date().getTime());
			/*
			 * fields[30] contains multiple timestamp separated by a whitespace.
			 * 0. time that the tick was generated at stock exchange 1. time
			 * that the tick is received by Tick Gateway 2. time that the tick
			 * is sent by Tick Gateway
			 */
			// System.out.println(Arrays.toString(fields));
			String[] timestamps = fields[30].split("\\s+");
			if (timestamps[0].trim().length() > 0) {
				result.setSentTime(timestampFormat.parse(today + timestamps[0]).getTime());
			} else {
				result.setSentTime(timestampFormat.parse(today + "000000000").getTime());
			}
			if (timestamps.length >= 3) {
				result.setTimestamp1(timestampFormat.parse(today + timestamps[1]).getTime());
				result.setTimestamp2(timestampFormat.parse(today + timestamps[2]).getTime());
			}

			st.append("setYesterdayClosePrice,").append(parseFloat(fields[3]));
			st.append(",setPrice,").append(parseFloat(fields[9]));
			st.append(",setTradeVolume,").append(parseDouble(fields[8]));
			st.append(",setRMBVolume,").append(parseDouble(fields[5]));

			st.append(",setAsk1Price,").append(parseFloat(fields[20]));
			st.append(",setAsk1Size,").append(parseFloat(fields[25]));
			st.append(",setAsk2Price,").append(parseFloat(fields[21]));
			st.append(",setAsk2Size,").append(parseFloat(fields[26]));
			st.append(",setAsk3Price,").append(parseDouble(fields[22]));
			st.append(",setAsk3Size,").append(parseDouble(fields[27]));
			st.append(",setAsk4Price,").append(parseDouble(fields[23]));
			st.append(",setAsk4Size,").append(parseDouble(fields[28]));
			st.append(",setAsk5Price,").append(parseDouble(fields[24]));
			st.append(",setAsk5Size,").append(parseDouble(fields[29]));

			st.append(",setBid1Price,").append(parseFloat(fields[10]));
			st.append(",setBid1Size,").append(parseFloat(fields[15]));
			st.append(",setBid2Price,").append(parseFloat(fields[11]));
			st.append(",setBid2Size,").append(parseFloat(fields[16]));
			st.append(",setBid3Price,").append(parseDouble(fields[12]));
			st.append(",setBid3Size,").append(parseDouble(fields[17]));
			st.append(",setBid4Price,").append(parseDouble(fields[13]));
			st.append(",setBid4Size,").append(parseDouble(fields[18]));
			st.append(",setBid5Price,").append(parseDouble(fields[14]));
			st.append(",setBid5Size,").append(parseDouble(fields[19]));

			st.append(",setYesterdayNAV,").append(parseDouble(fields[31]));
			st.append(",setNAV,").append(parseDouble(fields[32]));
			st.append(",setReceivedTime,").append(new Date().getTime());

			System.out.println(st.toString());

		} catch (NumberFormatException e) {
			// LOG.error(e.getMessage() + "\n" + Arrays.toString(fields), e);
			return;
			// throw new TickFormatException();
		} catch (ParseException e) {
			// throw new TickFormatException(e.getMessage() + "\n" +
			// Arrays.toString(fields), e);
			// LOG.error(e.getMessage() + "\n" + Arrays.toString(fields), e);
			return;
		}

	}

	/*
	 * [1008, F, IF1008, 2783.6000, 2792.0000, 247139341260.0000, 2803.2000,
	 * 2769.2000, 295351.0000, 2796.8000, 2796.6000, 0.0000, 0.0000, 0.0000,
	 * 0.0000, 1.0000, 0.0000, 0.0000, 0.0000, 0.0000, 2797.0000, 0.0000,
	 * 0.0000, 0.0000, 0.0000, 30.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0,
	 * IF1008, IF1008, 2782.2000, 22706.0000, 24625.0000, 2796.8000, 0.0000,
	 * 3060.4000, 2504.0000, 0.0000, 0.0000]
	 * 
	 * Tick Fields
	 * 09:15:03,IF1007,2522.8,268,202735980,0,0,0,0,0,0,0,0,0,2522.8,
	 * 2,2521.6,2,0,0,0,0,0,0,0,0,22775
	 */
	private void fileNewIfTick(String[] fields) throws InterruptedException {

		L1Tick result = new L1Tick();
		StringBuffer st = new StringBuffer();
		// result.setExchange(Exchange.INDEX_FUTURE);

		result.setType(Tick.Type.LEVEL_ONE);
		// result.setInstrument(tradeableInstrumentManager.getOrCreate(fields[2],
		// Exchange.INDEX_FUTURE));
		result.setCaughtUp(true);

		try {
			result.setYesterdayClosePrice(parseFloat(fields[3]));
			result.setPrice(parseFloat(fields[9]));
			result.setTradeVolume(parseDouble(fields[8]));
			result.setRMBVolume(parseDouble(fields[5]));

			result.setAsk1Price(parseFloat(fields[20]));
			result.setAsk2Price(parseFloat(fields[21]));
			result.setAsk3Price(parseFloat(fields[22]));
			result.setAsk4Price(parseFloat(fields[23]));
			result.setAsk5Price(parseFloat(fields[24]));

			result.setAsk1Size(parseFloat(fields[25]));
			result.setAsk2Size(parseFloat(fields[26]));
			result.setAsk3Size(parseFloat(fields[27]));
			result.setAsk4Size(parseFloat(fields[28]));
			result.setAsk5Size(parseFloat(fields[29]));

			result.setBid1Price(parseFloat(fields[10]));
			result.setBid2Price(parseFloat(fields[11]));
			result.setBid3Price(parseFloat(fields[12]));
			result.setBid4Price(parseFloat(fields[13]));
			result.setBid5Price(parseFloat(fields[14]));

			result.setBid1Size(parseFloat(fields[15]));
			result.setBid2Size(parseFloat(fields[16]));
			result.setBid3Size(parseFloat(fields[17]));
			result.setBid4Size(parseFloat(fields[18]));
			result.setBid5Size(parseFloat(fields[19]));

			result.setReceivedTime(new Date().getTime());
			/*
			 * fields[30] contains multiple timestamp separated by a whitespace.
			 * 0. time that the tick was generated at stock exchange 1. time
			 * that the tick is received by Tick Gateway 2. time that the tick
			 * is sent by Tick Gateway
			 */
			// System.out.println(Arrays.toString(fields));
			String[] timestamps = fields[30].split("\\s+");
			if (timestamps[0].trim().length() > 0) {
				result.setSentTime(timestampFormat.parse(today + timestamps[0]).getTime());
			} else {
				result.setSentTime(timestampFormat.parse(today + "000000000").getTime());
			}
			if (timestamps.length >= 3) {
				result.setTimestamp1(timestampFormat.parse(today + timestamps[1]).getTime());
				result.setTimestamp2(timestampFormat.parse(today + timestamps[2]).getTime());
			}

			st.append("代码：").append(fields[2]);
			// st.append("setYesterdayClosePrice,").append(parseFloat(fields[3]));
			st.append("  现价：").append(parseFloat(fields[9]));
			st.append("  成交量：").append(parseDouble(fields[8]));
			// st.append("  成交金额：").append(parseDouble(fields[5]));

			st.append("  卖1价：").append(parseFloat(fields[20]));
			st.append("  卖1量：").append(parseFloat(fields[25]));
			// st.append(",setAsk2Price,").append(parseFloat(fields[21]));
			// st.append(",setAsk2Size,").append(parseFloat(fields[26]));
			// st.append(",setAsk3Price,").append(parseDouble(fields[22]));
			// st.append(",setAsk3Size,").append(parseDouble(fields[27]));
			// st.append(",setAsk4Price,").append(parseDouble(fields[23]));
			// st.append(",setAsk4Size,").append(parseDouble(fields[28]));
			// st.append(",setAsk5Price,").append(parseDouble(fields[24]));
			// st.append(",setAsk5Size,").append(parseDouble(fields[29]));
			//
			st.append("  买1价：").append(parseFloat(fields[10]));
			st.append("  买1量：").append(parseFloat(fields[15]));
			// st.append(",setBid2Price,").append(parseFloat(fields[11]));
			// st.append(",setBid2Size,").append(parseFloat(fields[16]));
			// st.append(",setBid3Price,").append(parseDouble(fields[12]));
			// st.append(",setBid3Size,").append(parseDouble(fields[17]));
			// st.append(",setBid4Price,").append(parseDouble(fields[13]));
			// st.append(",setBid4Size,").append(parseDouble(fields[18]));
			// st.append(",setBid5Price,").append(parseDouble(fields[14]));
			// st.append(",setBid5Size,").append(parseDouble(fields[19]));
			//
			// st.append(",setYesterdayNAV,").append(parseDouble(fields[31]));
			// st.append(",setNAV,").append(parseDouble(fields[32]));
			// st.append(",setReceivedTime,").append(new Date().getTime());

			// 更新报价信息
			Quote quote = new Quote();
			quote.setCode(fields[2]);
			quote.setAsk1Price(parseFloat(fields[20]));
			quote.setAsk1Volume(parseFloat(fields[25]));
			quote.setBid1Price(parseFloat(fields[10]));
			quote.setBid1Volume(parseFloat(fields[15]));
			Properties prop = PropertyFactory.getProperties();
			if (fields[2].equals(prop.getProperty("liquidBad"))) {
				QuoteFactory.setQuoteA(quote);
			} else if (fields[2].equals(prop.getProperty("liquidGood"))) {
				QuoteFactory.setQuoteB(quote);
			}

			// System.out.println(st.toString());
			ConsoleUtil.printTick(st.toString());

		} catch (NumberFormatException e) {
			// throw new TickFormatException(e.getMessage() + "\n" +
			// Arrays.toString(fields), e);
			// LOG.error(e.getMessage() + "\n" + Arrays.toString(fields), e);
			return;

		} catch (ParseException e) {
			// throw new TickFormatException(e.getMessage() + "\n" +
			// Arrays.toString(fields), e);
			// LOG.error(e.getMessage() + "\n" + Arrays.toString(fields), e);
			return;
		}

	}

	private static float parseFloat(String s) {
		if (s == null || s.trim().length() == 0) {
			return 0;
		} else {
			return Float.valueOf(s);
		}
	}

	private static double parseDouble(String s) {
		if (s == null || s.trim().length() == 0) {
			return 0;
		} else {
			return Double.valueOf(s);
		}
	}

	static class Header {
		int length;
		String instruction;
		int msgIndex;
		int columns;
		int rows;

		void parse(int length, byte[] input) {
			this.length = length;

			String s = new String(input);
			String[] fields = s.split("\\x01");

			msgIndex = Integer.valueOf(fields[1]);
			instruction = fields[3];
			columns = Integer.valueOf(fields[4]);
			rows = Integer.valueOf(fields[5]);

			// System.out.println("Fields " + Arrays.asList(fields));
		}

		@Override
		public String toString() {
			return "Header [length=" + length + ", instruction=" + instruction + ", msgIndex=" + msgIndex + ", columns=" + columns + ", rows=" + rows + "]";
		}
	}

	static class Data {
		int length;
		int msgIndex;
		List<String[]> data = new ArrayList<String[]>();

		boolean parse(Header header, int length, byte[] input) {
			this.length = length;
			String s = new String(input);
			String[] fields = s.split("\\x01");

			msgIndex = Integer.valueOf(fields[1]);

			int index = 3;
			while (true) {
				if (index >= fields.length) {
					throw new RuntimeException("Data error " + index + " " + fields.length);
				}

				char nextC;
				if (fields[index].length() > 0) {
					nextC = fields[index].charAt(0);
				} else {
					nextC = ' ';
				}

				switch (nextC) {
				case LiveFeed.ETX:
					return true;
				case LiveFeed.EOT:
					return false;
				default:
					String[] row = new String[header.columns];
					for (int i = 0; i < header.columns; i++) {
						row[i] = fields[index++];
					}
					data.add(row);
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			// result.append("Data [length=" + length + ", msgIndex=" + msgIndex
			// + ", data=\n");

			for (String[] row : data) {
				result.append(Arrays.toString(row)).append("\n");
			}

			// result.append("]");

			return result.toString();
		}
	}

	void setInputStream(InputStream inputStream) {
		this.input = new DataInputStream(inputStream);
	}

	public void setQueueTimeoutSeconds(int queueTimeoutSeconds) {
		this.queueTimeoutSeconds = queueTimeoutSeconds;
	}
}
