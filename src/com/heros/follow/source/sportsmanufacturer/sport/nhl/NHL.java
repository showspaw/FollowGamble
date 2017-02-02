package com.heros.follow.source.sportsmanufacturer.sport.nhl;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**冰球
 * Created by Albert on 2017/1/13.
 */
public  abstract class NHL  implements Sport {
    private final String className = "NHL";
    public String getClassName(){
        return className;
    }
}
