package spring_boot_webflux_client_model.client.app.models;

public class Attributes {
	private String status;
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

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	    
}
