package com.heros.follow.source.sportsmanufacturer.sport.stock;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;

/**指數
 * Created by Albert on 2017/1/13.
 */
public  abstract class Stock implements Sport {
    private final String className = "Stock";
    public String getClassName(){
        return className;
    }
}
