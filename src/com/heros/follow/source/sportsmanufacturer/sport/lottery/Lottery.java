package com.heros.follow.source.sportsmanufacturer.sport.lottery;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**彩球
 * Created by Albert on 2017/1/13.
 */
public  abstract class Lottery implements Sport {
    private final String className = "Lottery";
    public String getClassName(){
        return className;
    }
}
