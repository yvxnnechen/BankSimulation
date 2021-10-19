public class AccountClosedException extends Exception{
    public AccountClosedException(int i) {
        super("Error: Account Number " + i + " is closed and transactions cannot be performed - Transaction Voided");
    }
}
