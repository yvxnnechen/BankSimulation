public class CheckTooOldException extends Exception{
    public CheckTooOldException(String date) {
        super("Error: Check too old - Date of Check: " + date);
    }
}
