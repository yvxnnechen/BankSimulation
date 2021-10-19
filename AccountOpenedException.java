public class AccountOpenedException extends Exception {
    public AccountOpenedException(int num) {
        super("Error: Account " + num + " Is Not Closed - Transaction Voided");
    }
}
