import java.util.Calendar;
public class TransactionTicket {
    private Calendar dateOfTransaction;
    private int acctNumber;
    private String typeOfTransaction;
    private double amountOfTransaction;
    private int termOfCD;

    public TransactionTicket(Calendar dateOfTransaction, int acctNumber, String typeOfTransaction, double amountOfTransaction, int termOfCD) {
        this.dateOfTransaction = dateOfTransaction;
        this.acctNumber = acctNumber;
        this.typeOfTransaction = typeOfTransaction;
        this.amountOfTransaction = amountOfTransaction;
        this.termOfCD = termOfCD;
    }

    public TransactionTicket(TransactionTicket tt) {
        this(tt.getDateOfTransaction(), tt.getAcctNumber(), tt.getTypeOfTransaction(), tt.getAmountOfTransaction(), tt.getTermOfCD());
    }

    //getDateOfTransaction() method is an accessor method that returns a Calendar object, dateOfTransaction
    public Calendar getDateOfTransaction() {
        return dateOfTransaction;
    }

    //getAcctNumber() method returns the account number
    public int getAcctNumber() {
        return acctNumber;
    }

    //getTypeOfTransaction() method is an accessor method that returns typeOfTransaction
    public String getTypeOfTransaction() {
        return typeOfTransaction;
    }

    //getAmountOfTransaction() method is an accessor method that returns amountOfTransaction
    public double getAmountOfTransaction() {
        return amountOfTransaction;
    }

    //getTermOfCD() method is an accessor method that returns termOfCD
    public int getTermOfCD() {
        return termOfCD;
    }

    /*
     * toString() method is overridden and returns a String stating:
     *     + date of transaction
     *     + type of transaction
     *     + amount of transaction
     *     + term of CD
     */
    public String toString() {
        return ("Date of Transaction: " + getDateOfTransaction().getTime() +
                "Type of Transaction: " + getTypeOfTransaction() +
                "Amount of Transaction: " + getAmountOfTransaction() +
                "Term of CD: " + getTermOfCD()
                 );
    }
}
