package com.heros.follow.source.result;



import com.heros.follow.utils.annotation.NotJsonData;

import java.util.ArrayList;
import java.util.List;

/*package hero.follow.result - Data Bean{
	GameResult.java - 場次的Data Bean*/
public class GameResult implements Cloneable,GameResultDataListener
{
	@NotJsonData
	private String followIdUrl;

	@NotJsonData private boolean isopen = false;
	@NotJsonData private String OpenState = "N";

	@NotJsonData private int No = 0;
	@NotJsonData private String Site = "";
	@NotJsonData private String SiteNo = "";
	@NotJsonData private String Cid;
	
	private String Source;	// 原SiteNo
	private String RotA;	// 主隊rot id
	private String RotB;	// 客隊rot id
	
	private String FollowID = "";
	private String OpenID = "";
	private String State = "N"; //關盤變數,y=關盤
	private String StartTime = "";
	private String LeagueName = "";
	private String ViewLeagueName = "";
	private String HomeTeam = "";
	private String ViewHomeTeam = "";
	private String AwayTeam = "";
	private String ViewAwayTeam = "";
	private int Type = 0;
	private String HomeBaseLine = "0";
	private String AwayBaseLine = "0";
	private String DrawDE = "0";
	private int ZF = 0;
	private String ZFOption = "0";
	private String ZFValue = "0";
	private String HomeZF = "0";
	private String AwayZF = "0";
	private String DSOption = "0";
	private String DSValue = "0";
	private String HomeDS = "0";
	private String AwayDS = "0";
	private String HomeDE = "0";
	private String AwayDE = "0";
	private String HomeESRE = "0";
	private String AwayESRE = "0";
	private String HomeSD = "0";
	private String AwaySD = "0";
	private String UpdateTime = "";
	private String ZFStop = "Y";
	private String DSStop = "Y";
	private String DEStop = "Y";
	private String ESREStop = "Y";
	private String SDStop = "Y";
	private String DrawStop = "Y";
	private String ZFActive = "N";
	private String DSActive = "N";
	private String DEActive = "N";
	private String ESREActive = "N";
	private String SDActive = "N";
	private String DrawActive = "N";
	
	// 0 = 沒變盤 1 = 變盤
	private int isZfHChange = 0; // 讓分盤口變更
	private int isZfOChange = 0; // 讓分賠率變更
	private int isDsHChange = 0; // 大小盤口
	private int isDsOChange = 0; // 大小賠率
	private int isDeOChange = 0; // 獨贏賠率
	private int isSdOChange = 0; // 單雙賠率
	private int isEsreOChange = 0; // 一輸二贏賠率
	private int isDrawOChange = 0; // 和局賠率
	private List<String> followIds = new ArrayList<>();

	public List<String> getFollowIds() {
		return followIds;
	}

	public void setFollowIds(List<String> followIds) {
		this.followIds = followIds;
	}

	//	private String
//->
	private String iSdbs = "1";
	/*
	 * 1/24加入此項目之後採用這變數直接開關盤, 減少另外發出開關盤狀況 增加效能
	 */
	private String RealStart = "0"; // 0 = 什麼都不做 1 = 開盤 2 = 跟盤
	/*
	 * 跟盤變盤時間
	 */
	@NotJsonData private long UpdateZFtime;
	@NotJsonData private long UpdateDStime;
	@NotJsonData private long UpdateDEtime;
	@NotJsonData private long UpdateESREtime;
	@NotJsonData private long UpdateSDtime;
	
	//**** UI Flag , 0=black,1=red,2=green,3=blue (正常,高,低,有變化)
	@NotJsonData private int PSZState = 0;
	@NotJsonData private int PSZHomeState = 0;
	@NotJsonData private int PSZAwayState = 0;
	@NotJsonData private int TotalState = 0;
	@NotJsonData private int TotalHomeState = 0;
	@NotJsonData private int TotalAwayState = 0;
	@NotJsonData private int MLHomeState = 0;
	@NotJsonData private int MLAwayState = 0;
	@NotJsonData private int PSState = 0;
	@NotJsonData private int PSHomeState = 0;
	@NotJsonData private int PSAwayState = 0;
	@NotJsonData private int SDHomeState = 0;
	@NotJsonData private int SDAwayState = 0;
	@NotJsonData private int DrawState = 0;

	//Donbest Field
	@NotJsonData private String Status = "";
	@NotJsonData private String Period = "";
	@NotJsonData private String LeagueID = "";
	private String EventID = "";
	@NotJsonData private String SportsBookID = "";
	@NotJsonData private long milliSecondTime;

	public GameResult(){

	}
	public Object clone(){
        try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
    }
	//**** DataManage Flog
//	@NotJsonData private DonBestLocalDataManager.Method method = DonBestLocalDataManager.Method.INSERT;

	// Set and Get FollowIDUrl
	public String getFollowIdUrl() {
		return followIdUrl;
	}
	public void setFollowIdUrl(String followIdUrl) {
		this.followIdUrl = followIdUrl;
	}
/*	
	public boolean isCheckCell() {
		return checkCell;
	}
	public void setCheckCell(boolean checkCell) {
		this.checkCell = checkCell;
	}
*/
	
	public String getISdbs() {
		return iSdbs;
	}
	public void setISdbs(String iSdbs) {
		this.iSdbs = iSdbs;
	}
	public String getViewHomeTeam() {
		return ViewHomeTeam;
	}
	public void setViewHomeTeam(String viewHomeTeam) {
		ViewHomeTeam = viewHomeTeam;
	}
	public String getViewAwayTeam() {
		return ViewAwayTeam;
	}
	public void setViewAwayTeam(String viewAwayTeam) {
		ViewAwayTeam = viewAwayTeam;
	}
	public String getViewLeagueName() {
		return ViewLeagueName;
	}
	public void setViewLeagueName(String viewLeagueName) {
		ViewLeagueName = viewLeagueName;
	}
//	public DonBestLocalDataManager.Method getMethod() {
//		return method;
//	}
//	public void setMethod(DonBestLocalDataManager.Method method) {
//		this.method = method;
//	}
	public int getPSZState() {
		return PSZState;
	}
	public void setPSZState(int pSZState) {
		PSZState = pSZState;
	}
	public int getPSZHomeState() {
		return PSZHomeState;
	}
	public void setPSZHomeState(int pSZHomeState) {
		PSZHomeState = pSZHomeState;
	}
	public int getPSZAwayState() {
		return PSZAwayState;
	}
	public void setPSZAwayState(int pSZAwayState) {
		PSZAwayState = pSZAwayState;
	}
	public int getTotalState() {
		return TotalState;
	}
	public void setTotalState(int totalState) {
		TotalState = totalState;
	}
	public int getTotalHomeState() {
		return TotalHomeState;
	}
	public void setTotalHomeState(int totalHomeState) {
		TotalHomeState = totalHomeState;
	}
	public int getTotalAwayState() {
		return TotalAwayState;
	}
	public void setTotalAwayState(int totalAwayState) {
		TotalAwayState = totalAwayState;
	}
	public int getMLHomeState() {
		return MLHomeState;
	}
	public void setMLHomeState(int mLHomeState) {
		MLHomeState = mLHomeState;
	}
	public int getMLAwayState() {
		return MLAwayState;
	}
	public void setMLAwayState(int mLAwayState) {
		MLAwayState = mLAwayState;
	}
	public int getPSState() {
		return PSState;
	}
	public void setPSState(int pSState) {
		PSState = pSState;
	}
	public int getPSHomeState() {
		return PSHomeState;
	}
	public void setPSHomeState(int pSHomeState) {
		PSHomeState = pSHomeState;
	}
	public int getPSAwayState() {
		return PSAwayState;
	}
	public void setPSAwayState(int pSAwayState) {
		PSAwayState = pSAwayState;
	}
	public int getSDHomeState() {
		return SDHomeState;
	}
	public void setSDHomeState(int sDHomeState) {
		SDHomeState = sDHomeState;
	}
	public int getSDAwayState() {
		return SDAwayState;
	}
	public void setSDAwayState(int sDAwayState) {
		SDAwayState = sDAwayState;
	}
	public String getEventID() {
		return EventID;
	}
	public int getDrawState() {
		return DrawState;
	}
	public void setDrawState(int drawState) {
		DrawState = drawState;
	}
	public void setEventID(String eventID) {
		EventID = eventID;
	}
	public String getSportsBookID() {
		return SportsBookID;
	}
	public void setSportsBookID(String sportsBookID) {
		SportsBookID = sportsBookID;
	}
	public String getLeagueID() {
		return LeagueID;
	}
	public void setLeagueID(String leagueID) {
		LeagueID = leagueID;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getPeriod() {
		return Period;
	}

	public void setPeriod(String period) {
		Period = period;
	}

	public long getMilliSecondTime() {
		return milliSecondTime;
	}

	public void setMilliSecondTime(long milliSecondTime) {
		this.milliSecondTime = milliSecondTime;
	}

	public boolean isIsopen() {
		return isopen;
	}

	public void setIsopen(boolean isopen) {
		this.isopen = isopen;
	}

	public int getNo() {
		return No;
	}

	public void setNo(int no) {
		No = no;
	}
	public String getSite() {
		return Site;
	}
	public void setSite(String site) {
		Site = site;
	}
	public String getSiteNo() {
		return SiteNo;
	}
	public void setSiteNo(String siteNo) {
		SiteNo = siteNo;
	}
	/****************************/
	/****************************/
	/****************************/
	/****************************/
	/****************************/
	public String getRotA() {
		return RotA;
	}
	public void setRotA(String rotA) {
		RotA = rotA;
	}
	public String getRotB() {
		return RotB;
	}
	public void setRotB(String rotB) {
		RotB = rotB;
	}		
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	
	
	public String getFollowID() {
		return FollowID;
	}
	public void setFollowID(String followID) {
		FollowID = followID;
	}
	public String getOpenID() {
		return OpenID;
	}
	public void setOpenID(String openID) {
		OpenID = openID;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getLeagueName() {
		return LeagueName;
	}
	public void setLeagueName(String leagueName) {
		LeagueName = leagueName;
	}
	public String getHomeTeam() {
		return HomeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		HomeTeam = homeTeam;
	}
	public String getAwayTeam() {
		return AwayTeam;
	}
	public void setAwayTeam(String awayTeam) {
		AwayTeam = awayTeam;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getHomeBaseLine() {
		return HomeBaseLine;
	}
	public void setHomeBaseLine(String homeBaseLine) {
		HomeBaseLine = homeBaseLine;
	}
	public String getAwayBaseLine() {
		return AwayBaseLine;
	}
	public void setAwayBaseLine(String awayBaseLine) {
		AwayBaseLine = awayBaseLine;
	}
	public String getDrawDE() {
		return DrawDE;
	}
	public void setDrawDE(String drawDE) {
		DrawDE = drawDE;
	}
	public int getZF() {
		return ZF;
	}
	public void setZF(int zF) {
		ZF = zF;
	}
	public String getZFOption() {
		return ZFOption;
	}
	public void setZFOption(String zFOption) {
		ZFOption = zFOption;
	}
	public String getZFValue() {
		return ZFValue;
	}
	public void setZFValue(String zFValue) {
		ZFValue = zFValue;
	}
	public String getHomeZF() {
		return HomeZF;
	}
	public void setHomeZF(String homeZF) {
		HomeZF = homeZF;
	}
	public String getAwayZF() {
		return AwayZF;
	}
	public void setAwayZF(String awayZF) {
		AwayZF = awayZF;
	}
	public String getDSOption() {
		return DSOption;
	}
	public void setDSOption(String dSOption) {
		DSOption = dSOption;
	}
	public String getDSValue() {
		return DSValue;
	}
	public void setDSValue(String dSValue) {
		DSValue = dSValue;
	}
	public String getHomeDS() {
		return HomeDS;
	}
	public void setHomeDS(String homeDS) {
		HomeDS = homeDS;
	}
	public String getAwayDS() {
		return AwayDS;
	}
	public void setAwayDS(String awayDS) {
		AwayDS = awayDS;
	}
	public String getHomeDE() {
		return HomeDE;
	}
	public void setHomeDE(String homeDE) {
		HomeDE = homeDE;
	}
	public String getAwayDE() {
		return AwayDE;
	}
	public void setAwayDE(String awayDE) {
		AwayDE = awayDE;
	}
	public String getHomeESRE() {
		return HomeESRE;
	}
	public void setHomeESRE(String homeESRE) {
		HomeESRE = homeESRE;
	}
	public String getAwayESRE() {
		return AwayESRE;
	}
	public void setAwayESRE(String awayESRE) {
		AwayESRE = awayESRE;
	}
	public String getHomeSD() {
		return HomeSD;
	}
	public void setHomeSD(String homeSD) {
		HomeSD = homeSD;
	}
	public String getAwaySD() {
		return AwaySD;
	}
	public void setAwaySD(String awaySD) {
		AwaySD = awaySD;
	}
	public String getUpdateTime() {
		return UpdateTime;
	}
	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}
	public String getZFStop() {
		return ZFStop;
	}
	public void setZFStop(String zFStop) {
		ZFStop = zFStop;
	}
	public String getDSStop() {
		return DSStop;
	}
	public void setDSStop(String dSStop) {
		DSStop = dSStop;
	}
	public String getDEStop() {
		return DEStop;
	}
	public void setDEStop(String dEStop) {
		DEStop = dEStop;
	}
	public String getESREStop() {
		return ESREStop;
	}
	public void setESREStop(String eSREStop) {
		ESREStop = eSREStop;
	}
	public String getSDStop() {
		return SDStop;
	}
	public void setSDStop(String sDStop) {
		SDStop = sDStop;
	}
	public String getDrawStop() {
		return DrawStop;
	}
	public void setDrawStop(String drawStop) {
		DrawStop = drawStop;
	}
	public String getZFActive() {
		return ZFActive;
	}
	public void setZFActive(String zFActive) {
		ZFActive = zFActive;
	}
	public String getDSActive() {
		return DSActive;
	}
	public void setDSActive(String dSActive) {
		DSActive = dSActive;
	}
	public String getDEActive() {
		return DEActive;
	}
	public void setDEActive(String dEActive) {
		DEActive = dEActive;
	}
	public String getESREActive() {
		return ESREActive;
	}
	public void setESREActive(String eSREActive) {
		ESREActive = eSREActive;
	}
	public String getSDActive() {
		return SDActive;
	}
	public void setSDActive(String sDActive) {
		SDActive = sDActive;
	}
	public String getDrawActive() {
		return DrawActive;
	}
	public void setDrawActive(String drawActive) {
		DrawActive = drawActive;
	}
	public String getOpenState() {
		return OpenState;
	}
	public void setOpenState(String openState) {
		OpenState = openState;
	}
	public String getCid() {
		return Cid;
	}
	public void setCid(String cid) {
		Cid = cid;
	}
//	public String getiSdbs() {
//		return iSdbs;
//	}
//	public void setiSdbs(String iSdbs) {
//		this.iSdbs = iSdbs;
//	}
	public long getUpdateZFtime() {
		return UpdateZFtime;
	}
	public void setUpdateZFtime(long updateZFtime) {
		UpdateZFtime = updateZFtime;
	}
	public long getUpdateDStime() {
		return UpdateDStime;
	}
	public void setUpdateDStime(long updateDStime) {
		UpdateDStime = updateDStime;
	}
	public long getUpdateDEtime() {
		return UpdateDEtime;
	}
	public void setUpdateDEtime(long updateDEtime) {
		UpdateDEtime = updateDEtime;
	}
	public long getUpdateESREtime() {
		return UpdateESREtime;
	}
	public void setUpdateESREtime(long updateESREtime) {
		UpdateESREtime = updateESREtime;
	}
	public long getUpdateSDtime() {
		return UpdateSDtime;
	}
	public void setUpdateSDtime(long updateSDtime) {
		UpdateSDtime = updateSDtime;
	}
	public String getRealStart() {
		return RealStart;
	}
	public void setRealStart(String realStart) {
		RealStart = realStart;
	}
	public int getIsZfHChange() {
		return isZfHChange;
	}
	public void setIsZfHChange(int isZfHChange) {
		this.isZfHChange = isZfHChange;
	}
	public int getIsZfOChange() {
		return isZfOChange;
	}
	public void setIsZfOChange(int isZfOChange) {
		this.isZfOChange = isZfOChange;
	}
	public int getIsDsHChange() {
		return isDsHChange;
	}
	public void setIsDsHChange(int isDsHChange) {
		this.isDsHChange = isDsHChange;
	}
	public int getIsDsOChange() {
		return isDsOChange;
	}
	public void setIsDsOChange(int isDsOChange) {
		this.isDsOChange = isDsOChange;
	}
	public int getIsDeOChange() {
		return isDeOChange;
	}
	public void setIsDeOChange(int isDeOChange) {
		this.isDeOChange = isDeOChange;
	}
	public int getIsSdOChange() {
		return isSdOChange;
	}
	public void setIsSdOChange(int isSdOChange) {
		this.isSdOChange = isSdOChange;
	}
	public int getIsEsreOChange() {
		return isEsreOChange;
	}
	public void setIsEsreOChange(int isEsreOChange) {
		this.isEsreOChange = isEsreOChange;
	}
	public int getIsDrawOChange() {
		return isDrawOChange;
	}
	public void setIsDrawOChange(int isDrawOChange) {
		this.isDrawOChange = isDrawOChange;
	}
	public String getiSdbs() {
		return iSdbs;
	}
	public void setiSdbs(String iSdbs) {
		this.iSdbs = iSdbs;
	}
	public void invert (){
		//Event Info
		invertInfo();
		//讓分
		invertZF();
		//獨贏
		invertDE();
		//一輸
		invertESRE();
		// 大小
		//invertDS();
		//單雙
//		inverSD();
	}
	private void inverSD()
	{
		// 2015-07-31 se
		String sSD = getAwaySD();
		setAwaySD(getHomeSD());
		setHomeSD(sSD);
	}
	private void invertDS()
	{
		// 2015-07-31 se
		String sDS = getAwayDS();
		setAwayDS(getHomeDS());
		setHomeDS(sDS);
	}
	private void invertInfo(){
		String ateam = getAwayTeam(), viewateam = getViewAwayTeam();
		setViewAwayTeam(getViewHomeTeam());
		setAwayTeam(getHomeTeam());
		setHomeTeam(ateam);
		setViewHomeTeam(viewateam);
	}
	private void invertZF(){
		setZF(getZF()==1?2:1);
		String aLine = getAwayZF();
		setAwayZF(getHomeZF());
		setHomeZF(aLine);
	}
	private void invertDE(){
		String aLine = getAwayDE();
		setAwayDE(getHomeDE());
		setHomeDE(aLine);
	}
	private void invertESRE(){
		String aLine = getAwayESRE();
		setAwayESRE(getHomeESRE());
		setHomeESRE(aLine);
	}
	public void checkZFLine() throws NumberFormatException{
		int base = Integer.parseInt(getZFValue());
		if (base >= 90 && base <=99 && getZFOption().equals("1")) {
			setDEActive("N");
			setESREActive("N");
		}
	}

	public void checkZFActive(){
		try {
			if (Float.parseFloat(getAwayZF()) <= 0 || Float.parseFloat(getHomeZF()) <= 0) {
				setZFActive("N");
				setAwayZF("0");
				setHomeZF("0");
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			setZFActive("N");
		}
	}
	public void checkDSActive(){
		try {
			if (Float.parseFloat(getAwayDS()) <= 0 || Float.parseFloat(getHomeDS()) <= 0) {
				setDSActive("N");
				setAwayDS("0");
				setHomeDS("0");
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			setDSActive("N");
		}
	}
	public void checkDEActive(){
		try {
			if (Float.parseFloat(getAwayDE()) <= 0 || Float.parseFloat(getHomeDE()) <= 0) {
				setDEActive("N");
				setAwayDE("0");
				setHomeDE("0");
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			setDEActive("N");
		}
	}
	public void checkSDActive(){
		try {
			if (Float.parseFloat(getAwaySD()) <= 0 || Float.parseFloat(getHomeSD()) <= 0) {
				setSDActive("N");
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			setSDActive("N");
		}
	}
	public void checkESREActive(){
		try {
			if (Float.parseFloat(getAwayESRE()) <= 0 || Float.parseFloat(getHomeESRE()) <= 0) {
				setESREActive("N");
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			setESREActive("N");
		}
	}
	public void checkDrawActive(){
		try {
			if (Float.parseFloat(getDrawDE()) <= 0) {
				setDrawActive("N");
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			setDrawActive("N");
		}
	}

	public void checkActive() {
		checkZFActive();
		checkDSActive();
		checkDEActive();
		checkESREActive();
		checkSDActive();
		checkDrawActive();
	}
	
	public void allDown(){
		setZFActive("N");
		setDSActive("N");
		setDEActive("N");
		setSDActive("N");
		setESREActive("N");
		setDrawActive("N");
	}
	
	public void checkZFLineToCloseDEandESRE(){
		if ((getZFOption().equals("2") && Integer.parseInt(getZFValue()) >= 80)
			|| (getZFOption().equals("1") && Integer.parseInt(getZFValue()) <= -80)) {
			setESREActive("N");
		}else if(getZFOption().equals("1") && Integer.parseInt(getZFValue()) >= 80){
			setDEActive("N");
		}
	}
	
	public String getSearchQueryByFollowID(String TableName){
		if (TableName.equals("")) {
			return "";
		}
		String Query = 	" select * From " + TableName
						+ " Where FollowID = '" + getFollowID() + "';";
		return Query;
	}
	public String getSearchQuery(String TableName){
		String Query = 	" select * From " + TableName
						+ " Where Site = '" + getSite() + "'"
						+ " and SiteNo ='"+ getSiteNo() + "' ; ";
		return Query;
	}
	public String getSearchQueryUseTeamName(String TableName){
		String Query = 	" select * From " + TableName
						+ " Where Site = '" + getSite() + "'"
						+ " and StartTime ='"+ getStartTime() + "'"
						+ " and HomeTeam ='"+ getHomeTeam() + "'"
						+ " and AwayTeam ='"+ getAwayTeam() + "'"
						+ " and Type = " + String.valueOf(getType()) + " ; ";
		return Query;
	}
	public String getUpdateFollowIDQuery(String TableName){
		String Query = 	"Update " + TableName + " set FollowID = '" + getFollowID() + "'"
						+ " Where Site = '" + getSite() + "'"
						+ " and SiteNo ='"+ getSiteNo() + "' ;";
		return Query;
	}
	public String getInsertQuery(String TableName){
		String colSC = "";
		String valSC = "";
		if(TableName.equals("Soccer")){
			colSC = "DrawDE,HomeBaseLine,AwayBaseLine,";
			valSC = getDrawDE() + "','" + getHomeBaseLine() + "','" + getAwayBaseLine() + "','";
		}
		String Query= 	"Insert into " + TableName + "( ViewLeagueName,ViewAwayTeam,ViewHomeTeam,ZFActive,DSActive,DEActive,ESREActive,SDActive,DrawActive,State,OpenID,FollowID,Site,SiteNo,StartTime,LeagueName,HomeTeam,AwayTeam,Type,ZF," + colSC + "ZFOption,ZFValue,AwayZF,HomeZF,DSOption,DSValue,AwayDS,HomeDS,AwayDE,HomeDE,AwayESRE,HomeESRE,AwaySD,HomeSD,UpdateTime) "
						+ " Values ('"
						+ getViewLeagueName()		+ "','"
						+ getViewAwayTeam()			+ "','"
						+ getViewHomeTeam()			+ "','"
						+ getZFActive()				+ "','"
						+ getDSActive()				+ "','"
						+ getDEActive()				+ "','"
						+ getESREActive()			+ "','"
						+ getSDActive()				+ "','"
						+ getDrawActive()			+ "','"
						+ getState()				+ "','"
						+ getOpenID()				+ "','"
						+ getFollowID()				+ "','"
						+ getSite()					+ "','"
						+ getSiteNo()				+ "','"
						+ getStartTime()  			+ "','"
						+ getLeagueName()  			+ "','"
						+ getHomeTeam()  			+ "','"
						+ getAwayTeam() 			+ "',"
						+ String.valueOf(getType()) + ","
						+ String.valueOf(getZF()) 	+ ",'"
						+ valSC
						+ getZFOption() 			+ "','"
						+ getZFValue() 				+ "','"
						+ getAwayZF() 				+ "','"
						+ getHomeZF() 				+ "','"
						+ getDSOption() 			+ "','"
						+ getDSValue() 				+ "','"
						+ getAwayDS() 				+ "','"
						+ getHomeDS() 				+ "','"
						+ getAwayDE() 				+ "','"
						+ getHomeDE() 				+ "','"
						+ getAwayESRE() 			+ "','"
						+ getHomeESRE() 			+ "','"
						+ getAwaySD() 				+ "','"
						+ getHomeSD() 				+ "',"
						+ " datetime('now', 'localtime'));";
		return Query;
	}
	public String getInsertRecordQuery(String TableName){
		String colSC = "";
		String valSC = "";
		if(TableName.equals("SoccerRecord")){
			colSC = "DrawDE,HomeBaseLine,AwayBaseLine,";
			valSC = getDrawDE() + "','" + getHomeBaseLine() + "','" + getAwayBaseLine() + "','";
		}
		String Query= 	"Insert into " + TableName + "( Site,SiteNo,StartTime,LeagueName,HomeTeam,AwayTeam,Type,ZF," + colSC + "ZFOption,ZFValue,AwayZF,HomeZF,DSOption,DSValue,AwayDS,HomeDS,AwayDE,HomeDE,AwayESRE,HomeESRE,AwaySD,HomeSD,UpdateTime) "
						+ " Values ('"
						+ getSite()					+ "','"
						+ getSiteNo()				+ "','"
						+ getStartTime()  			+ "','"
						+ getLeagueName()  			+ "','"
						+ getHomeTeam()  			+ "','"
						+ getAwayTeam() 			+ "',"
						+ String.valueOf(getType()) + ","
						+ String.valueOf(getZF()) 	+ ",'"
						+ valSC
						+ getZFOption() 			+ "','"
						+ getZFValue() 				+ "','"
						+ getAwayZF() 				+ "','"
						+ getHomeZF() 				+ "','"
						+ getDSOption() 			+ "','"
						+ getDSValue() 				+ "','"
						+ getAwayDS() 				+ "','"
						+ getHomeDS() 				+ "','"
						+ getAwayDE() 				+ "','"
						+ getHomeDE() 				+ "','"
						+ getAwayESRE() 			+ "','"
						+ getHomeESRE() 			+ "','"
						+ getAwaySD() 				+ "','"
						+ getHomeSD() 				+ "',"
						+ " datetime('now', 'localtime'));";
		return Query;
	}
	public String getUpdateQuery(String TableName){
		String valSC = "";
		if(TableName.equals("Soccer")){
			valSC = " DrawDE = '" 			+ getDrawDE() + "' ,"
					+ " AwayBaseLine = '" 	+ getAwayBaseLine() + "' ,"
					+ " HomeBaseLine = '" 	+ getHomeBaseLine() + "' ,";
		}

		String Query = 	"Update " + TableName
						+ " set ZF = " 			+ String.valueOf(getZF()) + " ,"
						+ " ZFOption = '" 		+ getZFOption() + "' ,"
						+ " ZFValue = '" 		+ getZFValue() + "' ,"
						+ " AwayZF = '" 		+ getAwayZF() + "' ,"
						+ " HomeZF = '" 		+ getHomeZF() + "' ,"
						+ " DSOption = '" 		+ getDSOption() + "' ,"
						+ " DSValue = '" 		+ getDSValue() + "' ,"
						+ " AwayDS = '" 		+ getAwayDS() + "' ,"
						+ " HomeDS = '" 		+ getHomeDS() + "' ,"
						+ " AwayDE = '" 		+ getAwayDE() + "' ,"
						+ " HomeDE = '" 		+ getHomeDE() + "' ,"
						+ valSC
						+ " AwayESRE = '" 		+ getAwayESRE() + "' ,"
						+ " HomeESRE = '" 		+ getHomeESRE() + "' ,"
						+ " AwaySD = '" 		+ getAwaySD() + "' ,"
						+ " HomeSD = '" 		+ getHomeSD() + "' ,"
						+ " ZFActive = '" 		+ getZFActive() + "' ,"
						+ " DSActive = '" 		+ getDSActive() + "' ,"
						+ " DEActive = '" 		+ getDEActive() + "' ,"
						+ " ESREActive = '" 	+ getESREActive() + "' ,"
						+ " SDActive = '" 		+ getSDActive() + "' ,"
						+ " DrawActive = '" 	+ getDrawActive() + "' ,"
						+ " State = '" 			+ getState() + "' ,"
						+ " UpdateTime = datetime('now', 'localtime') "
						+ " Where FollowID = '" + getFollowID() + "';";
		return Query;
	}
	public String getUpdateOpenStateToYQuery(String TableName){
		String Query = 	"Update " + TableName + " set OpenState = '"+getOpenState()+"'"
				+ " Where FollowID = '" + getFollowID() + "';";
		return Query;
	}
	public void ResetUIState(){
		this.PSZState = 0;
		this.PSZHomeState = 0;
		this.PSZAwayState = 0;
		this.TotalState = 0;
		this.TotalHomeState = 0;
		this.TotalAwayState = 0;
		this.MLHomeState = 0;
		this.MLAwayState = 0;
		this.PSState = 0;
		this.PSHomeState = 0;
		this.PSAwayState = 0;
		this.SDAwayState = 0;
		this.SDHomeState = 0;
		this.DrawState = 0;
	}
	public void SoccerDEDownWater(){
		double away = Double.parseDouble(getAwayDE());
		double home = Double.parseDouble(getHomeDE());
		if (away >= 0.3) {
			setAwayDE(String.valueOf(away-0.2));
		}
		if (home >= 0.3) {
			setHomeDE(String.valueOf(home-0.2));
		}


	}
	public void SoccerZFDownWater(){
		setAwayZF(String.valueOf(Double.parseDouble(getAwayZF())-0.02));
		setHomeZF(String.valueOf(Double.parseDouble(getHomeZF())-0.02));
	}
	public void setAllID(String ID) {
		// TODO Auto-generated method stub
		setSiteNo(ID);
		setOpenID(getSite() + "_" + ID);
		setFollowID(getSite() + "_" + ID + "_" + getType());
	}
}
