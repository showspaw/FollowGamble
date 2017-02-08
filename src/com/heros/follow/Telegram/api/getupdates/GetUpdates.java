package com.heros.follow.Telegram.api.getupdates;

import java.util.List;

/**
 * Created by root on 2017/2/8.
 */
public class GetUpdates {
    private boolean ok;
    private List<Result> result;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
}
