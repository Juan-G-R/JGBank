package components;

import java.time.LocalDate;

public class Credit extends Flow{

	public Credit(String comment, int identifier, double balance, int targetAccountNumber, boolean effect, LocalDate dateOfFlow) {
		super(comment, identifier, balance, targetAccountNumber, effect, dateOfFlow);
	}

}
