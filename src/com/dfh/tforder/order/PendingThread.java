package com.dfh.tforder.order;

import java.util.List;
import java.util.Properties;

import com.dfh.tforder.tick.Quote;
import com.dfh.tforder.tick.QuoteFactory;
import com.dfh.tforder.util.ConsoleUtil;
import com.dfh.tforder.util.Constant;
import com.dfh.tforder.util.PropertyFactory;

public class PendingThread implements Runnable {
	private Delegate delegateA;
	private Delegate delegateB;

	public Delegate getDelegateA() {
		return delegateA;
	}

	public void setDelegateA(Delegate delegateA) {
		this.delegateA = delegateA;
	}

	public Delegate getDelegateB() {
		return delegateB;
	}

	public void setDelegateB(Delegate delegateB) {
		this.delegateB = delegateB;
	}

	@Override
	public void run() {
		Properties prop = PropertyFactory.getProperties();
		String priceThresholdStr = prop.getProperty("priceThreshold");
		Float H = new Float(priceThresholdStr);
		String buyBadSellGood = prop.getProperty("buyBadSellGood");
		String deltaPriceStr = prop.getProperty("deltaPrice");
		Float deltaPrice = new Float(deltaPriceStr);
		Quote QuoteA = QuoteFactory.getQuoteA();
		Quote QuoteB = QuoteFactory.getQuoteB();
		float askA;
		float bidA;
		float askB;
		float bidB;
		float askL;
		float bidL;
		float askS;
		float bidS;

		TradeAgent.Delegate(delegateA);
		String MsgId = delegateA.getMsgId();
		String Orderidx = delegateA.getOrderidx();
		float volume = new Float(delegateA.getQuantity());
		while (true) {// ��ȡAί�з���״̬
			try {
				Thread.sleep(Constant.waitingTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DelegateBack delegateBack = TradeAgent.DelegateBack(MsgId, Orderidx);
			if (delegateBack == null) {
				continue;
			}
			if (delegateBack.getRetCode().equals("0")) {// ί�гɹ�
				break;
			} else {// ί�в��ɹ�����ί��
				TradeAgent.Delegate(delegateA);
				MsgId = delegateA.getMsgId();
				Orderidx = delegateA.getOrderidx();
			}
		}// ��ȡAί�з���״̬
		String MsgTime = "19000101:00:00:00:000";
		float volumeComplete = 0;
		while (true) {// ��ȡAί��״̬����
			try {
				Thread.sleep(Constant.waitingTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<UpdateDelegateStatus> updateDelegateStatusList = TradeAgent.UpdateDelegateStatus(MsgId, Orderidx, MsgTime);
			int listSize = updateDelegateStatusList.size();
			if (listSize == 0) {
				continue;
			}
			String CompleteNum = updateDelegateStatusList.get(listSize - 1).getCompleteNum();
			volumeComplete = Float.parseFloat(CompleteNum);
			String status = updateDelegateStatusList.get(listSize - 1).getStatus();
			if (volumeComplete == volume || status.equals("5") || status.equals("6")) {
				String priceCompleteStr = updateDelegateStatusList.get(listSize - 1).getPrice();
				float priceComplete = Math.round(new Float(priceCompleteStr) * Constant.leverage * 1000) / 1000;
				StringBuffer sb = new StringBuffer();
				sb.append("0000ί�н���");
				sb.append("  MsgId��").append(MsgId);
				sb.append("  Orderidx��").append(Orderidx);
				sb.append("  ���ճɽ��ۣ�").append(priceComplete);
				sb.append("  ���ճɽ�����").append(volumeComplete);
				ConsoleUtil.printOrder(sb.toString());
				break;
			}
			MsgTime = updateDelegateStatusList.get(listSize - 1).getMsgTime();
			if (status.equals("3") || status.equals("4")) {
				continue;
			}
			// �µļ۲����
			QuoteA = QuoteFactory.getQuoteA();
			QuoteB = QuoteFactory.getQuoteB();
			askA = QuoteA.getAsk1Price();
			bidA = QuoteA.getBid1Price();
			askB = QuoteB.getAsk1Price();
			bidB = QuoteB.getBid1Price();
			if (buyBadSellGood.equals("1")) {// L��ӦA��S��ӦB
				askL = askA;
				bidL = bidA;
				askS = askB;
				bidS = bidB;
			} else {// L��ӦB��S��ӦA
				askL = askB;
				bidL = bidB;
				askS = askA;
				bidS = bidA;
			}
			if ((askL - bidS) <= H || (bidL - askS) >= H) {// �۲ֵ���������ڷ��𳷵�
				UnDelegate unDelegate = new UnDelegate();
				unDelegate.setOrderidx(Orderidx);
				TradeAgent.UnDelegate(unDelegate);
				String MsgIdC = unDelegate.getMsgId();
				while (true) {// ��ȡ����ί�з���״̬
					try {
						Thread.sleep(Constant.waitingTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					UnDelegateBack unDelegateBack = TradeAgent.UnDelegateBack(MsgIdC, Orderidx);
					if (unDelegateBack == null) {
						continue;
					}
					if (unDelegateBack.getRetCode().equals("0")) {// ����ί�гɹ�
						break;
					} else {// ί�в��ɹ�����ί�г���
						TradeAgent.UnDelegate(unDelegate);
						MsgIdC = unDelegate.getMsgId();
					}
				}// ��ȡ����ί�з���״̬
			}// if
		}// ��ȡί��״̬����
		// ����Aʵ�ʳɽ�����B�ּ�׷��
		delegateB.setQuantity(String.valueOf(volumeComplete));
		float priceB;
		if (buyBadSellGood.equals("1")) {
			priceB = QuoteB.getBid1Price() - deltaPrice;
		} else {
			priceB = QuoteB.getAsk1Price() + deltaPrice;
		}
		delegateB.setPrice(String.valueOf(priceB));
		ImmediateThread immediateThread = new ImmediateThread();
		immediateThread.setDelegate(delegateB);
		Thread threadB = new Thread(immediateThread);
		threadB.start();
		// �߳̽�����������
		try {
			threadB.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
