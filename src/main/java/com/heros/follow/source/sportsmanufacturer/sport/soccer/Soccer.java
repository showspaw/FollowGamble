package com.heros.follow.source.sportsmanufacturer.sport.soccer;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**足球
 * Created by Albert on 2017/1/13.
 */
public  abstract class Soccer implements Sport {
    private final String className = "Soccer";
    public String getClassName(){
        return className;
    }
}
