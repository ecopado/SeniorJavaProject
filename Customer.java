package AvailabilityDemand;
import java.util.*;

public class Customer implements ISubscriber {
	
	public Customer(String customerName) {
		name= customerName;
	}
	
	public Customer(String customerName, int uniqueID) {
		id = uniqueID;
		name = customerName;
	}

	private int id;
	private String name;
	private Room room = new Room();
	private StayPeriod stay = new StayPeriod();

	public boolean subscribe(String location, Date from, Date to) {
		
		room.setLocation(location);
		stay.setStartDate(from);
		stay.setEndDate(to);
		
		Broker.getInstance().addSubscriber(this);
		return true;
	}

	public boolean unSubscribe(String location, Date from, Date to) {
		
		room.setLocation(location);
		stay.setStartDate(from);
		stay.setEndDate(to);
		
		if(compareLocation(location) && compareStart(from) && compareEnd(to)) {
			id = Broker.getInstance().setIDforUnsub(name, location, from, to);
			Broker.getInstance().removeSubscriber(this);
			return true;
		}	
		else
			return false;
	}
	
	public boolean compareLocation(String location) {
		if(room.getLocation().equalsIgnoreCase(location))
			return true;
		else
			return false;
	}
	
	public boolean compareStart(Date from) {
		if(stay.getStartDate().compareTo(from) == 0)
			return true;
		else
			return false;
	}
	
	public boolean compareEnd(Date to) {
		if(stay.getEndDate().compareTo(to) == 0)
			return true;
		else
			return false;
	}
	
	// Get and Set Variables
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
