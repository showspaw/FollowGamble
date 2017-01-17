package com.heros.follow.source.sportsmanufacturer;

import com.heros.follow.source.sportsmanufacturer.sport.baseball.Baseball;
import com.heros.follow.source.sportsmanufacturer.sport.basketball.Basketball;
import com.heros.follow.source.sportsmanufacturer.sport.horse.Horse;
import com.heros.follow.source.sportsmanufacturer.sport.lottery.Lottery;
import com.heros.follow.source.sportsmanufacturer.sport.mls.MLS;
import com.heros.follow.source.sportsmanufacturer.sport.nhl.NHL;
import com.heros.follow.source.sportsmanufacturer.sport.soccer.Soccer;
import com.heros.follow.source.sportsmanufacturer.sport.stock.Stock;
import com.heros.follow.source.sportsmanufacturer.sport.tennis.Tennis;

/**
 * Created by Albert on 2017/1/13.
 */
public interface SportsFactory {
    Baseball createBaseball();

    Tennis createTennis();

    Basketball createBasketball();

    Horse createHorse();

    Lottery createLottery();

    MLS createMLS();

    NHL createNHL();

    Soccer createSoccer();

    Stock createStock();

}
