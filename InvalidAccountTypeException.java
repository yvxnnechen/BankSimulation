public class InvalidAccountTypeException extends Exception{
    public InvalidAccountTypeException(String s) {
        super("Error: Account type " + s + " does not exist - Transaction Voided");
    }
    public InvalidAccountTypeException(int acc) {
        super("Error: Account Number " + acc + " Already Exists - Transaction Voided");
    }
    public InvalidAccountTypeException(String s, int num, String str) {
        super("Error: " + s + " Account " + num + " cannot " + str + " - Transaction Voided");
    }
}
