package com.heros.follow.source.sportsmanufacturer.sport.horse;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**賽馬
 * Created by Albert on 2017/1/13.
 */
public  abstract class Horse implements Sport {
    private final String className = "Horse";
    public String getClassName(){
        return className;
    }
}
