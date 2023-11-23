package components;

import java.time.LocalDate;

public class Debit extends Flow{

	public Debit(String comment, int identifier, double balance, int targetAccountNumber, boolean effect, LocalDate dateOfFlow) {
		super(comment, identifier, balance, targetAccountNumber, effect, dateOfFlow);
	}

}
