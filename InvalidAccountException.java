public class InvalidAccountException extends Exception{
    public InvalidAccountException(int i) {
        super("Error: Account Number " + i + " does not exist - Transaction Voided");
    }

    public InvalidAccountException(String s) {
        super("Error: Social Security Number " + s + " does not have any existing accounts - Transaction Voided");
    }
}
