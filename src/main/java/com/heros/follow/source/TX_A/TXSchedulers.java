package com.heros.follow.source.TX_A;

import com.heros.follow.datacenter.StateCheckSystem;
import com.heros.follow.datas.stateData;
import com.heros.follow.utils.GenericEnum;

import javax.swing.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

// 將勾選的賽事依序執行
public class TXSchedulers implements Runnable {
	
	private final Map<String, TX_Engine> putDataMap = new ConcurrentHashMap<>();
	private final Map<String, Map<Integer, stateData>> stateDatas = new HashMap<>();
	private final TX_MainController handler;

	private Thread thread;
	
	private boolean Running = false;
	private long Number = 0;
	
	private Timer timer;
	
	public TXSchedulers(TX_MainController handler) {
		this.handler = handler;
	}
	
	public void StopThread() {
		Running = false;
	}
	
	public void removeGame(String gameName) {
		putDataMap.remove(gameName);
		stateDatas.remove(gameName);
	}
	
	public void StartThread() {
		if (thread == null || ! thread.isAlive()) {
			thread = new Thread(this);
			thread.start();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						if (thread.isAlive()) {
							Iterator<Entry<String, Map<Integer, stateData>>> itors = stateDatas.entrySet().iterator();
							while (itors.hasNext()) {
								Entry<String, Map<Integer, stateData>> e = itors.next();
								if (putDataMap.containsKey(e.getKey())) {
									Iterator<stateData> itor = e.getValue().values().iterator();
									while (itor.hasNext()) {
										stateData sd = itor.next();
										sd.setLastActionTime(System.currentTimeMillis());
										if ( ! StateCheckSystem.getInstance().getStateDatas().containsKey(sd.getIdentifier())) {
											StateCheckSystem.getInstance().setData(sd).excuteTimer(true);
										}
									}
								} else {
									itors.remove();
								}
							}
						} else {
							try {
								thread = null;
								this.cancel();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 1, 1000);
		}
	}
	
	@Override
	public void run() {
		thread = Thread.currentThread();
		Running = true;
//		statusLED.setIcon(new ImageIcon(LabelLED.class.getResource(LabelLED.HIGHGREENLIGHT)));
		while(Running) {
			try {
				// 沒東西 = 全部停止
				if (putDataMap.isEmpty()) {
					handler.stopThread();
				} else {
					Iterator<Entry<String, TX_Engine>> itrs = putDataMap.entrySet().iterator();
					while (itrs.hasNext() && Running) {
						Entry<String, TX_Engine> itr = itrs.next();
						if ( ! stateDatas.containsKey(itr.getKey())) {
							stateDatas.putIfAbsent(itr.getKey(), new HashMap<>());
							Map<Integer, stateData> m = stateDatas.get(itr.getKey());
							// 九洲一次就是全爬, 故一次放進全部
							for (int i = 0; i <= 9; i++) {
								stateData sd = new stateData(GenericEnum.SiteCode.TX, GenericEnum.SportCode.valueOf(itr.getKey()), i);
								m.put(i, sd); // 全場 - 下半走地
								// 將狀態資料放入狀態確認中心
								StateCheckSystem.getInstance().setData(sd);
								if (i == 9) StateCheckSystem.getInstance().excuteTimer(true);
							}
						}
//						handler.showMsg("第 " + ++Number + " 次連線");
						itr.getValue().run();
						if (handler.getReloginDelayTimes() != -1L && Number >= handler.getReloginDelayTimes()) {
							while ( ! handler.login()) {
								Thread.sleep(1000);
							}
//							handler.showMsg("滿 " + Number +" 次, 重登成功...");
							Number = 0;
						}
						Thread.sleep((long) (handler.getCycletime() * 1000));
					}
				}
			} catch (InterruptedException e) {
				//
			}
		}
		Running = false;
//		statusLED.setIcon(new ImageIcon(LabelLED.class.getResource(LabelLED.HIGHREDLIGHT)));
	}
	
	public void setRunDataMap(String className, TX_Engine txEngine) {
		putDataMap.put(className, txEngine);
	}
	
	public boolean isRunning() {
		return Running;
	}

	public void setRunning(boolean running) {
		Running = running;
	}

	public Map<String, TX_Engine> getPutDataMap() {
		return putDataMap;
	}

	public Thread getThread() {
		return thread;
	}

}
