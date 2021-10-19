public class InvalidMenuSelectionException extends Exception{

    public InvalidMenuSelectionException(char s) {
        super("Error: Menu Selection " + s + " does not exist");
    }
}
