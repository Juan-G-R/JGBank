package components;

import java.time.LocalDate;

public class Transfert extends Flow{
	
	private int emitterAccountNumber;
	
	public Transfert(String comment, int identifier, double balance, int targetAccountNumber, boolean effect, LocalDate dateOfFlow, int emitterAccountNumber) {
		super(comment, identifier, balance, targetAccountNumber, effect, dateOfFlow);
		this.emitterAccountNumber = emitterAccountNumber;
	}

	public int getEmitterAccountNumber() {
		return emitterAccountNumber;
	}

	public void setEmitterAccountNumber(int emitterAccountNumber) {
		this.emitterAccountNumber = emitterAccountNumber;
	}
	
	

}
