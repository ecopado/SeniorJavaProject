package AvailabilityDemand;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AvailabilityDemand {
	
	private int customerID = 0;
	private int providerID = 0;

	public AvailabilityDemand() {
		Broker broker = Broker.getInstance();
		broker.reset();
	}

	public void processInput(String command) {
		DateFormat dffrom = new SimpleDateFormat("MM/dd/yyyy");
		String[] tokens = command.split(", ");
		
		Date defaultDate = new Date();
		Date startDate = new Date();
		Date endDate = new Date();
		
		try {
			defaultDate = dffrom.parse("11/27/2021");
		} catch (ParseException e1) {
			e1.printStackTrace();
		};
		
		try {
			startDate = dffrom.parse(tokens[3]);
		} catch (ParseException e) {
			return;
		};
		
		try {
			endDate = dffrom.parse(tokens[4]);
		} catch (ParseException e) {
			return;
		};
		
		if(startDate.compareTo(endDate) > 0 || startDate.compareTo(defaultDate) < 0) {
			return;
		}

		int size = tokens.length;
		if(size == 5) {
			if (tokens[0].equalsIgnoreCase("subscribe")) {
				customerID++;
				subscribe(tokens[1], tokens[2], startDate, endDate);
			}
	
			else if (tokens[0].equalsIgnoreCase("unsubscribe")) {
				unSubscribe(tokens[1], tokens[2], startDate, endDate); 
			}
	
			else if (tokens[0].equalsIgnoreCase("publish")) {
				providerID++;
				publisher(tokens[1], tokens[2], startDate, endDate);
			}
		}
	}

	public void subscribe(String name, String location, Date from, Date to) {
		Customer c = new Customer(name, customerID);
		c.subscribe(location, from, to);
	}

	public void unSubscribe(String name, String location, Date from, Date to) {
		Customer c = new Customer(name);
		c.unSubscribe(location, from, to);
	}

	public void publisher(String name, String location, Date from, Date to) {
		BnBProvider p = new BnBProvider(name, providerID);
		p.publish(location, from, to);
	}
	
	public List<String> getAggregatedOutput() { 
		return Broker.getInstance().getOutput(); 
	}
	
	public void reset() {
		Broker.getInstance().reset();
	}

  public static void main(String[] args) {
		AvailabilityDemand ava = new AvailabilityDemand();
		List<String> out;

		//Test 1 - Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
		ava.processInput("subscribe, Jane Doe, Tempe, 11/29/2021, 12/02/2021");
		ava.processInput("publish, High-Mountains, Tempe, 11/28/2021, 12/02/2021");
		ava.processInput("publish, AirCloud, Tempe, 11/28/2021, 12/05/2021");
		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));

		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}


		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 2 - Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		// John Doe's subscription fits within the criteria thus, John Doe should get notified of this event
		ava.processInput("publish, High-Mountains, New York City, 12/01/2021, 12/05/2021");
		// William's subscription fits within the criteria thus, William should getnotified of this event
		ava.processInput("publish, AirClouds, New York City, 12/10/2021, 12/15/2021");
		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}

		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 3 - Correct
		//Date format invalid: DD/MM/YYYY
		ava.processInput("subscribe, John Doe, New York City, 15/01/2022, 30/01/2022");
		//Date format invalid: DD MMM YYYY
		ava.processInput("publish, High-Mountains, New York City, 14Jan 2022, 30 Jan 2022");
		//stay period to date must be smaller than stay period from date
		ava.processInput("subscribe, John Doe, New York City, 30/01/2022, 15/01/2022");
		//extra parameter in the publish method
		ava.processInput("publish, High-Mountains, New York City, 14Jan 2022, 30 Jan 2022, great view and lot of space");
		//available till date must be smaller than available from date
		ava.processInput("publish, AirClouds, New York City, 30/01/2022, 15/01/2022");

		out = ava.getAggregatedOutput();
		System.out.println(out.size());
		//System.out.println(Arrays.toString(out.toArray()));


		ava.reset();
		System.out.println("\n\nNEW TEST \n");



		//Test 4 - checks array size, should be 0 - Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		// to date needs to be same as or greater than end date of subscribed period
		ava.processInput("publish, High-Mountains, New York City, 11/29/2021, 12/02/2021");
		// start date of availability period needs to be later than default date which is 11/27/2021
		ava.processInput("publish, High-Mountains, New York City, 11/20/2021, 12/05/2021");
		// start date of stay period needs to be later than default date which is 11/27/2021
		ava.processInput("subscribe, Jane Doe, New York City, 11/20/2021, 12/05/2021");

		out = ava.getAggregatedOutput();
		System.out.println(out.size());


		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 5 -- Correct
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/05/2021");
		//overlaps with the first published availability, should be discarded
		ava.processInput("publish, High-Mountains, New York City, 11/29/2021, 12/02/2021");
		//overlaps with the first published availability, should be discarded
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");

		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}

		ava.reset();
		System.out.println("\n\nNEW TEST \n");


		//Test 6 - FAIL event should notify all that fit criteria, not just one...
		//			Events should be stored so new subscribers can also see them.
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		//both subscribers should get the notification as satisfy the criteria
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
		//one subscriber removed from system
		ava.processInput("unsubscribe, William, New York City, 12/10/2021, 12/15/2021");
		//duplicate published event, no action taken
		ava.processInput("publish, High-Mountains, New York City, 11/30/2021, 12/15/2021");
		//one subscriber removed from system, no subscribers in system
		ava.processInput("unsubscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		//no subscribers in system, system will store the event
		ava.processInput("publish, AirClouds, New York City, 11/30/2021, 12/15/2021");
		// both stored published events will be fired for below customer since thisone comes as a new subscription
		ava.processInput("subscribe, William, New York City, 12/10/2021, 12/15/2021");
		// both stored published events will be fired for below customer since thisone comes as a new subscription
		ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		for(int i = 0; i< out.size(); i++) {
			System.out.println(out.get(i));
		}

		//ava.processInput("subscribe, Jane Doe, Atlanta   , 12/01/2021, 12/05/2021");
		//ava.processInput("publish, High-Mountains, New      York City, 11/30/2021, 12/15/2021");
		//ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");

		//out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		//ava.processInput("unsubscribe, Jane Doe, New York City, 12/01/2021, 12/05/2021");
		//ava.processInput("publish, High-Mountains, Atlanta, 11/30/2021, 12/15/2021");

		//out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
		//ava.reset();

		//ava.processInput("subscribe, Jane Doe, New York City, 12/01/2021, 12/05/2021");
		//ava.processInput("publish, High-Mountains, New      York City, 11/30/2021, 12/15/2021");
		//ava.processInput("subscribe, John Doe, New York City, 12/01/2021, 12/05/2021");
		//ava.processInput("unsubscribe, Jane Doe, New York City, 12/01/2021, 12/05/2021");
		//out = ava.getAggregatedOutput();
		//System.out.println(Arrays.toString(out.toArray()));
	}
}
