package AvailabilityDemand;
import java.util.*;

public class StayPeriod {
	
	private Date startDate = new Date();;
	private Date endDate = new Date();
	
	//START DATE
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date start) {
		startDate = start;
	} 
	
	//END DATE
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date end) {
		endDate = end;

	}
}
