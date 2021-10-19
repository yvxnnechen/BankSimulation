import java.util.Calendar;
public class CheckingAccount extends Account {
    private Check check;

    public CheckingAccount(Depositor depositor, int accountNumber, String accountType, String accountStatus, double accountBalance) {
        super(depositor, accountNumber, accountType, accountStatus, accountBalance);
        this.check = null;
    }

    public CheckingAccount(CheckingAccount acc) {
        super(acc.getDepositor(), acc.getAcctNumber(), acc.getAcctType(), acc.getAccountStatus(), acc.getBalance(new TransactionTicket(Calendar.getInstance(), acc.getAcctNumber(), "", 0 , 0)).getPreTransactionBalance());
        this.check = null;
    }
    /*
     * makeDeposit() method accepts:
     *   - transactionTicket: reference to TransactionTicket object
     * process:
     *   - calls transactionTicket.getAmountOfTransaction()
     *   - if the amount is less than 0, a new TransactionReceipt object is created indicating that the
     *     transaction was not successful because it is an invalid amount
     *   - otherwise, a new Transaction object is created indicating that the transaction was successful
     *     and adds the amount of the transaction to the account balance
     *   - the TransactionReceipt object is passed into the addTransaction() method to add the object into the ArrayList of TransactionReceipts
     *   - the TransactionReceipt object is then returned
     */
    public TransactionReceipt makeDeposit(TransactionTicket transactionTicket) throws InvalidAccountException {
        try {
            int index = Bank.findAcct(transactionTicket.getAcctNumber());
            if (index == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            } else if (transactionTicket.getAmountOfTransaction() < 0) {
                throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
            } else if (this.getAccountStatus().equals("Closed") || this.getAccountStatus().equals("Deleted")) {
                throw new AccountClosedException(transactionTicket.getAcctNumber());
            }
            return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() + transactionTicket.getAmountOfTransaction(), getMaturityDate(transactionTicket));
        } catch(InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(AccountClosedException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        }
    }

    //makeWithdrawal() method accepts a reference to TransactionTicket object
    public TransactionReceipt makeWithdrawal(TransactionTicket transactionTicket) {
        try {
            int index = Bank.findAcct(transactionTicket.getAcctNumber());
            if (index == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            } else if (getBalance(transactionTicket).getPreTransactionBalance() < transactionTicket.getAmountOfTransaction()) {
                throw new InsufficientFundsException("Error: Insufficient Funds - Transaction Voided");
            } else if (this.getAccountStatus().equals("Closed")) {
                throw new AccountClosedException(transactionTicket.getAcctNumber());
            } else if (transactionTicket.getAmountOfTransaction() < 0) {
                throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
            } else {
                return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() - transactionTicket.getAmountOfTransaction(), null);
            }
        } catch(InsufficientFundsException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(AccountClosedException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() + transactionTicket.getAmountOfTransaction(), getMaturityDate(transactionTicket));
        }
    }

    //clearCheck() methods accepts a reference to Check object and a reference to TransactionTicket object
    public TransactionReceipt clearCheck(Check check, TransactionTicket transactionTicket) {
        try {
            Calendar calendar = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -6);
            if (!(this.getAcctType().equals("Checking"))) {
                throw new InvalidAccountTypeException(this.getAcctType());
            } else if (this.getAccountStatus().equals("Closed")) {
                throw new AccountClosedException(this.getAcctNumber());
            } else if (check.getDateOfCheck().after(calendar)) {
                throw new PostDatedCheckException("Error: Post Dated Check - Transaction Voided");
            } else if (getBalance(transactionTicket).getPreTransactionBalance() < check.getCheckAmount()) {
                throw new InsufficientFundsException("Error: Insufficient Funds - Bounced Check");
            } else if (check.getCheckAmount() < 0) {
                throw new InvalidAmountException(check.getCheckAmount());
            } else if (check.getDateOfCheck().before(cal)) {
                return new TransactionReceipt(transactionTicket, false, "Check Too Old", getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
            } else if (getBalance(transactionTicket).getPreTransactionBalance() < 2500){
                return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance() - check.getCheckAmount() - 1.50, null);
            } else {
                return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance() - check.getCheckAmount(), null);
            }
        } catch(InvalidAccountTypeException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(AccountClosedException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch(PostDatedCheckException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        } catch (InsufficientFundsException e) {
            return new TransactionReceipt(transactionTicket, false, e.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() - 2.50, null);
        }
    }

    //getCheck() is an accessor method that returns a Check object, check
    public Check getCheck() {
        Check c = new Check(check);
        return c;
    }

    //overridden toString() method displays the depositor's attributes
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
     *     + check
     *   - returns true if all of the fields are equal and false otherwise
     */
    public boolean equals(Object o) {
        if(!(o instanceof Check)) {
            return false;
        }
        CheckingAccount ca= (CheckingAccount) o;
        return this.getAcctNumber() == ca.getAcctNumber() && this.getAcctType().equals(ca.getAcctType()) && this.getAccountStatus().equals(ca.getAccountStatus()) && this.getDepositor()== ca.getDepositor() && this.getBalance(new TransactionTicket(Calendar.getInstance(), getAcctNumber(), "", 0 , 0)) == ca.getBalance(new TransactionTicket(Calendar.getInstance(), getAcctNumber(), "", 0 , 0)) && this.check == ca.check;
    }
}