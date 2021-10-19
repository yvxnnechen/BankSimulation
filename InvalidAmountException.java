public class InvalidAmountException extends Exception{
    public InvalidAmountException (double amount) {
        super("Error: " + amount + " is not a valid amount - Transaction Voided");
    }
}
