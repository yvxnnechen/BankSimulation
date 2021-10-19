import java.util.Calendar;

public class TransactionReceipt {
    private TransactionTicket transactionTicket;
    private boolean successIndicatorFlag;
    private String reasonForFailure;
    private double preTransactionBalance;
    private double postTransactionBalance;
    private Calendar postTransactionMaturityDate;

    public TransactionReceipt(TransactionTicket transactionTicket, boolean successIndicatorFlag, String reasonForFailure, double preTransactionBalance, double postTransactionBalance, Calendar postTransactionMaturityDate) {
        this.transactionTicket = transactionTicket;
        this.successIndicatorFlag = successIndicatorFlag;
        this.reasonForFailure = reasonForFailure;
        this.preTransactionBalance = preTransactionBalance;
        this.postTransactionBalance = postTransactionBalance;
        this.postTransactionMaturityDate = postTransactionMaturityDate;
    }

    public TransactionReceipt(TransactionReceipt tr) {
        this(tr.getTransactionTicket(), tr.getTransactionSuccessIndicatorFlag(), tr.getTransactionFailureReason(), tr.getPreTransactionBalance(), tr.getPostTransactionBalance(), tr.getPostTransactionMaturityDate());
    }
    //setPostTransactionBalance() method is a mutator method that accepts a reference to a double of the amount of account has after a successful transaction
    public void setPostTransactionBalance(double postTransactionBalance) {
        this.postTransactionBalance = postTransactionBalance;
    }

    //getTransactionTicket() method is an accessor method that returns a TransactionTicket object
    public TransactionTicket getTransactionTicket() {
        TransactionTicket tt = new TransactionTicket(transactionTicket);
        return tt;
    }

    //getTransactionSuccessIndicatorFlag() method s an accessor method that returns successIndicatorFlag
    public boolean getTransactionSuccessIndicatorFlag() {
        return successIndicatorFlag;
    }

    //getTransactionFailureReason() method is an accessor method that returns reasonForFailure
    public String getTransactionFailureReason() {
        return reasonForFailure;
    }

    //getPreTransactionBalance() method is an accessor method that returns preTransactionBalance
    public double getPreTransactionBalance() {
        return preTransactionBalance;
    }

    //getPostTransactionBalance() method is an accessor method that returns postTransactionBalance
    public double getPostTransactionBalance() {
        return postTransactionBalance;
    }

    //getPostTransactionMaturityDate() method is an accessor method that returns postTransactionMaturityDate
    public Calendar getPostTransactionMaturityDate() {
        return postTransactionMaturityDate;
    }

    //toString() method returns the transaction details depending on the type of transaction
    public String toString() {
        String transactionType = this.getTransactionTicket().getTypeOfTransaction();
        String s;
        switch(transactionType) {
            case "Balance Inquiry":
                s = String.format("Transaction Type: Balance Inquiry\nAccount Number: %d\nCurrent Balance: %.2f", getTransactionTicket().getAcctNumber(), getPostTransactionBalance());
                break;
            case "Deposit":
                s = String.format("Transaction Type: Deposit\nAccount Number: %d\nCurrent Balance: %.2f\nAmount to Deposit: %.2f\nNew Balance: %.2f", getTransactionTicket().getAcctNumber(), getPreTransactionBalance(), getTransactionTicket().getAmountOfTransaction(), getPostTransactionBalance());
                break;
            case "Withdrawal":
                s = String.format("Transaction Type: Withdrawal\nAccount Number: %d\nCurrent Balance: %.2f\nAmount to Withdraw: %.2f\nNew Balance: %.2f", getTransactionTicket().getAcctNumber(), getPreTransactionBalance(), getTransactionTicket().getAmountOfTransaction(), getPostTransactionBalance());
                break;
            case "Clear Check":
                s = String.format("Transaction Type: Clear Check\nAccount Number: %d\nCurrent Balance: %.2f\nAmount to Withdraw: %.2f\nNew Balance: %.2f", getTransactionTicket().getAcctNumber(), getPreTransactionBalance(), getTransactionTicket().getAmountOfTransaction(), getPostTransactionBalance());
                break;
            case "Open New Account":
                s = String.format("Transaction Type: Open New Account\nAccount Number: %d\nCurrent Balance: %.2f\nAccount has successfully been opened!", getTransactionTicket().getAcctNumber(), getPostTransactionBalance());
                break;
            case "Close Account":
                s = String.format("Transaction Type: Close Account\nAccount Number: %d\nCurrentBalance: %.2f\nAccount has successfully been closed!", getTransactionTicket().getAcctNumber(), getPostTransactionBalance());
                break;
            case "Reopen Account":
                s = String.format("Transaction Type: Reopen Account\nAccount Number: %d\nCurrentBalance: %.2f\nAccount has successfully been reopened!", getTransactionTicket().getAcctNumber(), getPostTransactionBalance());
                break;
            case "Delete Account":
                s = String.format("Transaction Type: Delete Account\nAccount Number: %d\nAccount has successfully been deleted!", getTransactionTicket().getAcctNumber());
                break;
            default:
                s = "                                \n";
                break;
        }
        int index = Bank.findAcct(this.getTransactionTicket().getAcctNumber());
        Account a = Bank.getAcct(index);
        if(index != -1 && a.getAcctType().equals("CD") && this.getTransactionSuccessIndicatorFlag() && (transactionType.equals("Deposit") || transactionType.equals("Withdrawal"))) {
            Calendar cal = a.getMaturityDate(this.getTransactionTicket());
            String maturityDate = (cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
            s += String.format("\nNew Maturity Date: %s", maturityDate);
        }
        return s;
    }

}
