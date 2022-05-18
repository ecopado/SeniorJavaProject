package AvailabilityDemand;
import java.util.*;

public interface IPublisher {

	public abstract boolean publish(String location, Date availableFrom, Date availableDate);

}
