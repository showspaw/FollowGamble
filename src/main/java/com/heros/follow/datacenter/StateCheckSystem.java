package com.heros.follow.datacenter;

import com.google.gson.Gson;
import com.heros.follow.datas.stateData;
import com.heros.follow.utils.GenericEnum;
import com.heros.follow.utils.log4j2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author JS
 * 定時發送資訊至控端, 確認程式是否還正常
 * 判斷stateData.lastActionTime
 * -1 代表程式可能沒正常運作, 或是未更新, 會移除資料
 * 非-1都會發送至API, 發送完會設為-1, 下次確認之
 */
public class StateCheckSystem {
	
	private static StateCheckSystem instance;
	
	private final ConcurrentHashMap<String, stateData> stateDatas = new ConcurrentHashMap<>();
	private Gson gson = new Gson();
	private Timer timer;
	
	/**
	 * 傳入 true 啟動計時器, false 為 關閉
	 */
	public void excuteTimer(boolean excute) {
		if (excute) {
			if (timer == null)
				synchronized (instance) {
					if (timer == null) {
						timer = new Timer(true);
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								excuteCheckState();
							}
						}, 1, 3 * 1000);
					}
				}
		} else {
			synchronized (instance) {
				if (timer != null) {
					try {
						timer.cancel();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						timer = null;
					}
				}
			}
		}
	}
	
	/**
	 * 執行檢查
	 * 資料中 最後動作時間非-1, 都將傳送資料至api
	 * 傳送完會設置為-1
	 * 目前預設10秒檢查一次, 設置動作時間的時間點要低於10秒
	 */
	private void excuteCheckState() {
		if (stateDatas.isEmpty()) {
			synchronized (instance) {
				if (timer != null) {
					try {
						timer.cancel();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						timer = null;
					}
				}
			}
			return;
		}
		Iterator<stateData> itor = stateDatas.values().iterator();
		ArrayList<String> oks = new ArrayList<>();
		while (itor.hasNext()) {
			stateData sd = itor.next();
			if (sd.getLastActionTime() != -1) {
				oks.add(sd.getIdentifier());
				sd.setLastActionTime(-1);
			} else {
				itor.remove();
			}
		}
		if ( ! oks.isEmpty()) {
//			SendApiCenter.getSendApiCenter().sendCheckMsgApi(gson.toJsonTree(oks));
			log4j2.getInstance().setLog(GenericEnum.Logger.SystemRecorder.name(),gson.toJson(gson.toJsonTree(oks)));
		}
	}
	
	public StateCheckSystem setData(stateData sd) {
		stateDatas.put(sd.getIdentifier(), sd);
		return this;
	}
	
	public static final StateCheckSystem getInstance() {
		if (instance == null) {
			synchronized (StateCheckSystem.class) {
				if (instance == null) {
					instance = new StateCheckSystem();
				}
			}
		}
		return instance;
	}

	public ConcurrentHashMap<String, stateData> getStateDatas() {
		return stateDatas;
	}
	
}
