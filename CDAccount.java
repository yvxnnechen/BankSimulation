import java.util.*;

public class CDAccount extends Account {
    private Calendar maturityDate;

    public CDAccount(Calendar maturityDate, Depositor depositor, int accountNumber, String accountType, String accountStatus, double accountBalance) {
        super(depositor, accountNumber, accountType, accountStatus, accountBalance);
        this.maturityDate = maturityDate;
    }

    //Copy Constructor
    public CDAccount(CDAccount cd) {
        this(cd.getMaturityDate(new TransactionTicket(Calendar.getInstance(), cd.getAcctNumber(), "", 0 , 0)), cd.getDepositor(), cd.getAcctNumber(), cd.getAcctType(), cd.getAccountStatus(), cd.getBalance(new TransactionTicket(Calendar.getInstance(), cd.getAcctNumber(), "", 0 , 0)).getPreTransactionBalance());
        this.maturityDate = cd.maturityDate;
    }

    //makeDeposit() method accepts a reference to TransactionTicket object
    public TransactionReceipt makeDeposit(TransactionTicket transactionTicket) {
        try {
            //searches for the account
            int index = Bank.findAcct(transactionTicket.getAcctNumber());
            if (index == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            } else if (transactionTicket.getDateOfTransaction().before(maturityDate)) {
                return new TransactionReceipt(transactionTicket, false, "CD Maturity Date", getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), getMaturityDate(transactionTicket));
            } else if (transactionTicket.getAmountOfTransaction() < 0) {
                throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
            } else if (this.getAccountStatus().equals("Closed")) {
                throw new AccountClosedException(transactionTicket.getAcctNumber());
            } else {
                //changes the maturity date based on the number of months entered by the depositor
                maturityDate.add(Calendar.MONTH, transactionTicket.getTermOfCD());
                return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() + transactionTicket.getAmountOfTransaction(), maturityDate);
            }
        } catch (InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), getMaturityDate(transactionTicket));
        } catch (InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, "Invalid Amount", getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), getMaturityDate(transactionTicket));
        } catch (AccountClosedException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), null);
        }
    }

    //makeWithdrawal() method accepts a reference to TransactionTicket object
    public TransactionReceipt makeWithdrawal(TransactionTicket transactionTicket) {
        try {
            int index = Bank.findAcct(transactionTicket.getAcctNumber());
            if (index == -1) {
                throw new InvalidAccountException(transactionTicket.getAcctNumber());
            }
                if (transactionTicket.getDateOfTransaction().before(maturityDate)) {
                    Calendar cal = this.getMaturityDate(transactionTicket);
                    String maturityDate = (cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                    throw new CDMaturityDateException(maturityDate);
                } else if (getBalance(transactionTicket).getPostTransactionBalance() < transactionTicket.getAmountOfTransaction()) {
                    throw new InsufficientFundsException("Error: Insufficient Funds - Transaction Voided");
                } else if (transactionTicket.getAmountOfTransaction() < 0) {
                    throw new InvalidAmountException(transactionTicket.getAmountOfTransaction());
                } else {
                    //changes the maturity date based on the number of months entered by the depositor
                    maturityDate.add(Calendar.MONTH, transactionTicket.getTermOfCD());
                    return new TransactionReceipt(transactionTicket, true, null, getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance() - transactionTicket.getAmountOfTransaction(), maturityDate);
                }
        } catch(InvalidAccountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance(), getMaturityDate(transactionTicket));
        } catch(InvalidAmountException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPostTransactionBalance(), getMaturityDate(transactionTicket));
        } catch(InsufficientFundsException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), getMaturityDate(transactionTicket));
        } catch(CDMaturityDateException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), getBalance(transactionTicket).getPreTransactionBalance(), getBalance(transactionTicket).getPreTransactionBalance(), maturityDate);
        }
    }

    // getMaturitydate() method is an accessor method and accepts a reference to TransactionTicket object
    public Calendar getMaturityDate(TransactionTicket transactionTicket) {
        return maturityDate;
    }

    //overridden toString() method
    public String toString() {
        Calendar cal = this.getMaturityDate(new TransactionTicket(Calendar.getInstance(), this.getAcctNumber(), "", 0, 0));
        String maturityDate = (cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
        return String.format("%9s\t%14s\t%26s\t%18d\t%16s\t%18s\t%11.2f%17s",
                this.getDepositor().getName().getLastName(),
                this.getDepositor().getName().getFirstName(),
                this.getDepositor().getSSN(),
                this.getAcctNumber(),
                this.getAcctType(),
                this.getAccountStatus(),
                this.getBalance(new TransactionTicket(Calendar.getInstance(), this.getAcctNumber(), "", 0, 0)).getPostTransactionBalance(),
                maturityDate);
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
     *     + maturity date
     *   - returns true if all of the fields are equal and false otherwise
     */
    public boolean equals(Object o) {
        if (!(o instanceof CDAccount)) {
            return false;
        }
        CDAccount cd = (CDAccount) o;
        return this.getAcctNumber() == cd.getAcctNumber() && this.getAcctType().equals(cd.getAcctType()) && this.getAccountStatus().equals(cd.getAccountStatus()) && this.getDepositor()== cd.getDepositor() && this.getBalance(new TransactionTicket(Calendar.getInstance(), this.getAcctNumber(), "",  0 , 0)) == cd.getBalance(new TransactionTicket(Calendar.getInstance(), cd.getAcctNumber(), "",  0 , 0)) && this.maturityDate.get(Calendar.MONTH) == cd.maturityDate.get(Calendar.MONTH) && this.maturityDate.get(Calendar.DATE) == cd.maturityDate.get(Calendar.DATE) && this.maturityDate.get(Calendar.YEAR) == cd.maturityDate.get(Calendar.YEAR);
    }

}
