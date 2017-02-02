package com.heros.follow.source.errorlog;

import com.heros.follow.utils.GenericEnum;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogManageCenter {
	private static LogManageCenter logManageCenter;
	private ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	// 紀錄停押
	private Map<String, Map<String, ArrayList<String>>> liveStopLogList = new ConcurrentHashMap<>();
	
	// 紀錄盤口變更
	private Map<String, ArrayList<LineChangeData>> LineChangeLogList = new ConcurrentHashMap<>();
	
	private LogManageCenter() {
		//
	}
	
//	public void SendLiveStopLog(String userClass, String ballclass) {
//		if (datamanager.getInstence().getItemMap().containsKey("RecordLiveStopLog") && datamanager.getInstence().getItemMap().get("RecordLiveStopLog").equals("1"))
//		{
//			liveStopLogList.get(userClass).entrySet().stream()
//						.filter(i->!i.getValue().isEmpty())
//						.forEach(i->SendApiCenter.getSendApiCenter().LiveLingLog(i.getValue(), ballclass, i.getKey(), "N", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())));
//		}
//		liveStopLogList.get(userClass).entrySet().forEach(i->i.getValue().clear());
//	}
//
//	public void SendLineChangeLog(ArrayList<LineChangeData> loglist, String RequestTime, String HandleTime, String SendApiTime, String Updatetime) {
//		if (datamanager.getInstence().getItemMap().containsKey("RecordLineChangeLog") && datamanager.getInstence().getItemMap().get("RecordLineChangeLog").equals("1"))
//		{
//			SendApiCenter.getSendApiCenter().UpdateLineLog(loglist, RequestTime, HandleTime, SendApiTime, Updatetime);
//		}
//	}
	
	public static LogManageCenter getInstance() {
		if (logManageCenter == null)
			synchronized (LogManageCenter.class) {
				if (logManageCenter == null)
					logManageCenter = new LogManageCenter();
			}
		return logManageCenter;
	}

	public Map<String, Map<String, ArrayList<String>>> getLiveStopLogList() {
		return liveStopLogList;
	}
	
	// 若紀錄停押log容器上沒有該跟盤的專用容器 則新增 ， 名稱 用 class名單為KEY
	public Map<String, ArrayList<String>> getSingleLiveStopLogList(String userClass) {
		if (!liveStopLogList.containsKey(userClass)) {
			liveStopLogList.put(userClass, new ConcurrentHashMap<>());
			for (GenericEnum.LineName ln : GenericEnum.LineName.values()) {
				liveStopLogList.get(userClass).put(ln.getName(), new ArrayList<>());
			}
		}
		return liveStopLogList.get(userClass);
	}
	
	// 若紀錄變盤log容器上沒有該跟盤的專用容器 則新增 ， 名稱 用 class名單為KEY
	public ArrayList<LineChangeData> getSingleLineChangeList(String userClass) {
		if (!LineChangeLogList.containsKey(userClass)) {
			LineChangeLogList.put(userClass,  new ArrayList<>());
		}
		return LineChangeLogList.get(userClass);
	}
	
	public void ClearCollection(String userClass) {
		if (liveStopLogList.containsKey(userClass))
			liveStopLogList.get(userClass).entrySet().forEach(i->i.getValue().clear());
	}

	public void ClearLineChangeCollection(String userClass) {
		if (LineChangeLogList.containsKey(userClass))
			LineChangeLogList.get(userClass).clear();
	}
}
