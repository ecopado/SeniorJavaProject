package AvailabilityDemand;

import java.text.*;
import java.util.*;

public class Broker {

	private List<BnBProvider> providers = new ArrayList<BnBProvider>();
	private List<Customer> customers = new ArrayList<Customer>();
	private List<TakenBnb> takenRoomIDs = new ArrayList<TakenBnb>();
	private List<String> output = new ArrayList<String>();

	private static Broker singleton = null;

	public static Broker getInstance() {
		if (singleton == null)
			singleton = new Broker();
		return singleton;
	}

	public void addSubscriber(Customer cust) {
		boolean discard = false;
		
		int size = customers.size();
		for(int i=0; i<size; i++) {
			if(customers.get(i).getName().equalsIgnoreCase(cust.getName()) && customers.get(i).getRoom().equalsIgnoreCase(cust.getRoom())) {
        if (customers.get(i).getStart().compareTo(cust.getStart()) == 0 && customers.get(i).getEnd().compareTo(cust.getEnd()) == 0) {
          discard = true;
        }
			}
		}
		
		if(discard == false) {
			customers.add(cust);
			notifySubscriber();
		}
	}

	public void removeSubscriber(Customer cust) {
		List<Integer> deleteItems = new ArrayList<Integer>();
		
		int size = customers.size();
		for (int i = 0; i < size; i++) {
			if (compareCustomer(cust, i)) {
				deleteItems.add(i);
			} else
				continue;
		}
		
		size = deleteItems.size();
		for(int i=0; i<size; i++) {
			int x = deleteItems.get(i);
			removeSub(x);
			customers.remove(x);
		}
		notifySubscriber();
	}

	public void addPublisher(BnBProvider prov) {
		boolean discard = false;
		
		int size = providers.size();
		for(int i=0; i<size; i++) {
			if(providers.get(i).getName().equalsIgnoreCase(prov.getName()) && customers.get(i).getRoom().equalsIgnoreCase(cust.getRoom())) {
        if (providers.get(i).getStart().compareTo(prov.getStart()) == 0 && providers.get(i).getEnd().compareTo(prov.getEnd()) == 0) {
          discard = true;
        }
        if(prov.getStart().compareTo(providers.get(i).getEnd()) <= 0 && prov.getEnd().compareTo(providers.get(i).getStart()) >= 0) {
          discard = true;
        }
				
			}
		}
		
		if(discard == false) {
			providers.add(prov);
			notifySubscriber();
		}
	}


	public void notifySubscriber() {
		DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");

		int sizeC = customers.size();
		int sizeT = takenRoomIDs.size();
		int sizeP = providers.size();

		boolean roomTaken = false;
		boolean roomAssigned = false;

		for (int i = 0; i < sizeC; i++) {

			for (int k = 0; k < sizeP; k++) {
				if (roomMatch(i, k) == true) {
					int customerID = 0;

					for (int j = 0; j < sizeT; j++) {
						if (takenRoomIDs.get(j).getProvID() == providers.get(k).getID()) {
							customerID = takenRoomIDs.get(j).getSubID();
							roomTaken = true;
							sizeT = takenRoomIDs.size();

							break;
						}
					}

					if (roomTaken == false) {
						String result = customers.get(i).getName() + " notified of B&B availability in "
								+ customers.get(i).getRoom() + " from "
								+ outputFormatter.format(providers.get(k).getStart()) + " to "
								+ outputFormatter.format(providers.get(k).getEnd()) + " by "
								+ providers.get(k).getName() + " B&B";

						output.add(result);
						roomAssigned = true;

						addSubToRoom(i, k);
						sizeT = takenRoomIDs.size();
					}

					else {
						for (int l = 0; l < sizeC; l++) {
							if (customerID == customers.get(l).getID()) {
								if ((customers.get(i).getStart().compareTo(customers.get(l).getEnd()) <= 0)
										&& (customers.get(i).getEnd().compareTo(customers.get(l).getStart()) >= 0)) {

									roomAssigned = true;
									break;
								}

								else {
									String result = customers.get(i).getName() + " notified of B&B availability in "
											+ customers.get(i).getRoom() + " from "
											+ outputFormatter.format(providers.get(k).getStart()) + " to "
											+ outputFormatter.format(providers.get(k).getEnd()) + " by "
											+ providers.get(k).getName() + " B&B";

									output.add(result);
									roomAssigned = true;

									addSubToRoom(i, k);
									sizeT = takenRoomIDs.size();

									break;
								}
							}
						}
					}
				}
				roomAssigned = false;
				roomTaken = false;
			}
		}
	}

	public void returnCustomerList() {
		int size = customers.size();
		for (int i = 0; i < size; i++) {
			System.out.println(customers.get(i).getName() + " has ID: " + customers.get(i).getID());
		}
	}
	
	public int setIDforUnsub(String name, String location, Date from, Date to) {
		int size = customers.size();
		int id = 0;
		
		for(int i=0; i<size; i++) {
			if(customers.get(i).getName().equalsIgnoreCase(name) && customers.get(i).getRoom().equalsIgnoreCase(location)) {
        if(customers.get(i).getStart().compareTo(from) == 0 && customers.get(i).getEnd().compareTo(to) == 0) {
          id = customers.get(i).getID();
          break;
        }
			}
		}
		
		return id;
	}

	public void removeSub(int i) {
		List<Integer> deleteItems = new ArrayList<Integer>();
		int size = takenRoomIDs.size();

		for (int j = 0; j < size; j++) {
			if (customers.get(i).getID() == takenRoomIDs.get(j).getSubID()) {
				deleteItems.add(j);
			}
		}
		
		size = deleteItems.size();
		for(int k=0; k<size; k++) {
			
			int x = deleteItems.get(k) - k;
			takenRoomIDs.remove(x);
		}
	}

	public void addSubToRoom(int i, int k) {
		TakenBnb take = new TakenBnb();
		take.setSubID(customers.get(i).getID());
		take.setProvID(providers.get(k).getID());
		
		int size = takenRoomIDs.size();
		takenRoomIDs.add(take);
		
		size = takenRoomIDs.size();
	}

	public boolean roomMatch(int i, int k) {
		boolean check = false;
		if (customers.get(i).getRoom().equalsIgnoreCase(providers.get(k).getRoom())) {
			// Does customer date EQUAL to or AFTER provider date?
			if (customers.get(i).getStart().compareTo(providers.get(k).getStart()) >= 0) {
				// Does customer date EQUAL to or BEFORE provider date?
				if (customers.get(i).getEnd().compareTo(providers.get(k).getEnd()) <= 0) {
					check = true;
				}
			}
		}
		return check;
	}

	// Compare to Find Subscriber
	public boolean compareCustomer(Customer cust, int index) {
		if (cCompareName(cust.getName(), index) && cCompareLocation(cust.getRoom(), index)
				&& cCompareStart(cust.getStart(), index) && cCompareEnd(cust.getEnd(), index))
			return true;
		else
			return false;
	}

	public boolean cCompareName(String name, int index) {
		if (customers.get(index).getName().equalsIgnoreCase(name))
			return true;
		else
			return false;
	}

	public boolean cCompareLocation(String location, int index) {
		if (customers.get(index).getRoom().equalsIgnoreCase(location))
			return true;
		else
			return false;
	}

	public boolean cCompareStart(Date start, int index) {
		if (customers.get(index).getStart().compareTo(start) == 0)
			return true;
		else
			return false;
	}

	public boolean cCompareEnd(Date end, int index) {
		if (customers.get(index).getEnd().compareTo(end) == 0)
			return true;
		else
			return false;
	}

	public List<String> getOutput() {
		 return output;
	}

	public void reset() {
		providers = new ArrayList<BnBProvider>();
		customers = new ArrayList<Customer>();
		output = new ArrayList<String>();
	}
}
