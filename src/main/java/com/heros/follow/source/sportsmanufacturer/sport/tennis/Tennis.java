package com.heros.follow.source.sportsmanufacturer.sport.tennis;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**網球
 * Created by Albert on 2017/1/13.
 */
public abstract class Tennis implements Sport {
    private final String className = "Tennis";
    public String getClassName(){
        return className;
    }
}
