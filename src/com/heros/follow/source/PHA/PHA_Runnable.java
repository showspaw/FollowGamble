package com.heros.follow.source.PHA;

import com.heros.follow.source.result.GameResult;
import com.heros.follow.tools.HttpClientUtils;

import java.util.HashSet;
import java.util.Set;

//PHA_Runnable.java - 取得法老場次 EventID
// 抓取賽事ID,設定進GameResult
public class PHA_Runnable implements Runnable {
    private GameResult gameResult;
    private PHA_Engine threadPoolListener;
    private String href;

    public PHA_Runnable(PHA_Engine l1, GameResult l2, String url) {
        this.threadPoolListener = l1;
        this.gameResult = l2;
        this.href = url;
    }

    @Override
    public void run() {
        threadPoolListener.getFollowKeyTheads().put(gameResult.getFollowIdUrl().hashCode(), Thread.currentThread());

        if (!gameResult.getSiteNo().equals(""))
            return;

        String ID = "";

        int reTry = 1; // 可重試一次

        do {
            ID = HttpClientUtils.httpGetAsDom(HttpClientUtils.HttpWeb.PHA, this.href, null, null).select("span[class=important]").get(0).html();
            if (!ID.equals("")) {
                this.completeNotify(ID);
                break;
            } else
                reTry--;
        } while (reTry >= 0);


        threadPoolListener.getFollowKeyTheads().remove(gameResult.getFollowIdUrl().hashCode());
    }

    private void completeNotify(String ID) {
        this.gameResult.setAllID(ID);
        if (!threadPoolListener.getAlreadyOpen_FollowID().contains(gameResult.getFollowID())) // 不是黑名單 && 還沒開盤過 !datamanager.getInstence().getIdBanList().contains(gameResult.getFollowID())
        {
            if (gameResult.getISdbs() != null && !gameResult.getISdbs().equals("0")) { // 不是反灰
                threadPoolListener.getAlreadyOpen_FollowID().add(gameResult.getFollowID());
                Set<String> obj = new HashSet<>();
                obj.add(gameResult.getFollowID());
//				SendApiCenter.getSendApiCenter().sendOpen(SiteCode.PHA.getCode(), threadPoolListener.getHeroName(), obj, "");
            }
            threadPoolListener.sendSingleData(gameResult);
        }
    }

}
