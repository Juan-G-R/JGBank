package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import components.Account;
import components.Client;
import components.Credit;
import components.CreditTypeAdapter;
import components.CurrentAccount;
import components.Debit;
import components.DebitTypeAdapter;
import components.Flow;
import components.SavingsAccount;
import components.Transfert;
import components.TransfertTypeAdapter;

public class main {

	// Creation of lists

	static ArrayList<Client> clients = new ArrayList<Client>();
	static ArrayList<Account> accounts = new ArrayList<Account>();
	static Hashtable<Integer, Account> accountsHT = new Hashtable<>();
	static ArrayList<Flow> flows = new ArrayList<Flow>();

	public static void main(String[] args) {


		loadCliens(5);
		displayClients();

		loadAccounts(clients);
		displayAccounts();

		loadAccountsHT(accounts);
		displayAccountsHTAscending();

		loadFlows();
		updateBalance(accountsHT, flows);

		loadJson();
		loadXML();

	}

	public static ArrayList<Client> loadCliens(int nClients) { // Generate clients and add to he array

		for (int i = 1; i <= nClients; i++) {
			clients.add(new Client("name" + i, "firstName" + i));
		}

		return clients;

	}

	public static void displayClients() {

		System.out.println(clients.toString());

	}

	public static ArrayList<Account> loadAccounts(ArrayList<Client> clientsList) { // Generate and add account to array

		for (Client clientSelected : clientsList) {
			accounts.add(new CurrentAccount("currentAccount", clientSelected, 0));
			accounts.add(new SavingsAccount("savingAccount", clientSelected, 0));
		}

		return accounts;

	}

	public static void displayAccounts() {

		System.out.println(accounts.toString());

	}

	public static Hashtable<Integer, Account> loadAccountsHT(ArrayList<Account> accountsList) { // Load accounts hash
																								// table

		for (Account accountSelected : accountsList) {
			accountsHT.put(accountSelected.getAccountNumber(), accountSelected);
		}

		return accountsHT;

	}

	public static void displayAccountsHTAscending() { // Display accounts in ascending order based on the balance

		List<Account> tempSortedFlowHT = new ArrayList<>(accountsHT.values());

		Collections.sort(tempSortedFlowHT, Comparator.comparing(Account::getBalance));

		System.out.println(tempSortedFlowHT);

		System.out.println("Accounts sorted by balance:");
		System.out.println("    " + "Account N -> Balance");
		for (Account accountSelected : tempSortedFlowHT) {
			System.out.println(
					"    " + accountSelected.getAccountNumber() + "         -> " + accountSelected.getBalance());
		}

	}

	public static void loadFlows() { // Genearte flows and added to the array

		flows.add(new Debit("Debit of 50€", 01, 50, accounts.get(0).getAccountNumber(), false,
				LocalDate.now().plusDays(2)));

		for (Account accountSelected : accounts) { // Add credit to current or saving account depending on accountSelect
													// type
			flows.add(new Credit("Credit of 100.50", 0, accountSelected instanceof CurrentAccount ? 100.50 : 1500,
					accountSelected.getAccountNumber(), false, LocalDate.now().plusDays(2)));

		}

		flows.add(new Transfert("50€ transfer", 0, 50, accounts.get(1).getAccountNumber(), false,
				LocalDate.now().plusDays(2), accounts.get(0).getAccountNumber()));

	}

	public static void updateBalance(Hashtable<Integer, Account> accountsHTEnter, ArrayList<Flow> flowsEnter) {

		for (Flow flowSelected : flowsEnter) { // Interate flows and update balances
			accountsHTEnter.get(flowSelected.getTargetAccountNumber()).setBalance(flowSelected);

			Account targetAccount = accountsHTEnter.get(flowSelected.getTargetAccountNumber());
			targetAccount.setBalance(flowSelected);

		}

		Predicate<Account> hasNegativeBalance = account -> account.getBalance() < 0;

		Optional.ofNullable(accountsHTEnter.values().stream().filter(hasNegativeBalance) // Display accounts with
																							// negative balance
				.map(account -> "The account with number: " + account.getAccountNumber()
						+ " has a negative balance of: " + account.getBalance() + "€")
				.reduce((s1, s2) -> s1 + "\n" + s2).orElse(null)).ifPresent(System.out::println);

		displayAccountsHTAscending();

	}

	public static void loadJson() { // Load Json using GSon

		Path filePath = Paths.get("src/assets/flows.json");

		try {

			String jsonContent = new String(Files.readAllBytes(filePath)); // Read file

			JsonArray jsonArray = JsonParser.parseString(jsonContent).getAsJsonArray();

			Gson gson = new GsonBuilder().registerTypeAdapter(Debit.class, new DebitTypeAdapter())
					.registerTypeAdapter(Transfert.class, new TransfertTypeAdapter())
					.registerTypeAdapter(Credit.class, new CreditTypeAdapter()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
					.create();

			for (JsonElement jsonElement : jsonArray) {

				JsonObject jsonObject = jsonElement.getAsJsonObject();
				String type = jsonObject.get("type").getAsString();

				switch (type) { // Filter node by type
				case "Debit":
					Debit debit = gson.fromJson(jsonObject, Debit.class);
					debit.setDateOfFlow(LocalDate.parse(jsonObject.get("dateOfFlow").getAsString()));
					flows.add(debit);
					break;

				case "Credit":
					Credit credit = gson.fromJson(jsonObject, Credit.class);
					credit.setDateOfFlow(LocalDate.parse(jsonObject.get("dateOfFlow").getAsString()));
					flows.add(credit);
					break;

				case "Transfer":
					Transfert transfer = gson.fromJson(jsonObject, Transfert.class);
					transfer.setDateOfFlow(LocalDate.parse(jsonObject.get("dateOfFlow").getAsString()));
					flows.add(transfer);
					break;

				default:
					System.out.println("Unknown type: " + type);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		}

	}

	public static void loadXML() {

		Path filePath = Paths.get("src/assets/accounts.xml");

		Account account = null; // Create an empty account object

		try {

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document document = builder.parse(filePath.toFile());

			document.getDocumentElement().normalize();

			NodeList accountsXMLList = document.getElementsByTagName("Account");

			for (int i = 0; i < accountsXMLList.getLength(); i++) { // Iterate over all the nodes in the nodelist

				Node XMLAccount = accountsXMLList.item(i);

				if (XMLAccount.getNodeType() == Node.ELEMENT_NODE) { // Check for the right node type

					Element accountElement = (Element) XMLAccount;

					switch (accountElement.getAttribute("type")) { // Filter by account type
					case "Current": { // Assign corresponding type to account object
						account = new CurrentAccount(null, new Client(null, null), 0);
						break;
					}
					case "Saving": {
						account = new SavingsAccount(null, new Client(null, null), 0);
						break;
					}
					default:
						throw new IllegalArgumentException("Unexpected value");
					}

					NodeList accountDetails = XMLAccount.getChildNodes();

					for (int n = 0; n < accountDetails.getLength(); n++) {

						Node accountDetail = accountDetails.item(n);

						if (accountDetail.getNodeType() == Node.ELEMENT_NODE) {

							Element detailElement = (Element) accountDetail;

							if (detailElement.getNodeName().equals("client")) { // If the node is the client one create
																				// a nodelist

								NodeList clientDetails = detailElement.getChildNodes();

								for (int x = 0; x < clientDetails.getLength(); x++) {

									Node clientDetail = clientDetails.item(x);

									if (clientDetail.getNodeType() == Node.ELEMENT_NODE) {

										Element clientElement = (Element) clientDetail;

										switch (clientElement.getNodeName()) { // Filter between client values
										case "name": {
											account.getClient().setName(clientElement.getTextContent());
											break;
										}
										case "firstName": {
											account.getClient().setFirstName(clientElement.getTextContent());
											break;
										}
										default:
											throw new IllegalArgumentException(
													"Unexpected value: " + clientElement.getNodeName());
										}

									}

								}

							} else { // If is not a client node get the info

								switch (detailElement.getTagName()) { // Filter info by name
								case "label": {
									account.setLabel(detailElement.getTextContent());
									break;
								}
								case "balance": {
									account.setBalance(Double.parseDouble(detailElement.getTextContent()));
									break;
								}
								default:
									throw new IllegalArgumentException(
											"Unexpected value: " + detailElement.getTagName());
								}

							}

						}

					}

				}

				accounts.add(account);

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
