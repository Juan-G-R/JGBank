package components;

import java.time.LocalDate;


public abstract class Flow {

	protected String comment; 
	protected int identifier;
	protected double balance;
	protected int targetAccountNumber;
	protected boolean effect;
	protected LocalDate dateOfFlow;

	public Flow(String comment, int identifier, double balance, int targetAccountNumber, boolean effect, LocalDate dateOfFlow) {
		super();
		this.comment = comment;
		this.identifier = identifier;
		this.balance = balance;
		this.targetAccountNumber = targetAccountNumber;
		this.effect = effect;
		this.dateOfFlow = dateOfFlow;

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getTargetAccountNumber() {
		return targetAccountNumber;
	}

	public void setTargetAccountNumber(int targetAccountNumber) {
		this.targetAccountNumber = targetAccountNumber;
	}

	public boolean isEffect() {
		return effect;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}

	public LocalDate getDateOfFlow() {
		return dateOfFlow;
	}

	public void setDateOfFlow(LocalDate dateOfFlow) {
		this.dateOfFlow = dateOfFlow;
	}


	

}
