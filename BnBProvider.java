package AvailabilityDemand;
import java.util.*;

public class BnBProvider implements IPublisher {
	
	public BnBProvider(String providerName, int uniqueID) {
		id = uniqueID;
		name = providerName;
	}

	private int id;
	private String name;
	private Room room = new Room();
	private StayPeriod stay = new StayPeriod();

	public boolean publish(String location, Date from, Date to) {
		room.setLocation(location);
		stay.setStartDate(from);
		stay.setEndDate(to);

		Broker.getInstance().addPublisher(this);
		return true;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRoom() {
		return room.getLocation();
	}
	
	public Date getStart() {
		return stay.getStartDate();
	}
	
	public Date getEnd() {
		return stay.getEndDate();
	}
}
