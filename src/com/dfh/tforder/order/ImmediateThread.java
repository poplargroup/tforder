package com.dfh.tforder.order;

import java.util.List;

import com.dfh.tforder.util.ConsoleUtil;
import com.dfh.tforder.util.Constant;

public class ImmediateThread implements Runnable {

	private Delegate delegate;

	public Delegate getDelegate() {
		return delegate;
	}

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void run() {
		TradeAgent.Delegate(delegate);
		String MsgId = delegate.getMsgId();
		String Orderidx = delegate.getOrderidx();
		float volume = Float.parseFloat(delegate.getQuantity());
		while (true) {// 读取委托返回状态
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
				TradeAgent.Delegate(delegate);
				MsgId = delegate.getMsgId();
				Orderidx = delegate.getOrderidx();
			}
		}// 读取A委托返回状态
		String MsgTime = "19000101:00:00:00:000";
		float volumeComplete = 0;
		while (true) {// 读取委托状态更新
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
			MsgTime = updateDelegateStatusList.get(listSize - 1).getMsgTime();
			if (volumeComplete == volume) {
				String priceCompleteStr = updateDelegateStatusList.get(listSize - 1).getPrice();
				float priceComplete = Math.round(new Float(priceCompleteStr)*Constant.leverage*1000)/1000;
				StringBuffer sb = new StringBuffer();
				sb.append("0000委托结束");
				sb.append("  MsgId：").append(MsgId);
				sb.append("  Orderidx：").append(Orderidx);
				sb.append("  最终成交价：").append(priceComplete);
				sb.append("  最终成交量：").append(volumeComplete);
				ConsoleUtil.printOrder(sb.toString());
				break;
			}
		}// 读取委托状态更新
	}
}
