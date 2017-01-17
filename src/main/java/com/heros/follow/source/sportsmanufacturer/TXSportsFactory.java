package com.heros.follow.source.sportsmanufacturer;

import com.heros.follow.source.sportsmanufacturer.sport.baseball.Baseball;
import com.heros.follow.source.sportsmanufacturer.sport.basketball.Basketball;
import com.heros.follow.source.sportsmanufacturer.sport.horse.Horse;
import com.heros.follow.source.sportsmanufacturer.sport.lottery.Lottery;
import com.heros.follow.source.sportsmanufacturer.sport.mls.MLS;
import com.heros.follow.source.sportsmanufacturer.sport.nhl.NHL;
import com.heros.follow.source.sportsmanufacturer.sport.soccer.Soccer;
import com.heros.follow.source.sportsmanufacturer.sport.stock.Stock;
import com.heros.follow.source.sportsmanufacturer.sport.tennis.TXTennis;
import com.heros.follow.source.sportsmanufacturer.sport.tennis.Tennis;

/**
 * Created by Albert on 2017/1/13.
 */
public class TXSportsFactory implements SportsFactory {
    @Override
    public Baseball createBaseball() {
        return null;
    }

    @Override
    public Tennis createTennis() {
        return new TXTennis();
    }

    @Override
    public Basketball createBasketball() {
        return null;
    }

    @Override
    public Horse createHorse() {
        return null;
    }

    @Override
    public Lottery createLottery() {
        return null;
    }

    @Override
    public MLS createMLS() {
        return null;
    }

    @Override
    public NHL createNHL() {
        return null;
    }

    @Override
    public Soccer createSoccer() {
        return null;
    }

    @Override
    public Stock createStock() {
        return null;
    }
}
