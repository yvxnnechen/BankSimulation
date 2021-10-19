public class BalanceInAccountException extends Exception{
    public BalanceInAccountException(int accountNumber) {
        super("Error: Cannot close Account " + accountNumber + " with balance - Transaction Voided");
    }
}
