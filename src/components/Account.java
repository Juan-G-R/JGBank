package components;

import java.util.concurrent.atomic.AtomicInteger;


public abstract class Account {

	protected String label;
	protected double balance;
	protected int accountNumber;
	private static AtomicInteger count = new AtomicInteger(0);
	protected Client client;

	public Account(String label, Client client, double balance) {
		this.label = label;
		this.client = client;
		accountNumber = count.incrementAndGet();
		this.balance = balance;

	}

	public String toString() {
	    return "Label: " + label + ", Balance: " + String.format("%.2f", balance) + "€" +
	           ", AccountNumber: " + accountNumber + ", Client: " + client.toString();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(Flow flow) { //Add or subtract balance depending of the flow type

		if (flow instanceof Credit) {
			this.balance += flow.getBalance();
		} else if (flow instanceof Debit) {
			this.balance -= flow.getBalance();
		} else if (flow instanceof Transfert) {
			Transfert flow1 = (Transfert) flow;
			if (flow1.getTargetAccountNumber() == this.accountNumber) {
				this.balance += flow1.getBalance();
			} else if (flow1.getEmitterAccountNumber() == this.accountNumber) {
				this.balance -= flow1.getBalance();
			}
		}
	}
	
	public void setBalance(double balance) { //For the XML import that gets a number not a flow
		
		this.balance = balance;
		
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumebr) {
		this.accountNumber = accountNumebr;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	

}
