package com.heros.follow.datacenter;

import com.heros.follow.source.datas.GameData;
import com.heros.follow.source.datas.MainLineData;
import com.heros.follow.source.result.GameResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataCenter extends Observable {

	private static DataCenter dataCenter;
	private DataModel model = new DataModel();
	private SystemConfigBean C_bean;
	
	private DataCenter() {
//		for (LineType type : LineType.values()) {
//			LineDataMap.put(type, new HashMap<>());
//		}
	}

//	// 帳號資訊容器
//	private ConcurrentMap<String, accountData> accountMap = new ConcurrentHashMap<>();
//	// 水位資訊容器
//	private ConcurrentMap<String, waterData> waterMap = new ConcurrentHashMap<>();
	// 其它容器
	private ConcurrentMap<String, String> itemMap = new ConcurrentHashMap<>();
//	// 英雄站台設定
//	private ConcurrentMap<Integer, SiteSetData> SiteSetMap = new ConcurrentHashMap<>();
//	// 更新禁止名單
//	private Set<String> IdBanList = new HashSet<>();
//	// 更新禁止名單
//	private ArrayList<banlistData> IdBanReasonList = new ArrayList<>();
	
//	/*
//	 * 球探網專用容器區
//	 */
//	// 聯盟資訊 (Key = LeagueID, Value = data)
//	private ConcurrentMap<String, SingleLeagueData> SingleLeagueDataMap = new ConcurrentHashMap<>();
//	// 隊伍資訊 (Key = TeamID, Value = data)
//	private ConcurrentMap<String, SingleTeamData> SingleTeamDataMap = new ConcurrentHashMap<>();
//	// 賽事資訊 (Key = GameID, Value = data)
//	private ConcurrentMap<String, GameData> GameDataMap = new ConcurrentHashMap<>();
//	//盤口 [ Key = GameID , Value = 盤口資料群(盤口)]
//	private ConcurrentMap<String, ArrayList<MainLineData>> LineDataMap = new ConcurrentHashMap<>();
//	// 走地賽事清單 [GameID + 公司ID , 最後更新時間)]
//	private ConcurrentMap<String, Long> InplayMap = new ConcurrentHashMap<>();
//	// 組合好的GameResult (Key = FollowID, Value = data)
//	private ConcurrentMap<String, GameResult> GameResultMap = new ConcurrentHashMap<>();
//	// 現有聯盟ID
//	private Set<String> leagueList = new HashSet<>();
//	// 存放開關盤賽事
//	private ConcurrentMap<Mapkey, Set<String>> OC_LineContainer = new ConcurrentHashMap<>();
	
	// 載入所有資料
	public void LoadAll() {
//		LoadAccount();
//		LoadWater();	// 改由控端直接更新了
		LoadItem();
//		LoadSiteSetList();
//		LoadBanList();
	}

	// 載入帳號資料
	public boolean LoadAccount() {
		 int updateNum = 0;
		synchronized (accountMap) {
			updateNum = model.HandleAccountData(SendApiCenter.getSendApiCenter().sendAccountApi());
		}
		return updateNum > 0;
	}

	// 載入降水資料
	public boolean LoadWater() {
		 int updateNum = 0;
		synchronized (waterMap) {
			updateNum = model.HandleWaterData(SendApiCenter.getSendApiCenter().sendWaterApi());
		}
		return updateNum > 0;
	}

	// 載入一般資料
	public boolean LoadItem() {
		 int updateNum = 0;
		synchronized (itemMap) {
			updateNum = model.HandleItemData(SendApiCenter.getSendApiCenter().sendItemApi());
		}
		return updateNum > 0;
	}

	// 載入黑名單資料
	public boolean LoadBanList() {
		 int updateNum = 0;
		synchronized (IdBanList) {
			updateNum = model.HandleBanListData(SendApiCenter.getSendApiCenter().sendBanApi());
		}
		notifyObservers();
		return updateNum > 0;
	}
	
	// 載入站台設定資料
	public boolean LoadSiteSetList() {
		 int updateNum = 0;
		synchronized (SiteSetMap) {
			updateNum = model.HandleSiteSetListData(SendApiCenter.getSendApiCenter().sendSiteSetApi());
		}
		notifyObservers();
		return updateNum > 0;
	}
	
	public Map<String, accountData> getAccountMap() {
		return accountMap;
	}

	public Map<String, waterData> getWaterMap() {
		return waterMap;
	}

	public Map<String, String> getItemMap() {
		return itemMap;
	}

	public Set<String> getIdBanList() {
		return IdBanList;
	}
	
	public Map<Integer, SiteSetData> getSiteSetMap() {
		return SiteSetMap;
	}

	public ArrayList<banlistData> getIdBanReasonList() {
		return IdBanReasonList;
	}

	public SystemConfigBean getC_bean() {
		return C_bean;
	}
	
	public Map<String, SingleLeagueData> getSingleLeagueDataMap() {
		return SingleLeagueDataMap;
	}

	public Map<String, SingleTeamData> getSingleTeamDataMap() {
		return SingleTeamDataMap;
	}
	
	public Map<String, GameData> getGameDataMap() {
		return GameDataMap;
	}
	
	public Map<String, ArrayList<MainLineData>> getLineDataMap() {
		return LineDataMap;
	}

	public Map<String, Long> getInplayMap() {
		return InplayMap;
	}

	public Map<String, GameResult> getGameResultMap() {
		return GameResultMap;
	}
	
	public Set<String> getLeagueList() {
		return leagueList;
	}

	public Map<Mapkey, Set<String>> getOC_LineContainer() {
		return OC_LineContainer;
	}

	public static DataCenter getInstence() {
		if (dataCenter == null) {
			synchronized (DataCenter.class) {
				if (dataCenter == null) {
					dataCenter = new DataCenter();
					dataCenter.updateSetting();
				}
			}
		}
		return dataCenter;
	}

	public void toChange() {
		this.setChanged();
	}
	
	public void updateSetting() {
		this.C_bean = (SystemConfigBean) SerializableIO.getInstance().fileReader(BinFile.config);
		LineHandler.getLineHandler().updateSetting();
	}
	
}
