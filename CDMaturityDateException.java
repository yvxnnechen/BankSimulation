public class CDMaturityDateException extends Exception{
    public CDMaturityDateException(String s) {
        super("Error: CD Maturity Date " + s + " has not been met");
    }
}
