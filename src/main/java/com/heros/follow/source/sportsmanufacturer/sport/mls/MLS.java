package com.heros.follow.source.sportsmanufacturer.sport.mls;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**美式足球
 * Created by Albert on 2017/1/13.
 */
public  abstract class MLS implements Sport {
    private final String className = "MLS";
    public String getClassName(){
        return className;
    }
}
