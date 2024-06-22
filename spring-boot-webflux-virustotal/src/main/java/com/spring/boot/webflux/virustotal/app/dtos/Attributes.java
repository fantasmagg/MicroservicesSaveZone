package com.spring.boot.webflux.virustotal.app.dtos;

public class Attributes {
	  private long date;
	    private Stats stats;

	    public long getDate() {
	        return date;
	    }

	    public void setDate(long date) {
	        this.date = date;
	    }

	    public Stats getStats() {
	        return stats;
	    }

	    public void setStats(Stats stats) {
	        this.stats = stats;
	    }
}
