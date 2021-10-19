import java.util.*;

public class Account {
    private Depositor depositor;
    private int accountNumber;
    private String accountType;
    private String accountStatus;
    private double accountBalance;
    private ArrayList<TransactionReceipt> transactionReceipts;
    //Keeps every check the user tries to clear in this ArrayList
    private ArrayList<Check> checks;

    public Account() {
        //initialization of ArrayLists
        this.transactionReceipts = new ArrayList<>();
        this.checks = new ArrayList<>();
    }

    public Account(Depositor depositor, int accountNumber, String accountType, String accountStatus, double accountBalance) {
        //initialization of ArrayLists
        this.transactionReceipts = new ArrayList<>();
        this.checks = new ArrayList<>();
        this.depositor = depositor;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.accountBalance = accountBalance;
    }

    //Copy Constructor
    public Account(Account acc) {
        this(acc.getDepositor(), acc.getAcctNumber(), acc.getAcctType(), acc.getAccountStatus(), acc.accountBalance);
        //initialization of ArrayLists
        this.transactionReceipts = new ArrayList<>();
        this.checks = new ArrayList<>();
    }

    //setBalance() method accepts a reference to a TransactionReceipt object
    public void setBalance(TransactionReceipt transactionReceipt) {
        //after every transaction, the account balance will change accordingly
        accountBalance = transactionReceipt.getPostTransactionBalance();
    }

    //getBalance() methods accepts a reference to TransactionTicket object

    public TransactionReceipt getBalance(TransactionTicket transactionTicket) {
        return new TransactionReceipt(transactionTicket, true, null, accountBalance, accountBalance, getMaturityDate(transactionTicket));
    }

    /*
     * makeDeposit() method accepts:
     *   - transactionTicket: reference to TransactionTicket object
     * process:
     *   - calls transactionTicket.getAmountOfTransaction()
     *   - if the amount is less than 0, a new TransactionReceipt object is returned indicating that the
     *     transaction was not successful because it is an invalid amount
     *   - otherwise, a new Transaction object is returned indicating that the transaction was successful
     *     and adds the amount of the transaction to the account balance
     */
    public TransactionReceipt makeDeposit(TransactionTicket transactionTicket) throws InvalidAccountException, InvalidAmountException, AccountClosedException {
        //a deposit is only made if none of these conditions apply, otherwise the balance stays the same
        try {
            int index = Bank.findAcct(transactionTicket.getAcctNumber());
            if (index == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            } else if (transactionTicket.getAmountOfTransaction() < 0) {
                throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
            } else if (this.getAccountStatus().equals("Closed")) {
                throw new AccountClosedException(transactionTicket.getAcctNumber());
            }
                return new TransactionReceipt(transactionTicket, true, null, accountBalance, accountBalance + transactionTicket.getAmountOfTransaction(), getMaturityDate(transactionTicket));
        } catch(InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, null);
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, null);
        } catch(AccountClosedException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, null);
        }
    }

    /*
     * makeWithdrawal() method accepts:
     *   - transactionTicket: reference to TransactionTicket object
     * process:
     *   - if the balance of the account is less than the amount returned by transactionTicket.getAmountOfTransaction(),
     *     a new TransactionReceipt object is returned indicating it failed for insufficient funds. No change is made to the balance
     *   - if the amount is less than 0, a new TransactionReceipt object is returned indicating that the
     *     transaction failed because it is an invalid amount
     *   - otherwise, a new TransactionReceipt is returned indicating that the transaction was successful
     */
    public TransactionReceipt makeWithdrawal(TransactionTicket transactionTicket) throws InsufficientFundsException, InvalidAmountException{
        //a withdrawal is only made if none of these conditions apply, otherwise the balance stays the same
        //if the account balance is less than 2500, a fee of 1.50 is applied
        try {
            if (getBalance(transactionTicket).getPostTransactionBalance() < transactionTicket.getAmountOfTransaction()) {
                throw new InsufficientFundsException("Error: Insufficient Funds - Transaction Voided");
            } else if (transactionTicket.getAmountOfTransaction() < 0) {
                throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
            } else if (this.getAccountStatus().equals("Closed")) {
                throw new AccountClosedException(transactionTicket.getAcctNumber());
            } return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() - transactionTicket.getAmountOfTransaction(), getMaturityDate(transactionTicket));
        } catch(InsufficientFundsException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        } catch (AccountClosedException e) {
            return new TransactionReceipt(transactionTicket, false, e.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        }
    }

    /*
     * clearCheck() methods accepts:
     *   - check: reference to Check object
     *   - transactionTicket: reference to TransactionTicket object
     */
    public TransactionReceipt clearCheck(Check check, TransactionTicket transactionTicket) {
        try {
            //First Calendar object calendar stores the current date
            Calendar calendar = Calendar.getInstance();
            //Second Calendar object cal stores the date 6 months ago from today
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -6);
            if (check.getDateOfCheck().after(calendar)) {
                throw new PostDatedCheckException("Error: Post Dated Check - Transaction Voided");
            } else if (accountBalance < check.getCheckAmount()) {
                throw new InsufficientFundsException("Error: Insufficient Funds - Bounced Check");
            } else if (check.getCheckAmount() < 0) {
                throw new InvalidAmountException(check.getCheckAmount());
            } else if (check.getDateOfCheck().before(cal)) {
                return new TransactionReceipt(transactionTicket, false, "Check Too Old", accountBalance, accountBalance, getMaturityDate(transactionTicket));
            } else if (!(this.getAcctType().equals("Checking"))) {
                return new TransactionReceipt(transactionTicket, false, "Error: Invalid Account Type - Transaction Voided", accountBalance, accountBalance, getMaturityDate(transactionTicket));
            } else {
                return new TransactionReceipt(transactionTicket, true, null, accountBalance, accountBalance, getMaturityDate(transactionTicket));
            }
        } catch(PostDatedCheckException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        } catch(InsufficientFundsException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance -  2.50, getMaturityDate(transactionTicket));
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        }
    }

    //closeAcct() methods accepts a reference to TransactionTicket object
    public TransactionReceipt closeAcct(TransactionTicket transactionTicket) {
        try {
            //searches for the account
            int a = Bank.findAcct(transactionTicket.getAcctNumber());
            if (a == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            } else if (accountBalance == 0) {
                //changes the account status if the account balance is 0
                this.accountStatus = "closed";
                return new TransactionReceipt(transactionTicket, true, null, 0, 0, null);
            } else {
                throw new BalanceInAccountException(transactionTicket.getAcctNumber());
            }
        } catch (InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        } catch(BalanceInAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        }

    }
    // reopenAcct() methods accepts a reference to TransactionTicket object
    public TransactionReceipt reopenAcct(TransactionTicket transactionTicket) {
        try {
            //searches for the account
            int a = Bank.findAcct(transactionTicket.getAcctNumber());
            if (a == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            } else {
                //account status is changed
                this.accountStatus = "Opened";
                return new TransactionReceipt(transactionTicket, true, null, accountBalance, accountBalance, getMaturityDate(transactionTicket));
            }
        } catch(InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), accountBalance, accountBalance, getMaturityDate(transactionTicket));
        }
    }

    //addTransaction() method accepts a reference to TransactionReceipt object
    public void addTransaction(TransactionReceipt transactionReceipt) {
        transactionReceipts.add(transactionReceipt);
    }

    //addCheck accepts a Check object to add it to the ArrayList of Checks
    public void addCheck(Check c) {
        checks.add(c);
    }

    //getChecks() is an accessor method that returns an ArrayList of Checks
    public ArrayList<Check> getChecks() {
        return checks;
    }
    //getTransactionHistory() accepts a reference to TransactionTicket object
    public ArrayList<TransactionReceipt> getTransactionHistory(TransactionTicket transactionTicket) {
        //adds the TransactionTicket reference that was passed into the method and adds it to the Transaction History
        transactionReceipts.add(new TransactionReceipt(transactionTicket, true, null, accountBalance, accountBalance, getMaturityDate(transactionTicket)));
        //returns the ArrayList of TransactionReceipt
        return transactionReceipts;
    }

    //etDepositor() is an accessor method that returns the depositor
    public Depositor getDepositor() {
        Depositor dep = new Depositor(depositor);
        return dep;
    }

    //getAcctNumber() is an accessor method that returns the account number
    public int getAcctNumber() {
        return accountNumber;
    }

    //getAcctType() is an accessor method that returns the account type
    public String getAcctType() {
        return accountType;
    }

    //setAccountStatus() is a mutator method that sets the class variable accountStatus to the accountStatus variable passed in
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    //getAccountStatus() is an accessor method that returns the account status
    public String getAccountStatus() {
        return accountStatus;
    }

    /*
     * getMaturityDate() method accepts:
     *   - transactionTicket: reference to TransactionTicket object
     * process:
     *   - transactionTicket.getTermOfCD() returns the number of months the customer wants added to their current maturity date
     *   - calendar.add() is called to add these months to the month of the current maturity date
     *   - the calendar object is returned
     */
    public Calendar getMaturityDate(TransactionTicket transactionTicket) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, transactionTicket.getTermOfCD());
        return calendar;
    }

    //overridden toString() method
    public String toString() {
        return this.toString();
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
        return this.accountNumber == a.accountNumber && this.accountType.equals(a.accountType) && this.accountStatus.equals(a.accountStatus) && this.depositor == a.depositor && this.accountBalance == a.accountBalance ;
    }
}