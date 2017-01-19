package com.heros.follow.datas;

import com.heros.follow.source.sportsmanufacturer.sport.Sport;
import com.heros.follow.utils.GenericEnum;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class stateData {
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private final String identifier;
	private final String cid;
	private final String game;
	private final int type;
	
	private long lastActionTime;
	
	public stateData(GenericEnum.SiteCode cid, GenericEnum.SportCode game, int type) {
		this.cid = cid.getCode();
		this.game = game.getClassName();
		this.type = type;
		this.identifier = (this.cid + "_" + this.game + "_" + type).toUpperCase();
		lastActionTime = System.currentTimeMillis();
	}

	public long getLastActionTime() {
		lock.readLock().lock();
		try {
			return lastActionTime;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void setLastActionTime(long lastActionTime) {
		lock.writeLock().lock();
		try {
			this.lastActionTime = lastActionTime;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getCid() {
		return cid;
	}

	public String getGame() {
		return game;
	}

	public int getType() {
		return type;
	}
	
}
