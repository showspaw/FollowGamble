package com.heros.follow.source.sportsmanufacturer.sport.basketball;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**籃球
 * Created by Albert on 2017/1/13.
 */
public  abstract class Basketball implements Sport {
    private final String className = "Basketball";
    public String getClassName(){
        return className;
    }
}
