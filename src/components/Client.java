package components;

import java.util.concurrent.atomic.AtomicInteger;

public class Client {
	
	private String name;
	private String firstName;
	private int clientNumber;
	private static AtomicInteger count = new AtomicInteger(0); 

	
	public Client(String Name, String firstName) {
		this.name = Name;
		this.firstName = firstName;
		clientNumber = count.incrementAndGet(); 
	}
	

	public String toString() {
	
		return "Name: " + name + " FirstName: " + firstName + " clientNumber: " + clientNumber;
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}
	
	

}
