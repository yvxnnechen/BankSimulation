import java.util.Calendar;

public class SavingsAccount extends Account{
    public SavingsAccount(Depositor depositor, int accountNumber, String accountType, String accountStatus, double accountBalance) {
        super(depositor, accountNumber, accountType, accountStatus, accountBalance);
    }

    public SavingsAccount(SavingsAccount acc) {
        super(acc.getDepositor(), acc.getAcctNumber(), acc.getAcctType(), acc.getAccountStatus(), acc.getBalance(new TransactionTicket(Calendar.getInstance(), acc.getAcctNumber(), "", 0 , 0)).getPreTransactionBalance());
    }
    //makeDeposit() method accepts a reference to TransactionTicket object
    public TransactionReceipt makeDeposit(TransactionTicket transactionTicket) throws InvalidAccountException, InvalidAmountException, AccountClosedException {
        try {
            //searches for the account
            int index = Bank.findAcct(transactionTicket.getAcctNumber());
            if (index == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            } else if (transactionTicket.getAmountOfTransaction() < 0) {
                throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
            } else if (this.getAccountStatus().equals("Closed")) {
                throw new AccountClosedException(transactionTicket.getAcctNumber());
            } else {
                //adds the transaction amount
                return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() + transactionTicket.getAmountOfTransaction(), null);
            }
        } catch (InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance(), null);
        } catch (InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance(), null);
        } catch (AccountClosedException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance(), null);
        }
    }

    //makeWithdrawal() method accepts a reference to TransactionTicket object

    public TransactionReceipt makeWithdrawal(TransactionTicket transactionTicket) {
        try {
            if (getBalance(transactionTicket).getPostTransactionBalance() < transactionTicket.getAmountOfTransaction()) {
                throw new InsufficientFundsException("Error: Insufficient Funds - Transaction Voided");
            } else if (transactionTicket.getAmountOfTransaction() < 0) {
                throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
            } else if (this.getAccountStatus().equals("Closed")) {
                throw new AccountClosedException(transactionTicket.getAcctNumber());
            } else {
                //subtracts the transaction amount
                return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() - transactionTicket.getAmountOfTransaction(), null);
            }
        } catch(InsufficientFundsException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance(), null);
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance(), null);
        } catch(AccountClosedException ex) {
           return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        }
    }

    //overridden toString() method
    public String toString() {
        return String.format("%9s\t%14s\t%26s\t%18d\t%16s\t%18s\t%11.2f",
                this.getDepositor().getName().getLastName(),
                this.getDepositor().getName().getFirstName(),
                this.getDepositor().getSSN(),
                this.getAcctNumber(),
                this.getAcctType(),
                this.getAccountStatus(),
                this.getBalance(new TransactionTicket(Calendar.getInstance(), this.getAcctNumber(), "", 0, 0)).getPostTransactionBalance());
    }

    /*
     * equals() method accepts:
     *   - o: reference to Object class object
     * process:
     *   - checks if o is of Account type
     *   - if not, the object calling the equals() method and o are compared to each other looking at the fields:
     *     + account number
     *     + account type
     *     + account status
     *     + depositor
     *     + account balance
     *   - returns true if all of the fields are equal and false otherwise
     */
    public boolean equals(Object o) {
        if(!(o instanceof Account)) {
            return false;
        }
        Account a = (Account) o;
        return this.getAcctNumber() == a.getAcctNumber() && this.getAcctType().equals(a.getAcctType()) && this.getAccountStatus().equals(a.getAccountStatus()) && this.getDepositor()== a.getDepositor() && this.getBalance(new TransactionTicket(Calendar.getInstance(), this.getAcctNumber(), "", 0 , 0)) == a.getBalance(new TransactionTicket(Calendar.getInstance(), a.getAcctNumber(), "", 0 , 0)) ;
    }

}