package com.dfh.tforder.order;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dfh.tforder.tick.Quote;
import com.dfh.tforder.tick.QuoteFactory;
import com.dfh.tforder.util.PropertyFactory;

public class OrderFeed implements Runnable {

	private static AtomicBoolean started = new AtomicBoolean(false);

	public static Boolean getStarted() {
		return started.get();
	}

	public static AtomicBoolean running = new AtomicBoolean(false);

	public static Boolean getRunning() {
		return running.get();
	}

	public void start() {
		started.set(true);
		Thread thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		started.set(false);
	}

	@Override
	public void run() {
		while (started.get()) {
			Quote QuoteA = QuoteFactory.getQuoteA();
			Quote QuoteB = QuoteFactory.getQuoteB();
			if (QuoteA == null || QuoteB == null) {
				continue;
			}
			Properties prop = PropertyFactory.getProperties();
			String priceThresholdStr = prop.getProperty("priceThreshold");
			Float H = new Float(priceThresholdStr);
			String deltaPriceStr = prop.getProperty("deltaPrice");
			Float deltaPrice = new Float(deltaPriceStr);
			String buyBadSellGood = prop.getProperty("buyBadSellGood");
			String sellBadBuyGood = prop.getProperty("sellBadBuyGood");
			String lotThresholdStr = prop.getProperty("lotThreshold");
			float lotThreshold = new Float(lotThresholdStr);

			String codeA = QuoteA.getCode();
			String codeB = QuoteB.getCode();
			float askA = QuoteA.getAsk1Price();
			float bidA = QuoteA.getBid1Price();
			float askB = QuoteB.getAsk1Price();
			float bidB = QuoteB.getBid1Price();
			float volumeAskA = QuoteA.getAsk1Volume();
			float volumeBidA = QuoteA.getBid1Volume();
			float volumeAskB = QuoteB.getAsk1Volume();
			float volumeBidB = QuoteB.getBid1Volume();

			String codeL;
			String codeS;
			float askL;
			float bidL;
			float askS;
			float bidS;
			float volumeL;
			float volumeS;
			String directionA;
			String directionB;
			if (buyBadSellGood.equals("1")) {// L��ӦA��S��ӦB
				codeL = codeA;
				codeS = codeB;
				askL = askA;
				bidL = bidA;
				askS = askB;
				bidS = bidB;
				volumeL = volumeAskA;
				volumeS = volumeBidB;
				directionA = "B";
				directionB = "S";
			} else if (sellBadBuyGood.equals("1")) {// L��ӦB��S��ӦA
				codeL = codeB;
				codeS = codeA;
				askL = askB;
				bidL = bidB;
				askS = askA;
				bidS = bidA;
				volumeL = volumeAskB;
				volumeS = volumeBidA;
				directionB = "B";
				directionA = "S";
			} else {
				continue;
			}
			float volume = (volumeL < volumeS ? volumeL : volumeS) < lotThreshold ? (volumeL < volumeS ? volumeL : volumeS) : lotThreshold;
			if ((askL - bidS) <= H) {// �Ƿ�Ӧ���Ǽ۲���ϵ����۵�����ֵ
				running.set(true);
				// L�µ�
				Delegate delegateL = new Delegate();
				delegateL.setStockCode(codeL);
				delegateL.setBS("B");
				delegateL.setPrice(String.valueOf(askL + deltaPrice));
				delegateL.setQuantity(String.valueOf(volume));
				ImmediateThread immediateThreadL = new ImmediateThread();
				immediateThreadL.setDelegate(delegateL);
				Thread threadL = new Thread(immediateThreadL);
				threadL.start();
				// S�µ�
				Delegate delegateS = new Delegate();
				delegateS.setStockCode(codeS);
				delegateS.setBS("S");
				delegateS.setPrice(String.valueOf(bidS - deltaPrice));
				delegateS.setQuantity(String.valueOf(volume));
				ImmediateThread immediateThreadS = new ImmediateThread();
				immediateThreadS.setDelegate(delegateS);
				Thread threadS = new Thread(immediateThreadS);
				threadS.start();
				// L,S���߳̾�������������
				try {
					threadL.join();
					threadS.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				running.set(false);
			} else if ((askL - bidS) > H && (bidL - askS) < H) {// ��ֵ���ڼ۲�������֮��Ž��в𵥽���
				running.set(true);
				// A��
				Delegate delegateA = new Delegate();
				delegateA.setStockCode(codeA);
				delegateA.setBS(directionA);
				float priceA;
				if (directionA.equals("B")) {// A�۹ҵ�
					priceA = bidA;
				} else {
					priceA = askA;
				}
				delegateA.setPrice(String.valueOf(priceA));
				delegateA.setQuantity(String.valueOf(volume));
				// B��
				Delegate delegateB = new Delegate();
				delegateB.setStockCode(codeB);
				delegateB.setBS(directionB);
				float priceB;
				if (directionB.equals("B")) {// B��ֱ�ӳɽ����������A�ɽ�����¼۸�
					priceB = askB;
				} else {
					priceB = bidB;
				}
				delegateB.setPrice(String.valueOf(priceB));
				delegateB.setQuantity(String.valueOf(volume));
				PendingThread pendingThread = new PendingThread();
				pendingThread.setDelegateA(delegateA);
				pendingThread.setDelegateB(delegateB);
				Thread thread = new Thread(pendingThread);
				thread.start();
				// �߳̽�����������
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				running.set(false);
			}
		}
	}
}
