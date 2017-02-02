package com.heros.follow.source.sportsmanufacturer.sport.baseball;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**棒球
 * Created by Albert on 2017/1/13.
 */
public  abstract class Baseball implements Sport {
    private final String className = "Baseball";
    public String getClassName(){
        return className;
    }
}
