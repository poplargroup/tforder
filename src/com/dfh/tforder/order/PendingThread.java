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
		while (true) {// 读取A委托返回状态
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
			if (delegateBack.getRetCode().equals("0")) {// 委托成功
				break;
			} else {// 委托不成功重新委托
				TradeAgent.Delegate(delegateA);
				MsgId = delegateA.getMsgId();
				Orderidx = delegateA.getOrderidx();
			}
		}// 读取A委托返回状态
		String MsgTime = "19000101:00:00:00:000";
		float volumeComplete = 0;
		while (true) {// 读取A委托状态更新
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
				sb.append("0000委托结束");
				sb.append("  MsgId：").append(MsgId);
				sb.append("  Orderidx：").append(Orderidx);
				sb.append("  最终成交价：").append(priceComplete);
				sb.append("  最终成交量：").append(volumeComplete);
				ConsoleUtil.printOrder(sb.toString());
				break;
			}
			MsgTime = updateDelegateStatusList.get(listSize - 1).getMsgTime();
			if (status.equals("3") || status.equals("4")) {
				continue;
			}
			// 新的价差计算
			QuoteA = QuoteFactory.getQuoteA();
			QuoteB = QuoteFactory.getQuoteB();
			askA = QuoteA.getAsk1Price();
			bidA = QuoteA.getBid1Price();
			askB = QuoteB.getAsk1Price();
			bidB = QuoteB.getBid1Price();
			if (buyBadSellGood.equals("1")) {// L对应A，S对应B
				askL = askA;
				bidL = bidA;
				askS = askB;
				bidS = bidB;
			} else {// L对应B，S对应A
				askL = askB;
				bidL = bidB;
				askS = askA;
				bidS = bidA;
			}
			if ((askL - bidS) <= H || (bidL - askS) >= H) {// 价差阀值不在区间内发起撤单
				UnDelegate unDelegate = new UnDelegate();
				unDelegate.setOrderidx(Orderidx);
				TradeAgent.UnDelegate(unDelegate);
				String MsgIdC = unDelegate.getMsgId();
				while (true) {// 读取撤单委托返回状态
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
					if (unDelegateBack.getRetCode().equals("0")) {// 撤单委托成功
						break;
					} else {// 委托不成功重新委托撤单
						TradeAgent.UnDelegate(unDelegate);
						MsgIdC = unDelegate.getMsgId();
					}
				}// 读取撤单委托返回状态
			}// if
		}// 读取委托状态更新
		// 根据A实际成交量及B现价追单
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
		// 线程结束才往下走
		try {
			threadB.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
