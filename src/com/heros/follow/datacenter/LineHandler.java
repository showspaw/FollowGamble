package com.heros.follow.datacenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heros.follow.source.errorlog.LineChangeData;
import com.heros.follow.source.errorlog.LogManageCenter;
import com.heros.follow.source.result.GameResult;
import com.heros.follow.source.result.LineChangeListener;
import com.heros.follow.utils.GenericEnum.*;
import com.heros.follow.utils.GenericMethod;
import com.heros.follow.utils.MyExclusionStrategy;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LineHandler extends Observable implements Observer {
	// 開關debug輸出 
	private char debug = 'N';
	
	// 放需要自動打開停押的ID 容器
	private Map<String, Map<LineName, Map<String, Long>>> NeedAutoOpenLineMap = new ConcurrentHashMap<>();
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	private static LineHandler lineHandler;
	private Gson gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).serializeNulls().create();
	private boolean FullStop;
	private boolean LiveStop;
	private boolean autoOpenLive;
	private boolean meWork;
	private long autoOpenLiveTime;
	private ReadWriteLock ResultDataLock = new ReentrantReadWriteLock();
	
	private LineHandler() {
//		Procressor.getInstance().addObserver(this);
	}


	//, Map<String, ArrayList<String>> liveStopLogList
	// 統一處理盤口格式 有變更回傳true 否則false
	public boolean handleLine(LineChangeListener lineChangeListener, String userClass, String ballclass, GameResult Exrs, GameResult Nowrs){
		
		// 若自動打開停押容器上沒有該跟盤的專用容器 則新增 ， 名稱 用 class名單為KEY
		if (!NeedAutoOpenLineMap.containsKey(userClass)) {
			NeedAutoOpenLineMap.put(userClass, new ConcurrentHashMap<>());
			for (LineName ln : LineName.values()) {
				NeedAutoOpenLineMap.get(userClass).put(ln, new ConcurrentHashMap<>());
			}
		}

		Map<String, ArrayList<String>> liveStopLogList = LogManageCenter.getInstance().getSingleLiveStopLogList(userClass);
		ArrayList<LineChangeData> lineChangeLogList = LogManageCenter.getInstance().getSingleLineChangeList(userClass);
		
		// 比較當前資料 和 上次資料
		if ( Nowrs.getZF() != Exrs.getZF() || !Nowrs.getZFOption().equals(Exrs.getZFOption()) || !Nowrs.getZFValue().equals(Exrs.getZFValue()) || !Nowrs.getZFActive().equals(Exrs.getZFActive()) || !Nowrs.getAwayZF().equals(Exrs.getAwayZF())
				|| !Nowrs.getHomeZF().equals(Exrs.getHomeZF()) || !Nowrs.getDSOption().equals(Exrs.getDSOption()) || !Nowrs.getDSValue().equals(Exrs.getDSValue()) || !Nowrs.getDSActive().equals(Exrs.getDSActive()) || !Nowrs.getAwayDS().equals(Exrs.getAwayDS())
				|| !Nowrs.getHomeDS().equals(Exrs.getHomeDS()) || !Nowrs.getAwayDE().equals(Exrs.getAwayDE()) || !Nowrs.getHomeDE().equals(Exrs.getHomeDE()) || !Nowrs.getDEActive().equals(Exrs.getDEActive()) || !Nowrs.getAwayESRE().equals(Exrs.getAwayESRE())
				|| !Nowrs.getHomeESRE().equals(Exrs.getHomeESRE()) || !Nowrs.getESREActive().equals(Exrs.getESREActive()) || !Nowrs.getAwaySD().equals(Exrs.getAwaySD()) || !Nowrs.getHomeSD().equals(Exrs.getHomeSD()) || !Nowrs.getSDActive().equals(Exrs.getSDActive())
				|| !Nowrs.getState().equals(Exrs.getState()))
		{
			// 停押判斷
			// 如果值有變動就停壓否則就打開
			boolean livestop = (Exrs.getType() != 7 && Exrs.getType() != 8 && Exrs.getType() != 9 && FullStop) || 
											((Exrs.getType() == 7 || Exrs.getType() == 8 || Exrs.getType() == 9) && LiveStop) ;
			
				long nowTime = System.currentTimeMillis();
				// 讓分
				if (Nowrs.getZF() != Exrs.getZF() || !Nowrs.getZFOption().equals(Exrs.getZFOption()) || !Nowrs.getZFValue().equals(Exrs.getZFValue()) || !Nowrs.getAwayZF().equals(Exrs.getAwayZF()) || !Nowrs.getHomeZF().equals(Exrs.getHomeZF())) {
					if (livestop) {
						Nowrs.setZFStop("N");
						// 若需要自動打開停押, 則加入打開停押容器
						if (autoOpenLive) {
							NeedAutoOpenLineMap.get(userClass).get(LineName.ZF).put(Nowrs.getFollowID(), nowTime);
							lineChangeListener.setLineChange();
//							debugOut("ZF add: %s", Nowrs.getFollowID());
							liveStopLogList.get(LineName.ZF.getName()).add(Nowrs.getFollowID());
						}
					}
					if (!Nowrs.getZFOption().equals(Exrs.getZFOption()) || !Nowrs.getZFValue().equals(Exrs.getZFValue())) {
						Nowrs.setIsZfHChange(1);
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"讓分盤口", 
								Exrs.getZFOption() + Exrs.getZFValue(),
								Nowrs.getZFOption() + Nowrs.getZFValue()
						));
					}
					
					if (!Nowrs.getAwayZF().equals(Exrs.getAwayZF())) {
						Nowrs.setIsZfOChange(1);
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"客隊讓分賠率", 
								Exrs.getAwayZF(),
								Nowrs.getAwayZF()
						));
					}
					
					if (!Nowrs.getHomeZF().equals(Exrs.getHomeZF())) {
						Nowrs.setIsZfOChange(1);
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"主隊讓分賠率", 
								Exrs.getHomeZF(),
								Nowrs.getHomeZF()
						));
					}
				}
				// 大小
				if (!Nowrs.getDSOption().equals(Exrs.getDSOption()) || !Nowrs.getDSValue().equals(Exrs.getDSValue()) || !Nowrs.getAwayDS().equals(Exrs.getAwayDS()) || !Nowrs.getHomeDS().equals(Exrs.getHomeDS())) {
					if (livestop) {
						Nowrs.setDSStop("N");
						if (autoOpenLive) {
							NeedAutoOpenLineMap.get(userClass).get(LineName.DS).put(Nowrs.getFollowID(), nowTime);
							lineChangeListener.setLineChange();
//							debugOut("DS add: %s", Nowrs.getFollowID());
							liveStopLogList.get(LineName.DS.getName()).add(Nowrs.getFollowID());
						}
					}
					
					if (!Nowrs.getDSOption().equals(Exrs.getDSOption()) || !Nowrs.getDSValue().equals(Exrs.getDSValue())) {
						Nowrs.setIsDsHChange(1);
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"大小盤口", 
								Exrs.getDSOption() + Exrs.getDSValue(),
								Nowrs.getDSOption() + Nowrs.getDSValue()
						));
					}
					
					if (!Nowrs.getAwayDS().equals(Exrs.getAwayDS())) {
						Nowrs.setIsDsOChange(1);
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"大球賠率", 
								Exrs.getAwayDS(),
								Nowrs.getAwayDS()
						));
					}
					
					if (!Nowrs.getHomeDS().equals(Exrs.getHomeDS())) {
						Nowrs.setIsDsOChange(1);
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"小球賠率", 
								Exrs.getHomeDS(),
								Nowrs.getHomeDS()
						));
					}
				}
				
				// 獨贏
				if (!Nowrs.getAwayDE().equals(Exrs.getAwayDE()) || !Nowrs.getHomeDE().equals(Exrs.getHomeDE())) {
					Nowrs.setIsDeOChange(1);
					if (livestop) {
						Nowrs.setDEStop("N");
						if (autoOpenLive) {
							NeedAutoOpenLineMap.get(userClass).get(LineName.DE).put(Nowrs.getFollowID(), nowTime);
							lineChangeListener.setLineChange();
//							debugOut("DE add: %s", Nowrs.getFollowID());
							liveStopLogList.get(LineName.DE.getName()).add(Nowrs.getFollowID());
						}
					}
					
					if (!Nowrs.getAwayDE().equals(Exrs.getAwayDE())) {
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"客隊獨贏賠率", 
								Exrs.getAwayDE(),
								Nowrs.getAwayDE()
						));
					}
					
					if (!Nowrs.getHomeDE().equals(Exrs.getHomeDE())) {
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"主隊獨贏賠率", 
								Exrs.getHomeDE(),
								Nowrs.getHomeDE()
						));
					}
				}
				// 一輸二贏
				if (!Nowrs.getAwayESRE().equals(Exrs.getAwayESRE()) || !Nowrs.getHomeESRE().equals(Exrs.getHomeESRE())) {
					Nowrs.setIsEsreOChange(1);
					if (livestop) {
						Nowrs.setESREStop("N");
						if (autoOpenLive) {
							NeedAutoOpenLineMap.get(userClass).get(LineName.ESRE).put(Nowrs.getFollowID(), nowTime);
							lineChangeListener.setLineChange();
//							debugOut("ESRE add: %s", Nowrs.getFollowID());
							liveStopLogList.get(LineName.ESRE.getName()).add(Nowrs.getFollowID());
						}
					}
					
					if (!Nowrs.getAwayESRE().equals(Exrs.getAwayESRE())) {
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"客隊一輸賠率", 
								Exrs.getAwayESRE(),
								Nowrs.getAwayESRE()
						));
					}
					
					if (!Nowrs.getHomeESRE().equals(Exrs.getHomeESRE())) {
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"主隊一輸賠率", 
								Exrs.getHomeESRE(),
								Nowrs.getHomeESRE()
						));
					}
				}
				// 單雙
				if (!Nowrs.getAwaySD().equals(Exrs.getAwaySD()) || !Nowrs.getHomeSD().equals(Exrs.getHomeSD())) {
					Nowrs.setIsSdOChange(1);
					if (livestop) {
						Nowrs.setSDStop("N");
						if (autoOpenLive) {
							NeedAutoOpenLineMap.get(userClass).get(LineName.SD).put(Nowrs.getFollowID(), nowTime);
							lineChangeListener.setLineChange();
//							debugOut("SD add: %s", Nowrs.getFollowID());
							liveStopLogList.get(LineName.SD.getName()).add(Nowrs.getFollowID());
						}
					}
					
					if (!Nowrs.getAwaySD().equals(Exrs.getAwaySD())) {
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"單賠率", 
								Exrs.getAwaySD(),
								Nowrs.getAwaySD()
						));
					}
					
					if (!Nowrs.getHomeSD().equals(Exrs.getHomeSD())) {
						lineChangeLogList.add(new LineChangeData(
								Nowrs.getFollowID(), 
								ballclass, 
								Nowrs.getLeagueName(), 
								Nowrs.getHomeTeam(), 
								Nowrs.getAwayTeam(), 
								GenericMethod.getGameTypeTitle(Nowrs.getType()),
								"雙賠率", 
								Exrs.getHomeSD(),
								Nowrs.getHomeSD()
						));
					}
				}
				// 足球獨贏和局
				if (!Nowrs.getDrawDE().equals(Exrs.getDrawDE()) && ballclass.equals("Soccer")) {
					Nowrs.setIsDrawOChange(1);
					if (livestop) {
						Nowrs.setDrawStop("N");
						if (autoOpenLive) {
							NeedAutoOpenLineMap.get(userClass).get(LineName.ESRE).put(Nowrs.getFollowID(), nowTime);
							lineChangeListener.setLineChange();
//							debugOut("ESRE add:  %s", Nowrs.getFollowID());
							liveStopLogList.get(LineName.ESRE.getName()).add(Nowrs.getFollowID());
						}
					}
					lineChangeLogList.add(new LineChangeData(
							Nowrs.getFollowID(), 
							ballclass, 
							Nowrs.getLeagueName(), 
							Nowrs.getHomeTeam(), 
							Nowrs.getAwayTeam(), 
							GenericMethod.getGameTypeTitle(Nowrs.getType()),
							"和局賠率", 
							Exrs.getDrawDE(),
							Nowrs.getDrawDE()
					));				
				}
			return true;	
		} 
		return false;
	}
	
	public static LineHandler getLineHandler() {
		if (lineHandler == null) {
			synchronized (LineHandler.class) {
				if (lineHandler == null) {
					lineHandler = new LineHandler();
					lineHandler.updateSetting();
				}
			}
		}
		return lineHandler;
	}
	
	public void updateSetting() {
//		FullStop = datamanager.getInstence().getC_bean().getFullStop().equals("Y");
//		LiveStop = datamanager.getInstence().getC_bean().getLiveStop().equals("Y");
//		autoOpenLive = datamanager.getInstence().getC_bean().getAutoOpenLive().equals("Y");
//		autoOpenLiveTime = Long.parseLong(datamanager.getInstence().getC_bean().getOpenLiveTime());
	}
	
	//  新增自動打開停押thread
//	public void setAutoOpenLiveThread(final String userClass, final String site, final String ballsclass) {
//		executor.execute(() -> {
//			try {
//				Thread.sleep(autoOpenLiveTime * 1000 + 10);
//				long delaytime = autoOpenLiveTime * 1000;
//				Map<LineName, ArrayList<String>> followId = new HashMap<>();
//				synchronized (NeedAutoOpenLineMap) {
//					long nowTime = System.currentTimeMillis();
//					NeedAutoOpenLineMap.get(userClass).entrySet().stream()
//											.filter(i -> ! i.getValue().isEmpty()) // 這盤口類別的容器不為空
//											.forEach(i -> i.getValue().entrySet().stream()	// 选代所有盤口容器
//													.filter(id -> (nowTime - id.getValue()) >  delaytime) // 每個盤口容器中要開啟停押的followid 要超過指定時間 預設15秒
//													.map(id-> id.getKey())
//													.forEach(id -> { // 放入ID
//														followId.putIfAbsent(i.getKey(), new ArrayList<>());
//														followId.get(i.getKey()).add(id);
//													}));
//
//					followId.entrySet().forEach(i->{	// 选代 ID map
//						i.getValue().forEach(id->{	// 將發送出打開停押的ID 從 容器刪除
//							NeedAutoOpenLineMap.get(userClass).get(i.getKey()).remove(id);
//						});
//
//						SendApiCenter.getSendApiCenter().openLive(site, ballsclass, i.getKey().getName(), i.getValue(), SoundPlay.OpenLive);
//						if (datamanager.getInstence().getItemMap().containsKey("RecordLiveStopLog") && datamanager.getInstence().getItemMap().get("RecordLiveStopLog").equals("1")) {
//							SendApiCenter.getSendApiCenter().LiveLingLog(i.getValue(), ballsclass, i.getKey().getName(), "Y", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowTime));
//						}
//						debugOut("此次發送出打開 %s 停押名單共 %d 筆: %s", i.getKey().getName(), i.getValue().size(), new Gson().toJsonTree(i.getValue()));
//					});
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		});
//	}

//	public void setDebugMode(char command) {
//		this.debug = command;
//	}
//
//	@Override
//	public void debugOut(Object msg, Object... args) {
//
//		if (debug == 'N' || (debug == 'E' && !(msg instanceof Exception)) || (debug == 'S' && (msg instanceof Exception)))
//			return;
//		if (msg instanceof Exception) {
//			((Exception) msg).printStackTrace();
//		} else {
//
//			if (args.length > 0)
//				System.out.printf( ((String) msg) + "\n", args);
//			else
//				System.out.println( ((String) msg));
//		}
//
//	}
//
//	private datamanager getInstence() {
//		return datamanager.getInstence();
//	}
//
	@Override
	public void update(Observable o, Object arg) {
//		if (!(arg instanceof ObserverData) || !meWork)
//			return;
//		switch (((ObserverData) arg).getObserverType()) {
//		case ComposeLine:
//			handleLine(((ObserverData) arg));
//			break;
//		case SendAllGame:
//			sendallgame();
//			break;
//		case IDremove:
//			RefreshAll(((ObserverData) arg).getEmphasisList());
//			break;
//		case IDchange:
//			BaseContentChange(((ObserverData) arg).getEmphasisList());
//			break;
//		default:
//			break;
//		}
	}
	
//	public void BaseContentChange(List<String> obj) {
//		debugOut("UI: %s", ObserverType.RefreshLineUI.name());
//		List<String > change = getInstence().getGameResultMap().entrySet().stream()
//				.filter(i->obj.contains(i.getValue().getSiteNo()))
//				.map(i->i.getKey())
//				.collect(Collectors.toList());
//		SendApiCenter.getSendApiCenter().sendClose(SiteCode.BTW.getCode(), "Soccer", new HashSet<>(change), SoundPlay.CloseHandicap);
//	}
//
//	/*
//	 *  清除組合盤口 和 通知UI變更
//	 */
//	public void RefreshAll(List<String> obj) {
//		if (obj != null) {
//			ResultDataLock.writeLock().lock();
//			try {
//				List<String> removeList = getInstence().getGameResultMap().entrySet().stream()
//						.filter(i->obj.contains(i.getValue().getSiteNo()))
//						.map(i-> i.getKey())
//						.collect(Collectors.toList());
//				removeList.forEach(i->getInstence().getGameResultMap().remove(i));
//				SendApiCenter.getSendApiCenter().sendClose(SiteCode.BTW.getCode(), "Soccer", new HashSet<>(removeList), SoundPlay.CloseLine);
//				NotifyAllObserver(new ObserverData(ObserverType.RefreshLineUI));
//				debugOut("UI: %s", ObserverType.RefreshLineUI.name());
//			} finally {
//				ResultDataLock.writeLock().unlock();
//			}
//		}
//	}
	
//	public void handleLine(ObserverData Obd) {
//		BtwConfigBean bean = (BtwConfigBean) SerializableIO.getInstance().fileReader(BinFile.btwconfig);
//		boolean refresh = false, LiveChange = false;
//		long requestTime = 0L, handleDataTime = 0L, handleLineTime = System.currentTimeMillis();
//		if (Obd.getHandleTime() != null && Obd.getHandleTime().length == 2) {
//			requestTime = Obd.getHandleTime()[0].longValue();
//			handleDataTime = Obd.getHandleTime()[1].longValue();
//		}
//
//		if (!NeedAutoOpenLineMap.containsKey(this.getClass().getName())) {
//			NeedAutoOpenLineMap.put(this.getClass().getName(), new ConcurrentHashMap<>());
//			for (LineName ln : LineName.values()) {
//				NeedAutoOpenLineMap.get(this.getClass().getName()).put(ln, new ConcurrentHashMap<>());
//			}
//		}
//
//		Map<String, GameResult> GameMap = getInstence().getGameResultMap();
//
//		// LOG - 存放有停押的ID
//		Map<String, ArrayList<String>> LiveStopLogList = LogManageCenter.getInstance().getSingleLiveStopLogList(this.getClass().getName());
//		LogManageCenter.getInstance().ClearCollection(this.getClass().getName());
//
//		// LOG - 存放有變盤的ID
//		ArrayList<LineChangeData> LineChangeLogList = LogManageCenter.getInstance().getSingleLineChangeList(this.getClass().getName());
//		LogManageCenter.getInstance().ClearLineChangeCollection(this.getClass().getName());
//		// API - 存放有變盤的資料
//		Set<GameResult> LineUpdateList = new HashSet<>();
//		// 存放這次有新增的賽事(不包含更新)
//		Set<String> CurrentInsertList = new HashSet<>();
//
//		ResultDataLock.readLock().lock();
//		Procressor.getInstance().getLineDataLock().readLock().lock();
//		try {
//			for (Entry<String, ArrayList<MainLineData>> m1 : getInstence().getLineDataMap().entrySet()) {
//				for (MainLineData mld : m1.getValue()) {
//
//					if (!getInstence().getGameDataMap().containsKey(mld.getGameID())) {
//						continue;
//					}
//					GameData gameData = Procressor.getInstance().getGameDataMapChild(mld.getGameID());
//					if (gameData == null) {
//						continue;
//					}
//					for (Entry<Integer, LineData> data : mld.getLineMap().entrySet()) {
//						if (Obd.getThisHaveChangeList() != null && !Obd.getThisHaveChangeList().isEmpty()) {
//							if (!Obd.getThisHaveChangeList().contains(data.getValue())) {
//								continue;
//							}
//						}
//						try {
//							// 過濾公司
//							if (!bean.getCompanys().get(0)) {
//								if (!bean.getCompanys().get(Integer.parseInt(mld.getCID()))) {
//									continue;
//								}
//							}
//
//							// 過濾盤口類型
//							if (!bean.getTypes().get("全部")) {
//								switch (GenericMethod.getNodeId(mld.getLineType())) {
//								case "0":
//									if (!bean.getTypes().get("全場")) continue;
//									break;
//								case "1":
//									if (!bean.getTypes().get("半場")) continue;
//									break;
//								}
//							}
//
//							// 過濾盤口序號
//							if (!bean.getLineNos().get("全部")) {
//								if (!bean.getLineNos().get(String.valueOf(data.getKey()))) {
//									continue;
//								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//						String followID = GenericMethod.ComposeID(mld.getGameID(), mld.getCID(), mld.getLineType(), data.getKey());
//
//						Object[] away, home, main;
//						GameResult gameResult;
//
//						boolean livestop = false;
//						Procressor.getInstance().getGameDataLock().readLock().lock();
//						try {
//							if (GameMap.containsKey(followID)) {
//								gameResult = GameMap.get(followID);
//							} else {
//								GameMap.put(followID, new GameResult());
//								CurrentInsertList.add(followID);
//								gameResult = GameMap.get(followID);
//								gameResult.setStartTime(GenericMethod.getTransTime(2, Long.parseLong(gameData.getGameTime())));
//								gameResult.setLeagueName(datamanager.getInstence().getSingleLeagueDataMap().get(gameData.getLeagueID()).getL_Traditional());
//								gameResult.setAwayTeam(gameData.getAwayTraditional());
//								gameResult.setHomeTeam(gameData.getHomeTraditional());
//								gameResult.setSite(SiteCode.BTW.getCode());
//								gameResult.setSiteNo(gameData.getGameID());
//								gameResult.setFollowID(followID);
//								gameResult.setCid(mld.getCID());
//								gameResult.setType(Integer.parseInt(GenericMethod.getNodeId(mld.getLineType())));
//								refresh = true;
//							}
//							int  status = Integer.parseInt(gameData.getGameStatus());
//							livestop = (status == 0 && FullStop) || (status >= 1 && status <= 3 && LiveStop);
//							gameResult.setHomeBaseLine(gameData.getHomeSocore());
//							gameResult.setAwayBaseLine(gameData.getAwaySocore());
//						} finally {
//							Procressor.getInstance().getGameDataLock().readLock().unlock();
//						}
//
//						data.getValue().getLineDataLock().readLock().lock();
//						try {
//							switch (mld.getLineType()) {
//
//							case RunLine:
//							case HalfRunLine:
//								RunLineData rd = (RunLineData) data.getValue();
//								gameResult.setZF(rd.getLine().indexOf("-") == -1? 2 : 1);
//								String option = GenericMethod.trans_BTW_SC_Option(rd.getLine().replace("-", ""));
//								home = GenericMethod.CompareLine(gameResult.getHomeZF(), rd.getHomeOdds());
//								away = GenericMethod.CompareLine(gameResult.getAwayZF(), rd.getAwayOdds());
//								main = GenericMethod.CompareLine(gameResult.getZFOption(), option);
//
//								if (!CurrentInsertList.contains(followID) && (((Boolean) main[0]).booleanValue() || ((Boolean) home[0]).booleanValue() || ((Boolean) away[0]).booleanValue())) {
//									gameResult.setUpdateZFtime(System.currentTimeMillis());
//									LineUpdateList.add(gameResult);
//									if (livestop) {
//										gameResult.setZFStop("N");
//										if (autoOpenLive) {
//											NeedAutoOpenLineMap.get(this.getClass().getName()).get(LineName.ZF).put(followID, System.currentTimeMillis());
//											debugOut("ZF add: %s", followID);
//											LiveStopLogList.get(LineName.ZF.getName()).add(followID);
//											LiveChange = true;
//										}
//									}
//
//									if (((Boolean) main[0]).booleanValue() ) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"讓球盤口",
//												gameResult.getZFOption(),
//												option
//										));
//									}
//
//									if (((Boolean) home[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"主隊讓球",
//												gameResult.getHomeZF(),
//												rd.getHomeOdds()
//										));
//									}
//
//									if (((Boolean) away[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"客隊讓分",
//												gameResult.getAwayZF(),
//												rd.getAwayOdds()
//										));
//									}
//								}
//
//								if (((Integer) home[1]).intValue() != 0) gameResult.setPSZHomeState(((Integer) home[1]).intValue());
//								if (((Integer) away[1]).intValue() != 0) gameResult.setPSZAwayState(((Integer) away[1]).intValue());
//								if (((Integer) main[1]).intValue() != 0) gameResult.setPSZState(((Integer) main[1]).intValue());
//
//								gameResult.setZFOption(option);
//								gameResult.setHomeZF(rd.getHomeOdds());
//								gameResult.setAwayZF(rd.getAwayOdds());
//								if (rd.isLineClose()) {
//									gameResult.setZFActive("N");
//								} else {
//									gameResult.setZFActive("Y");
//									gameResult.checkZFActive();
//								}
//								break;
//
//							case StandardLine:
//							case HalfStandardLine:
//								StandardLineData sd = (StandardLineData) data.getValue();
//								home = GenericMethod.CompareLine(gameResult.getHomeDE(), sd.getHomeWinOdds());
//								away = GenericMethod.CompareLine(gameResult.getAwayDE(), sd.getAwayWinOdds());
//								main = GenericMethod.CompareLine(gameResult.getDrawDE(), sd.getDrawOdds());
//
//								if (!CurrentInsertList.contains(followID) && (((Boolean) home[0]).booleanValue() || ((Boolean) away[0]).booleanValue())) {
//									gameResult.setUpdateDEtime(System.currentTimeMillis());
//									LineUpdateList.add(gameResult);
//									if (livestop) {
//										gameResult.setDEStop("N");
//										if (autoOpenLive) {
//											NeedAutoOpenLineMap.get(this.getClass().getName()).get(LineName.DE).put(followID, System.currentTimeMillis());
//											debugOut("DE add: %s", followID);
//											LiveStopLogList.get(LineName.DE.getName()).add(followID);
//											LiveChange = true;
//										}
//									}
//
//									if (((Boolean) home[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"主勝",
//												gameResult.getHomeDE(),
//												sd.getHomeWinOdds()
//										));
//									}
//
//									if (((Boolean) away[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"客勝",
//												gameResult.getAwayDE(),
//												sd.getAwayWinOdds()
//										));
//									}
//								}
//
//								if (!CurrentInsertList.contains(followID) && ((Boolean) main[0]).booleanValue()) {
//									gameResult.setUpdateDEtime(System.currentTimeMillis());
//									LineUpdateList.add(gameResult);
//									if (livestop) {
//										gameResult.setDrawStop("N");
//										if (autoOpenLive) {
//											NeedAutoOpenLineMap.get(this.getClass().getName()).get(LineName.ESRE).put(followID, System.currentTimeMillis());
//											debugOut("Draw ESRE add: %s", followID);
//											LiveStopLogList.get(LineName.ESRE.getName()).add(followID);
//											LiveChange = true;
//										}
//									}
//									LineChangeLogList.add(new LineChangeData(
//											gameResult.getFollowID(),
//											"Soccer",
//											gameResult.getLeagueName(),
//											gameResult.getHomeTeam(),
//											gameResult.getAwayTeam(),
//											GenericMethod.getGameTypeTitle(gameResult.getType()),
//											"和局",
//											gameResult.getDrawDE(),
//											sd.getDrawOdds()
//									));
//								}
//
//								if (((Integer) home[1]).intValue() != 0) gameResult.setMLHomeState(((Integer) home[1]).intValue());
//								if (((Integer) away[1]).intValue() != 0) gameResult.setMLAwayState(((Integer) away[1]).intValue());
//								if (((Integer) main[1]).intValue() != 0) gameResult.setDrawState(((Integer) main[1]).intValue());
//
//								gameResult.setDrawDE(sd.getDrawOdds());
//								gameResult.setHomeDE(sd.getHomeWinOdds());
//								gameResult.setAwayDE(sd.getAwayWinOdds());
//
//								if (sd.isLineClose()) {
//									gameResult.setDEActive("N");
//									gameResult.setDrawActive("N");
//								} else {
//									gameResult.setDEActive("Y");
//									gameResult.setDrawActive("Y");
//									gameResult.checkDrawActive();
//									gameResult.checkDEActive();
//								}
//								break;
//
//							case DSB:
//							case HalfDSB:
//								DsbData db = (DsbData) data.getValue();
//								home = GenericMethod.CompareLine(gameResult.getHomeDS(), db.getDOdds());
//								away = GenericMethod.CompareLine(gameResult.getAwayDS(), db.getSOdds());
//								main = GenericMethod.CompareLine(gameResult.getDSOption(), db.getLine());
//
//								if (!CurrentInsertList.contains(followID) && (((Boolean) main[0]).booleanValue() || ((Boolean) home[0]).booleanValue() || ((Boolean) away[0]).booleanValue())) {
//									gameResult.setUpdateDStime(System.currentTimeMillis());
//									LineUpdateList.add(gameResult);
//									if (livestop) {
//										gameResult.setDSStop("N");
//										if (autoOpenLive) {
//											NeedAutoOpenLineMap.get(this.getClass().getName()).get(LineName.DS).put(followID, System.currentTimeMillis());
//											debugOut("DS add: %s", followID);
//											LiveStopLogList.get(LineName.DS.getName()).add(followID);
//											LiveChange = true;
//										}
//									}
//
//									if (((Boolean) main[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"大小盤口",
//												gameResult.getDSOption(),
//												db.getLine()
//										));
//									}
//
//									if (((Boolean) home[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"大球盤口",
//												gameResult.getHomeDS(),
//												db.getDOdds()
//										));
//									}
//
//									if (((Boolean) away[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"小球盤口",
//												gameResult.getAwayDS(),
//												db.getSOdds()
//										));
//									}
//								}
//
//								if (((Integer) home[1]).intValue() != 0) gameResult.setTotalHomeState(((Integer) home[1]).intValue());
//								if (((Integer) away[1]).intValue() != 0) gameResult.setTotalAwayState(((Integer) away[1]).intValue());
//								if (((Integer) main[1]).intValue() != 0) gameResult.setTotalState(((Integer) main[1]).intValue());
//
//								gameResult.setDSOption(db.getLine());
//								gameResult.setHomeDS(db.getDOdds());
//								gameResult.setAwayDS(db.getSOdds());
//
//								if (db.isLineClose()) {
//									gameResult.setDSActive("N");
//								} else {
//									gameResult.setDSActive("Y");
//									gameResult.checkDSActive();
//								}
//
//								break;
//
//							case DsGoal:
//								DsGoalHtData dg = (DsGoalHtData) data.getValue();
//								home = GenericMethod.CompareLine(gameResult.getHomeSD(), dg.getSingle());
//								away = GenericMethod.CompareLine(gameResult.getAwaySD(), dg.getDouble());
//
//								if (!CurrentInsertList.contains(followID) && (((Boolean) home[0]).booleanValue() || ((Boolean) away[0]).booleanValue())) {
//									gameResult.setUpdateSDtime(System.currentTimeMillis());
//									LineUpdateList.add(gameResult);
//									if (livestop) {
//										gameResult.setSDStop("N");
//										if (autoOpenLive) {
//											NeedAutoOpenLineMap.get(this.getClass().getName()).get(LineName.SD).put(followID, System.currentTimeMillis());
//											debugOut("SD add: %s", followID);
//											LiveStopLogList.get(LineName.SD.getName()).add(followID);
//											LiveChange = true;
//										}
//									}
//
//									if (((Boolean) home[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"單",
//												gameResult.getHomeSD(),
//												dg.getSingle()
//										));
//									}
//
//									if (((Boolean) away[0]).booleanValue()) {
//										LineChangeLogList.add(new LineChangeData(
//												gameResult.getFollowID(),
//												"Soccer",
//												gameResult.getLeagueName(),
//												gameResult.getHomeTeam(),
//												gameResult.getAwayTeam(),
//												GenericMethod.getGameTypeTitle(gameResult.getType()),
//												"雙",
//												gameResult.getAwaySD(),
//												dg.getDouble()
//										));
//									}
//								}
//
//								if (((Integer) home[1]).intValue() != 0) gameResult.setSDHomeState(((Integer) home[1]).intValue());
//								if (((Integer) away[1]).intValue() != 0) gameResult.setSDAwayState(((Integer) away[1]).intValue());
//
//								gameResult.setHomeSD(dg.getSingle());
//								gameResult.setAwaySD(dg.getDouble());
//
//								if (dg.isLineClose()) {
//									gameResult.setSDActive("N");
//								} else {
//									gameResult.setSDActive("Y");
//									gameResult.checkSDActive();
//								}
//
//								break;
//
//							case Goalorder:
////								GoalorderData gg = (GoalorderData) data.getValue();
//								break;
//
//							case OtherPlay:
////								OtherPlayData op = (OtherPlayData) data.getValue();
//								break;
//
//							case InPlayLine:
//								break;
//							}
//						} finally {
//							data.getValue().getLineDataLock().readLock().unlock();
//						}
//
//						// 判斷是不是所有盤口都關了, 全關了= 整場關盤
//						boolean isAnyActive = false;
//						for (String active: Arrays.asList(gameResult.getZFActive(),
//																			gameResult.getDSActive(),
//																			gameResult.getDEActive(),
//																			gameResult.getDrawActive(),
//																			gameResult.getSDActive()))
//						{
//							if (active.equals("Y")) {
//								isAnyActive = true;
//								break;
//							}
//						}
//						if (isAnyActive)
//							gameResult.setRealStart("1");
//						else
//							gameResult.setRealStart("2");
//					}
//				}
//			}
//		} catch (Exception e) {
//			debugOut(e);
//		} finally {
//			Procressor.getInstance().getLineDataLock().readLock().unlock();
//			ResultDataLock.readLock().unlock();
//		}
//
//		handleLineTime = (System.currentTimeMillis() - handleLineTime);
//
//		debugOut("處理和組合盤口花費了 %d ms", handleLineTime);
//
//		if (LiveChange) {
//			setAutoOpenLiveThread(this.getClass().getName(), SiteCode.BTW.getCode(), "Soccer");
//			LogManageCenter.getInstance().SendLiveStopLog(this.getClass().getName(), "Soccer");
//		}
//
//		debugOut("新增: %d 筆, 變盤: %d 筆......", CurrentInsertList.size(), LineChangeLogList.size());
//
//		if (!LineUpdateList.isEmpty()) {
//			JsonArray arrayJsonData = new JsonArray();
//			LineUpdateList.stream().filter(i -> !getInstence().getIdBanList().contains(i.getSiteNo())).forEach(i->arrayJsonData.add(gson.toJsonTree(i)));
//			debugOut("送出 %d 筆資料去API", arrayJsonData.size());
//			if (requestTime == 0L || handleDataTime == 0L)
//				SendApiCenter.getSendApiCenter().requestAPI(SiteCode.BTW.getCode(), "Soccer", arrayJsonData, SoundPlay.PATH_AUDIO1UP);
//			else
//				SendApiCenter.getSendApiCenter().requestAPI(SiteCode.BTW.getCode(), "Soccer", arrayJsonData, SoundPlay.PATH_AUDIO1UP, this.getClass().getName(), requestTime, handleDataTime + handleLineTime);
//		}
////		if (bean.isAutoUpdateTableView()) {
////			if (refresh) {
////				NotifyAllObserver(new ObserverData(ObserverType.RefreshLineUI));
////				debugOut("UI: %s", ObserverType.RefreshLineUI.name());
////			} else {
////				NotifyAllObserver(new ObserverData(ObserverType.UpdateLineUI));
////				debugOut("UI: %s", ObserverType.UpdateLineUI.name());
////			}
////		}
//	}
	
//	public void sendallgame() {
//		JsonArray arrayJsonData = new JsonArray();
//		getInstence().getGameResultMap().values().stream().filter(i -> !getInstence().getIdBanList().contains(i.getSiteNo())).forEach(i->arrayJsonData.add(gson.toJsonTree(i)));
//		debugOut("送出 %d 筆資料去API", arrayJsonData.size());
//		SendApiCenter.getSendApiCenter().requestAPI(SiteCode.BTW.getCode(), "Soccer", arrayJsonData, SoundPlay.PATH_AUDIO1UP);
//	}
	
//	public void NotifyAllObserver(ObserverData observerData) {
//		setChanged();
//		notifyObservers(observerData);
//		clearChanged();
//	}
	
	public boolean isMeWork() {
		return meWork;
	}

	public void setMeWork(boolean meWork) {
		this.meWork = meWork;
	}

}
