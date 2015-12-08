package com.dfh.tforder.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dfh.tforder.util.ConsoleUtil;
import com.dfh.tforder.util.FileUtil;

public class TradeAgent {

	private static AtomicInteger msgIndex = new AtomicInteger(1);
	private static AtomicInteger orderIndex = new AtomicInteger(1);

	public static void Delegate(Delegate delegate) {

		delegate.setVersion("1");
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd:hh:mm:ss:SSS");
		Date date = new Date();
		String msgTime = f.format(date);
		delegate.setMsgTime(msgTime);
		int MsgId = msgIndex.getAndIncrement();
		delegate.setMsgId(Integer.toString(MsgId));
		delegate.setMsg("1001");
		int Orderidx = orderIndex.getAndIncrement();
		delegate.setOrderidx(Integer.toString(Orderidx));
		delegate.setExchangeCode("F");
		// 证券代码、买卖方向由参数传入
		delegate.setOpenOrClose("O");
		// 价格、数量由参数传入

		StringBuilder delegateStr = new StringBuilder();
		delegateStr.append("01_Version@").append(delegate.getVersion());
		delegateStr.append("|02_MsgTime@").append(delegate.getMsgTime());
		delegateStr.append("|03_MsgID@").append(delegate.getMsgId());
		delegateStr.append("|04_Msg@").append(delegate.getMsg());
		delegateStr.append("|05_OrderIdx@").append(delegate.getOrderidx());
		delegateStr.append("|06_Exchange@").append(delegate.getExchangeCode());
		delegateStr.append("|07_StockCode@").append(delegate.getStockCode());
		delegateStr.append("|08_BS@").append(delegate.getBS());
		delegateStr.append("|09_OpenOrClose@").append(delegate.getOpenOrClose());
		delegateStr.append("|10_Price@").append(delegate.getPrice());
		delegateStr.append("|11_Quantity@").append(delegate.getQuantity());
		delegateStr.append("|12_OrderType@LIMIT");
		delegateStr.append("|13_note@[AccountNo=918039;CombinationNo=222;SeatNo=015609]");
		delegateStr.append("\r\n");

		FileUtil.WriteIn(delegateStr.toString());

		StringBuffer sbDelegate = new StringBuffer();
		sbDelegate.append("1001委托下单");
		sbDelegate.append("  MsgId：").append(MsgId);
		sbDelegate.append("  Orderidx：").append(Orderidx);
		sbDelegate.append("  StockCode").append(delegate.getStockCode());
		sbDelegate.append("  方向：").append(delegate.getBS());
		sbDelegate.append("  委托价：").append(delegate.getPrice());
		sbDelegate.append("  委托量：").append(delegate.getQuantity());
		ConsoleUtil.printOrder(sbDelegate.toString());
	}

	public static DelegateBack DelegateBack(String MsgId, String Orderidx) {

		DelegateBack delegateBack = new DelegateBack();
		List<String> delegateBackList = FileUtil.ReadOut(MsgId, Orderidx, "5001", "19000101:00:00:00:000");
		int listSize = delegateBackList.size();
		if (listSize == 0) {
			return null;
		}

		String delegateBackStr = delegateBackList.get(listSize - 1);
		String[] delegateBackArr = delegateBackStr.split("\\|");
		delegateBack.setVersion(delegateBackArr[0].replace("01_Version@", ""));
		delegateBack.setMsgTime(delegateBackArr[1].replace("02_MsgTime@", ""));
		delegateBack.setMsgId(delegateBackArr[2].replace("03_MsgID@", ""));
		delegateBack.setMsg(delegateBackArr[3].replace("04_Msg@", ""));
		delegateBack.setOrderidx(delegateBackArr[4].replace("05_OrderIdx@", ""));
		delegateBack.setRetCode(delegateBackArr[5].replace("06_RetCode@", ""));
		delegateBack.setOrderNo(delegateBackArr[6].replace("07_OrderNo@", ""));
		delegateBack.setOrderMsg(delegateBackArr[7].replace("08_OrderMsg@", ""));

		StringBuffer sbDelegateBack = new StringBuffer();
		sbDelegateBack.append("5001委托应答");
		sbDelegateBack.append("  MsgId：").append(MsgId);
		sbDelegateBack.append("  Orderidx：").append(Orderidx);
		sbDelegateBack.append("  RetCode：").append(delegateBack.getRetCode());
		ConsoleUtil.printOrder(sbDelegateBack.toString());

		return delegateBack;
	}

	public static void UnDelegate(UnDelegate unDelegate) {

		unDelegate.setVersion("1");
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd:hh:mm:ss:SSS");
		Date date = new Date();
		String msgTime = f.format(date);
		unDelegate.setMsgTime(msgTime);
		int MsgId = msgIndex.getAndIncrement();
		unDelegate.setMsgId(Integer.toString(MsgId));
		unDelegate.setMsg("1002");
		// Orderidx由参数传入

		StringBuilder unDelegateStr = new StringBuilder();
		unDelegateStr.append("01_Version@").append(unDelegate.getVersion());
		unDelegateStr.append("|02_MsgTime@").append(unDelegate.getMsgTime());
		unDelegateStr.append("|03_MsgID@").append(unDelegate.getMsgId());
		unDelegateStr.append("|04_Msg@").append(unDelegate.getMsg());
		unDelegateStr.append("|05_OrderIdx@").append(unDelegate.getOrderidx());
		unDelegateStr.append("\r\n");

		FileUtil.WriteIn(unDelegateStr.toString());

		StringBuffer sbUnDelegate = new StringBuffer();
		sbUnDelegate.append("1002委托撤单");
		sbUnDelegate.append("  MsgId：").append(MsgId);
		sbUnDelegate.append("  Orderidx：").append(unDelegate.getOrderidx());
		ConsoleUtil.printOrder(sbUnDelegate.toString());
	}

	public static UnDelegateBack UnDelegateBack(String MsgId, String Orderidx) {

		UnDelegateBack unDelegateBack = new UnDelegateBack();
		List<String> unDelegateBackList = FileUtil.ReadOut(MsgId, Orderidx, "5002", "19000101:00:00:00:000");
		int listSize = unDelegateBackList.size();
		if (listSize == 0) {
			return null;
		}
		String unDelegateBackStr = unDelegateBackList.get(listSize - 1);

		String[] unDelegateBackArr = unDelegateBackStr.split("\\|");
		unDelegateBack.setVersion(unDelegateBackArr[0].replace("01_Version@", ""));
		unDelegateBack.setMsgTime(unDelegateBackArr[1].replace("02_MsgTime@", ""));
		unDelegateBack.setMsgId(unDelegateBackArr[2].replace("03_MsgID@", ""));
		unDelegateBack.setMsg(unDelegateBackArr[3].replace("04_Msg@", ""));
		unDelegateBack.setOrderidx(unDelegateBackArr[4].replace("05_OrderIdx@", ""));
		unDelegateBack.setRetCode(unDelegateBackArr[5].replace("06_RetCode@", ""));
		unDelegateBack.setCancelMsg(unDelegateBackArr[6].replace("07_CancelMsg@", ""));

		StringBuffer sbUnDelegateBack = new StringBuffer();
		sbUnDelegateBack.append("5002撤单应答");
		sbUnDelegateBack.append("  MsgId：").append(MsgId);
		sbUnDelegateBack.append("  Orderidx：").append(Orderidx);
		sbUnDelegateBack.append("  RetCode：").append(unDelegateBack.getRetCode());
		ConsoleUtil.printOrder(sbUnDelegateBack.toString());

		return unDelegateBack;
	}

	public static List<UpdateDelegateStatus> UpdateDelegateStatus(String MsgId, String Orderidx, String MsgTime) {
		List<UpdateDelegateStatus> updateDelegateStatusList = new ArrayList<UpdateDelegateStatus>();
		List<String> updateDelegateStatusStrList = FileUtil.ReadOut(MsgId, Orderidx, "5003", MsgTime);
		int listSize = updateDelegateStatusStrList.size();

		for (int i = 0; i < listSize; i++) {
			UpdateDelegateStatus updateDelegateStatus = new UpdateDelegateStatus();
			String updateDelegateStatusStr = updateDelegateStatusStrList.get(i);
			String[] updateDelegateStatusArr = updateDelegateStatusStr.split("\\|");
			updateDelegateStatus.setVersion(updateDelegateStatusArr[0].replace("01_Version@", ""));
			updateDelegateStatus.setMsgTime(updateDelegateStatusArr[1].replace("02_MsgTime@", ""));
			updateDelegateStatus.setMsgId(updateDelegateStatusArr[2].replace("03_MsgID@", ""));
			updateDelegateStatus.setMsg(updateDelegateStatusArr[3].replace("04_Msg@", ""));
			updateDelegateStatus.setOrderidx(updateDelegateStatusArr[4].replace("05_OrderIdx@", ""));
			updateDelegateStatus.setStatus(updateDelegateStatusArr[5].replace("06_Status@", ""));
			updateDelegateStatus.setCompleteNum(updateDelegateStatusArr[6].replace("07_CompleteNum@", ""));
			updateDelegateStatus.setCancelNum(updateDelegateStatusArr[7].replace("08_CancelNum@", ""));
			updateDelegateStatus.setPrice(updateDelegateStatusArr[8].replace("09_Price@", ""));
			updateDelegateStatusList.add(updateDelegateStatus);

			StringBuffer sbUpdateDelegateStatus = new StringBuffer();
			sbUpdateDelegateStatus.append("5003状态更新");
			sbUpdateDelegateStatus.append("  MsgId：").append(MsgId);
			sbUpdateDelegateStatus.append("  Orderidx：").append(Orderidx);
			sbUpdateDelegateStatus.append("  Status：").append(updateDelegateStatus.getStatus());
			sbUpdateDelegateStatus.append("  当前成交价格：").append(updateDelegateStatus.getPrice());
			sbUpdateDelegateStatus.append("  当前成交量：").append(updateDelegateStatus.getCompleteNum());
			ConsoleUtil.printOrder(sbUpdateDelegateStatus.toString());
		}
		return updateDelegateStatusList;
	}
}
