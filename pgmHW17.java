import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;

public class pgmHW17 {
    public static void main(String[] args) throws IOException {
        Bank bank = new Bank();
        char choice;
        boolean notDone = true;
        File testFile = new File("myTestCases.txt");
        File initialFile = new File("initialAccounts.txt");
        Scanner kybd = new Scanner(testFile);
        Scanner inFile = new Scanner(initialFile);
        PrintWriter outFile = new PrintWriter("myOutput.txt");
        // reads in the initial accounts into the database and prints them
        readAccts(bank, inFile);
        printAccts(bank, outFile);
        Bank.totalAmounts();
        outFile.printf("Total Amount in Savings Accounts: %.2f\n", bank.getTotalAmountInSavingsAccts());
        outFile.printf("Total Amount in Checking Accounts: %.2f\n", bank.getTotalAmountInCheckingAccts());
        outFile.printf("Total Amount in CD Accounts: %.2f\n", bank.getTotalAmountInCDAccts());
        outFile.printf("Total Amount in All Accounts: %.2f\n", bank.getTotalAmountInAllAccts());
        outFile.println();
        printAcctsByAcctNumSortKey(bank, outFile);
        printAcctsBySSNSortKey(bank, outFile);
        printAcctsByNameSortKey(bank, outFile);
        printAcctsByBalanceSortKey(bank, outFile);
        // calculates the total amounts of each account and prints them
        // prompts the user to enter a letter corresponding to their desired transaction and calls the functions to process this
        do {
            menu();
            choice = kybd.next().charAt(0);
            try {
                switch(choice) {
                    case 'q':
                    case 'Q':
                        notDone = false;
                        printAccts(bank, outFile);
                        break;
                    case 'b':
                    case 'B':
                        balance(bank, outFile, kybd);
                        break;
                    case 'w':
                    case 'W':
                        withdrawal(bank, outFile, kybd);
                        break;
                    case 'd':
                    case 'D':
                        deposit(bank, outFile, kybd);
                        break;
                    case 'c':
                    case 'C':
                        clearCheck(bank, outFile, kybd);
                        break;
                    case 'n':
                    case 'N':
                        newAcct(bank, outFile, kybd);
                        break;
                    case 's':
                    case 'S':
                        closeAcct(bank, outFile, kybd);
                        break;
                    case 'r':
                    case 'R':
                        reopenAcct(bank, outFile, kybd);
                        break;
                    case 'x':
                    case 'X':
                        deleteAcct(bank, outFile, kybd);
                        break;
                    case 'i':
                    case 'I':
                        accountInfo(bank, outFile, kybd);
                        break;
                    case 'h':
                    case 'H':
                        accountInfoWithTransactionHistory(bank, outFile, kybd);
                        break;
                    default:
                        throw new InvalidMenuSelectionException(choice);
                }
            } catch (InvalidMenuSelectionException ex) {
                outFile.println(ex.getMessage());
                outFile.println();
                outFile.flush();
            } catch(InvalidAccountException invalidAccountException) {
                outFile.println(invalidAccountException.getMessage());
                outFile.println();
                outFile.flush();
            } catch(AccountClosedException ace) {
                outFile.println(ace.getMessage());
                outFile.println();
                outFile.flush();
            } catch (InsufficientFundsException ife) {
                outFile.println(ife.getMessage());
                outFile.println();
                outFile.flush();
            } catch (InvalidAmountException iae) {
                outFile.println(iae.getMessage());
                outFile.println();
                outFile.flush();
            } catch(CheckTooOldException ctoe) {
                outFile.println(ctoe.getMessage());
                outFile.println();
                outFile.flush();
            } catch(BalanceInAccountException bia) {
                outFile.println(bia.getMessage());
                outFile.println();
                outFile.flush();
            } catch(CDMaturityDateException cdmd) {
                outFile.println(cdmd.getMessage());
                outFile.println();
                outFile.flush();
            } catch(AccountOpenedException aoe) {
                outFile.println(aoe.getMessage());
                outFile.println();
                outFile.flush();
            } catch(PostDatedCheckException pdce) {
                outFile.println(pdce.getMessage());
                outFile.println();
                outFile.flush();
            } catch(InvalidAccountTypeException iaete) {
                outFile.println(iaete.getMessage());
                outFile.println();
                outFile.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (notDone);
        //print statements for sorted account numbers, social security numbers, and last names
        //finds the total amount in every Savings account, in every Checking account, in every CD Account, and the sum of them
        Bank.totalAmounts();
        outFile.printf("Total Amount in Savings Accounts: %.2f\n", bank.getTotalAmountInSavingsAccts());
        outFile.printf("Total Amount in Checking Accounts: %.2f\n", bank.getTotalAmountInCheckingAccts());
        outFile.printf("Total Amount in CD Accounts: %.2f\n", bank.getTotalAmountInCDAccts());
        outFile.printf("Total Amount in All Accounts: %.2f\n", bank.getTotalAmountInAllAccts());
        outFile.println();
        Bank.sortAccounts();
        printAcctsByAcctNumSortKey(bank, outFile);
        printAcctsBySSNSortKey(bank, outFile);
        printAcctsByNameSortKey(bank, outFile);
        printAcctsByBalanceSortKey(bank, outFile);
        outFile.println();
        outFile.close();
        kybd.close();
        System.out.println();
        System.out.println("The program is terminating");
    }

    /*
     * readAccts() method accepsts:
     *   - bank : reference to Bank Object
     *   - infile: reference to the input file "initialAccounts.txt"
     * process:
     *   - reads the initial database of accounts by calling bank.openNewAcct() method
     *   - if the account type is CD, the program will prompt the user to enter a maturity date
     *   - the transaction is then added to the account's transaction history
     * output:
     *   - Fills in the initial array of Account objects within the Bank object
     */
    public static void readAccts(Bank bank, Scanner inFile) {
        int i = 0;
        while(inFile.hasNext()) {
            String firstName = inFile.next();
            String lastName = inFile.next();
            String SSN = String.valueOf(inFile.nextInt());
            int accountNumber = inFile.nextInt();
            String accountType = inFile.next();
            double balance = inFile.nextDouble();
            TransactionReceipt transactionReceipt;
            if(accountType.equalsIgnoreCase("CD")) {
                String maturityDate = inFile.next();
                Calendar c = Calendar.getInstance();
                String[] s = maturityDate.split("/");
                c.set(Calendar.MONTH, Integer.parseInt(s[0]) -1);
                c.set(Calendar.DATE, Integer.parseInt(s[1]));
                c.set(Calendar.YEAR, Integer.parseInt(s[2]));
                transactionReceipt = bank.openNewAcct(new TransactionTicket(Calendar.getInstance(), accountNumber, "Open New Account", balance, 0), new Depositor(new Name(firstName, lastName), SSN), accountNumber, accountType, "Opened", balance, c);
            } else {
                transactionReceipt = bank.openNewAcct(new TransactionTicket(Calendar.getInstance(), accountNumber, "Open New Account", balance, 0), new Depositor(new Name(firstName, lastName), SSN), accountNumber, accountType, "Opened", balance, null);
            }
            //TransactionReceipt is created when opening a new account
            //this transaction is added to the ArrayList of Transaction Receipts in the Bank class
            bank.getListOfAccounts().get(i).addTransaction(transactionReceipt);
            i++;
        }
    }

    /* menu()
     * process: prints the menu of transaction choices
     */
    public static void menu() {
        System.out.println("Select one of the following: ");
        System.out.println("W - Withdrawal");
        System.out.println("D - Deposit");
        System.out.println("C - Clear Check");
        System.out.println("N - New Account");
        System.out.println("B - Balance");
        System.out.println("I - Account Info");
        System.out.println("H - Account Info plus Account Transaction History");
        System.out.println("S - Close Account");
        System.out.println("R - Reopen Account");
        System.out.println("X - Delete Account");
        System.out.println("Q - Quit");
    }

    /* balance()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for requested account
     *   - finds the account and sets it equal to a
     *   - if the account reference is equal to null, an InvalidAccountException is thrown
     *   - otherwise, a TransactionReceipt object is created when calling a.getBalance() method
     * output:
     *   - if the success indicator flag is true, the account number and current balance are printed
     */
    public static void balance(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountException {
        int accountNumber = inFile.nextInt();
        int index = Bank.findAcct(accountNumber);
        //a is a reference to the original account
        Account a = Bank.getAcct(index);
        if(a == null) {
            outFile.println("Transaction Requested: Balance Inquiry");
            throw new InvalidAccountException(accountNumber);
        } else {
            //TransactionTicket object is created to pass into the getBalance() method
            TransactionTicket tt = new TransactionTicket(Calendar.getInstance(), accountNumber, "Balance Inquiry", 0,  0);
            TransactionReceipt tr = a.getBalance(tt);
            outFile.println(tr + "\n");
            //the transaction is then added to the account's transaction history
            bank.getListOfAccounts().get(index).addTransaction(tr);
        }
    }

    /* deposit()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for requested account, the amount the customer wants to deposit, and the desired new maturity date in months
     *   - the program searches for the account matching the input account number
     *   - if the account is null, an InvalidAccountException is thrown
     *   - calls a.makeDeposit()
     *   - the new CD Maturity date is also adjusted accordingly based on the number of months previously entered by the user
     * output:
     *   - prints the transaction type and account number
     *   - if the transaction is successful, it also prints the current balance, amount to deposit, and the new balance
     */
    public static void deposit(Bank bank, PrintWriter outFile, Scanner inFile) throws CDMaturityDateException, InvalidAccountException, InvalidAmountException, AccountClosedException {
        int accountNumber = inFile.nextInt();
        double amount = inFile.nextDouble();
        int months = inFile.nextInt();
        String date = "";
        int index = Bank.findAcct(accountNumber);
        //a is a reference to the actual account
        Account a = Bank.getAcct(index);
        if(a == null) {
            outFile.println("Transaction Type: Deposit");
            throw new InvalidAccountException(accountNumber);
        } else {
            TransactionReceipt transactionReceipt = a.makeDeposit(new TransactionTicket(Calendar.getInstance(), accountNumber, "Deposit", amount, months));
            //adds transaction to transaction history of that account
            bank.getListOfAccounts().get(index).addTransaction(transactionReceipt);
            //calls the toString() method and prints account number
            if(transactionReceipt.getTransactionSuccessIndicatorFlag()) {
                //adjusts the balance to match the deposited amount
                bank.getListOfAccounts().get(index).setBalance(transactionReceipt);
                a.setBalance(transactionReceipt);
                //prints the transaction
                outFile.println(transactionReceipt);
                outFile.println();
            } else {
                outFile.println(transactionReceipt);
                if(transactionReceipt.getTransactionFailureReason().equalsIgnoreCase("CD Maturity Date")) {
                    //created a String date with the month, day, and year associated with the object Calendar cal
                    Calendar cal = a.getMaturityDate(transactionReceipt.getTransactionTicket());
                    date = (cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                    throw new CDMaturityDateException(date);
                } else {
                    outFile.println(transactionReceipt.getTransactionFailureReason() + "\n");
                }
            }
        }
    }

    /* withdrawal()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for requested account, the amount the customer wants to deposit, and the desired new maturity date in months
     *   - the program searches for the account matching the input account number
     *   - if the account is null, an InvalidAccountException is thrown
     *   - calls a.makeWithdrawal()
     *   - the new CD Maturity date is also adjusted accordingly based on the number of months previously entered by the user
     * output:
     *   - prints the transaction type and account number
     *   - if the transaction is successful, it also prints the current balance, amount to withdraw, and the new balance
     */
    public static void withdrawal(Bank bank, PrintWriter outFile, Scanner inFile) throws InsufficientFundsException, AccountClosedException, InvalidAmountException, CDMaturityDateException, InvalidAccountException {
        int accountNumber = inFile.nextInt();
        double amount = inFile.nextDouble();
        int months = inFile.nextInt();
        String date;
        int index = Bank.findAcct(accountNumber);
        //a is a reference to the actual account
        Account a = Bank.getAcct(index);
        if(a == null)  {
            outFile.println("Transaction Type: Withdrawal");
            throw new InvalidAccountException(accountNumber);
        }
        else {
            TransactionReceipt transactionReceipt = a.makeWithdrawal(new TransactionTicket(Calendar.getInstance(), accountNumber, "Withdrawal", amount, months));
            //TransactionReceipt added to the transaction history of the account
            bank.getListOfAccounts().get(index).addTransaction(transactionReceipt);
            if(transactionReceipt.getTransactionSuccessIndicatorFlag()) {
                //calls the toString() method and prints account number and transaction
                outFile.println(transactionReceipt);
                outFile.println();
                //adjusts the balance of the account to match the amount that was withdrawn
                bank.getListOfAccounts().get(index).setBalance(transactionReceipt);
                a.setBalance(transactionReceipt);
            } else {
                outFile.println(transactionReceipt);
                outFile.println(transactionReceipt.getTransactionFailureReason()+ "\n");
            }
        }
    }

    /* clearCheck()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for the account number, check amount, and date of check
     *   - the date is formatted into a Calendar object
     *   - the program searches for the account matching the input account number
     *   - if the account is null, an InvalidAccountException is thrown
     *   - calls a.clearCheck()
     *   - the transaction is then added to the account's transaction history
     *   - if the transaction is successful, the amount of the check is withdrawn from the balance
     * output:
     *   - prints the transaction type and account number
     *   - if the transaction is successful or has been charged a bounce fee, it also prints the current balance, amount to withdraw, and the new balance
     */
    public static void clearCheck(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountException, InsufficientFundsException, CheckTooOldException, PostDatedCheckException, InvalidAmountException, AccountClosedException, InvalidAccountTypeException {
        int accountNumber = inFile.nextInt();
        double amount = inFile.nextDouble();
        int index = Bank.findAcct(accountNumber);
        String date = inFile.next();
        Calendar cal = Calendar.getInstance();
        String[] s = date.split("/");
        // setting the date of the cal object to match that of the check date
        cal.set(Calendar.MONTH, Integer.parseInt(s[0]) -1);
        cal.set(Calendar.DATE, Integer.parseInt(s[1]));
        cal.set(Calendar.YEAR, Integer.parseInt(s[2]));
        Account a = Bank.getAcct(index);
        Check check = new Check(accountNumber, amount, cal);
        if (a == null) {
            outFile.println("Transaction Type: Clear Check");
            throw new InvalidAccountException(accountNumber);
        } else {
            TransactionReceipt tr = a.clearCheck(check, new TransactionTicket (Calendar.getInstance(), accountNumber, "Clear Check", amount, 0));
            //adds TransactionReceipt object to the transaction history of the account
            bank.getListOfAccounts().get(index).addTransaction(tr);
            //adds Check object to the ArrayList of Checks for that account
            bank.getListOfAccounts().get(index).addCheck(check);
            //calls the toString() method and prints account number
            if(tr.getTransactionSuccessIndicatorFlag()) {
                //changes the balance of the account
                Bank.getListOfAccounts().get(index).setBalance(tr);
                //calls the toString() method of class TransactionReceipt and prints the transaction
                outFile.println(tr + "\n");
                a.setBalance(tr);
                bank.getListOfAccounts().get(index).setBalance(tr);
            } else {
                outFile.println("Transaction Type: Clear Check");
                outFile.println("Account Number: " + accountNumber);
                outFile.println("Current Balance: " + tr.getPreTransactionBalance());
                outFile.println("Amount to Withdraw: " + amount);
                outFile.println("New Balance: " + tr.getPostTransactionBalance());
                bank.getListOfAccounts().get(index).setBalance(tr);
                if (tr.getTransactionFailureReason().equalsIgnoreCase("Check Too Old")) {
                    throw new CheckTooOldException(date);
                }
                outFile.println(tr.getTransactionFailureReason() + "\n");
            }
        }
    }

    /* newAcct()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - if the account type is "CD", program prompts user to enter a maturity date
     *   - calls Bank.openNewAcct()
     *   - if the transaction is successful, it is added to the ArrayList of TransactionReceipts
     * output:
     *    prints the transaction type, account number, and success if the transaction went through
     */
    public static void newAcct(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountTypeException {
        TransactionReceipt transactionReceipt;
        //reads in the account attributes
        int accountNumber = inFile.nextInt();
        String firstName = inFile.next();
        String lastName = inFile.next();
        String SSN = inFile.next();
        String accountType = inFile.next();
        double startingBalance = inFile.nextDouble();
        //checks to see if the account number entered already exists
        for (int p = 0; p < Bank.getListOfAccounts().size(); p++) {
            if (accountNumber == Bank.getListOfAccounts().get(p).getAcctNumber()) {
                throw new InvalidAccountTypeException(accountNumber);
            }
        }
        if (accountType.equalsIgnoreCase("CD")) {
            String maturityDate = inFile.next();
            Calendar c = Calendar.getInstance();
            String[] s = maturityDate.split("/");
            c.set(Calendar.MONTH, Integer.parseInt(s[0]) - 1);
            c.set(Calendar.DATE, Integer.parseInt(s[1]));
            c.set(Calendar.YEAR, Integer.parseInt(s[2]));
            transactionReceipt = Bank.openNewAcct(new TransactionTicket(Calendar.getInstance(), accountNumber, "Open New Account", 0, 0), new Depositor(new Name(firstName, lastName), SSN), accountNumber, accountType, "Opened", startingBalance, c);
        } else {
            transactionReceipt = Bank.openNewAcct(new TransactionTicket(Calendar.getInstance(), accountNumber, "Open New Account", 0, 0), new Depositor(new Name(firstName, lastName), SSN), accountNumber, accountType, "Opened", startingBalance, null);
        }
        if(transactionReceipt.getTransactionSuccessIndicatorFlag()) {
            outFile.println(transactionReceipt + "\n");
            int n = bank.findAcct(accountNumber);
            //adds a new TransactionReceipt to the transaction history of account at index n
            bank.getListOfAccounts().get(n).addTransaction(transactionReceipt);
        } else {
            outFile.println("Transaction Type: Open New Account");
            outFile.println("Account Number: " + accountNumber);
            outFile.println(transactionReceipt.getTransactionFailureReason() + "\n");
        }
    }

    /* deleteAcct()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - program searches for the account
     *   - calls Bank.deleteAcct()
     *   - the transaction is added to the account's transaction history
     *   - if the account does not exist, an InvalidAccountException is thrown
     *   - if there is a reason for failure and it is "Account Is Not Closed", an AccountOpenedException is thrown
     *   - if the success indicator flag is true, the transaction is added to the ArrayList of TransactionReceipts
     * output:
     *   - prints the transaction type and account number
     *   - if the transaction is successful, it also prints "Account has successfully been deleted"
     */
    public static void deleteAcct(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountException, AccountOpenedException {
        int accountNumber = inFile.nextInt();
        int index = Bank.findAcct(accountNumber);
        //a is a reference to the actual account
        Account a = Bank.getAcct(index);
        if(a == null) {
            throw new InvalidAccountException(accountNumber);
        } else {
            TransactionReceipt transactionReceipt = Bank.deleteAcct(new TransactionTicket(Calendar.getInstance(), accountNumber, "Delete Account", 0,0), accountNumber);
            // adds TransactionReceipt object to the transaction history of the account
            bank.getListOfAccounts().get(index).addTransaction(transactionReceipt);
            if (a.getAccountStatus().equals("Opened")) {
                outFile.println("Transaction Type: Delete Account");
                throw new AccountOpenedException(accountNumber);
            } else if(transactionReceipt.getTransactionSuccessIndicatorFlag()) {
                //calls the toString() method of the TransactionReceipt class to print the contents
                outFile.println(transactionReceipt + "\n");
            }
        }
    }

    /* closeAcct()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for requested account
     *   - program searches for the account
     *   - if the account does not exist, an InvalidAccountException is thrown
     *   - a.getBalance() is called to check if the balance is 0
     *   - calls Bank.closeAcct()
     *   - if the balance is 0, the account status is changed to closed
     *   - otherwise, a BalanceInAccountException is thrown
     *   - if the success indicator flag is true, the transaction is added to the ArrayList of TransactionReceipts
     * output:
     *   - prints the transaction type and account number
     *   - if the transaction is successful, it also prints "Success"
     */
    public static void closeAcct(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountException, BalanceInAccountException {
        int accountNumber = inFile.nextInt();
        int index = Bank.findAcct(accountNumber);
        //a is a reference to the actual account
        Account a = bank.getAcct(index);
        if(a == null) {
            outFile.println("Transaction Type: Close Account");
            throw new InvalidAccountException(accountNumber);
        } else{
            TransactionReceipt transactionReceipt = a.closeAcct(new TransactionTicket(Calendar.getInstance(), accountNumber, "Close Account", 0, 0));
            bank.getListOfAccounts().get(index).addTransaction(transactionReceipt);
            if(transactionReceipt.getPreTransactionBalance() == 0) {
                outFile.println(transactionReceipt + "\n");
                a.setAccountStatus("Closed");
                bank.getListOfAccounts().get(index).setAccountStatus("Closed");
            } else {
                outFile.println("Transaction Type: Close Account");
                outFile.println("Account Number: " + accountNumber);
                // if the account balance is not 0, the actual balance is printed
                outFile.printf("Current Balance: %.2f\n",transactionReceipt.getPreTransactionBalance());
                outFile.println(transactionReceipt.getTransactionFailureReason() + "\n");
            }
        }
    }
    /* reopenAcct()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for requested account
     *   - program searches for the account
     *   - calls bank.reopenAcct()
     *   - if the account does not exist, an InvalidAccountException is thrown
     *   - otherwise the account status is changed to opened
     *   - if the success indicator flag is true, the transaction is added to the ArrayList of TransactionReceipts
     * output:
     *   - prints the transaction type and account number
     *   - if the transaction is successful, it also prints "Success"
     */
    public static void reopenAcct(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountException {
        int accountNumber = inFile.nextInt();
        int index = Bank.findAcct(accountNumber);
        Account a = Bank.getAcct(index);
        if (a == null) {
            throw new InvalidAccountException(accountNumber);
        }
        else {
            TransactionReceipt transactionReceipt = a.reopenAcct(new TransactionTicket(Calendar.getInstance(), accountNumber, "Reopen Account", 0, 0));
            //Account status is changed from closed to open
            bank.getListOfAccounts().get(index).setAccountStatus("Opened");
            outFile.println("Transaction Type: " + transactionReceipt.getTransactionTicket().getTypeOfTransaction());
            outFile.println("Account Number: " + accountNumber);
            outFile.println("Success!");
            outFile.println();
            //TransactionReceipt is added to the transaction history of the account
            bank.getListOfAccounts().get(index).addTransaction(transactionReceipt);
        }
    }

    /* accountInfo()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for the user's Social Security Number
     *   - calls Bank.findAcct(), which returns an ArrayList<> of accounts matching that SSN
     *   - if there are no accounts in the ArrayList, an InvalidAccountException is thrown
     * output:
     *   - prints header that has account number, account type, account status, balance, and maturity date
     *   - prints all corresponding accounts' information
     */
    public static void accountInfo(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountException {
        String SSN = inFile.next();
        ArrayList<Account> list = Bank.findAcct(SSN);
        outFile.println("Transaction Type: Account Information");
        outFile.println("SSN: " + SSN);
        //if the list is empty, there are no accounts that have the SSN of the entered SSN
        if (list.isEmpty()) {
            throw new InvalidAccountException(SSN);
        } else {
            outFile.println("Account Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
            //print statements are separated by account type, if it is CD, a maturity date is also printed
            for(int j = 0; j < list.size(); j++) {
                if(list.get(j).getAcctType().equalsIgnoreCase("CD")) {
                    Calendar cal = bank.getListOfAccounts().get(j).getMaturityDate(new TransactionTicket(Calendar.getInstance(), 0, "", 0, 0));
                    String maturityDate = (cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                    outFile.printf("%14d\t\t%12s\t\t%14s\t\t%7.2f\t\t%12s\n", list.get(j).getAcctNumber(), list.get(j).getAcctType(), list.get(j).getAccountStatus(), list.get(j).getBalance(new TransactionTicket(Calendar.getInstance(), 0, "Account Info Inquiry", 0, 0)).getPostTransactionBalance(), maturityDate);
                }
                else {
                    outFile.printf("%14d\t\t%12s\t\t%14s\t\t%7.2f\n", list.get(j).getAcctNumber(), list.get(j).getAcctType(), list.get(j).getAccountStatus(), list.get(j).getBalance(new TransactionTicket(Calendar.getInstance(), 0, "Account Info Inquiry", 0, 0)).getPostTransactionBalance());
                }
            }
        }
        outFile.println();
    }

    /* accountInfo()
     * input:
     *   - bank: reference to the Bank Object
     *   - outFile: reference to the output file
     *   - inFile: reference to the "myTestCases.txt" input file
     * process:
     *   - prompts for the user's Social Security Number
     *   - calls Bank.findAcct(), which returns an ArrayList<> of accounts matching that SSN
     *   - if there are no accounts in the ArrayList, an InvalidAccountException is thrown
     * output:
     *   - prints header that has last name, first name, social security number, account number, account type, account status, balance, and maturity date
     *   - prints all corresponding accounts' information and their transaction history
     */
    public static void accountInfoWithTransactionHistory(Bank bank, PrintWriter outFile, Scanner inFile) throws InvalidAccountException {
        String SSN = inFile.next();
        //retrieves the ArrayList containing all accounts with the same SSN
        ArrayList<Account> list = Bank.findAcct(SSN);
        String indicator;
        TransactionTicket ticket = new TransactionTicket(Calendar.getInstance(), 0, "", 0 , 0);
        Calendar today = Calendar.getInstance();
        // creating a String containing the month, day, and year retrieved from the Calendar object today
        String dateOfToday = (today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR);
        outFile.println("Account Information with Transaction History");
        outFile.println("SSN: " + SSN);
        if (list.isEmpty()) {
            throw new InvalidAccountException(SSN);
        }
        //for every Account object in the Arra    yList of accounts that have the same SSN
        for (Account account : list) {
            outFile.println("\nLast Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tStatus\t\tBalance\t\tMaturity Date");
            if (account.getAcctType().equals("CD")) {
                Calendar cal = account.getMaturityDate(new TransactionTicket(Calendar.getInstance(), 0, "", 0, 0));
                String maturityDate = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                outFile.printf("%9s\t\t%10s\t\t%22s\t\t%14d\t\t%12s\t\t%s\t\t%7.2f\t\t%13s\n", account.getDepositor().getName().getLastName(), account.getDepositor().getName().getFirstName(), account.getDepositor().getSSN(), account.getAcctNumber(), account.getAcctType(), account.getAccountStatus(), account.getBalance(ticket).getPreTransactionBalance(), maturityDate);
            } else {
                outFile.printf("%9s\t\t%10s\t\t%22s\t\t%14d\t\t%12s\t\t%s\t\t%7.2f\n", account.getDepositor().getName().getLastName(), account.getDepositor().getName().getFirstName(), account.getDepositor().getSSN(), account.getAcctNumber(), account.getAcctType(), account.getAccountStatus(), account.getBalance(ticket).getPreTransactionBalance());
            }
            outFile.println();
            outFile.println("\tDate\t\t\t   Transaction Type\t\t\tAmount\t\t\tStatus\t\t\tBalance");
            //for every TransactionReceipt object
            for (TransactionReceipt receipt : account.getTransactionHistory(new TransactionTicket(Calendar.getInstance(), 0, "Transaction History", 0, 0))) {
                if (receipt.getTransactionSuccessIndicatorFlag()) {
                    indicator = "Done";
                    outFile.printf("%s\t\t\t%19s\t\t\t%6.2f\t\t\t%6s\t\t\t%7.2f", dateOfToday, receipt.getTransactionTicket().getTypeOfTransaction(), receipt.getTransactionTicket().getAmountOfTransaction(), indicator, receipt.getPostTransactionBalance());
                } else {
                    indicator = "Failed";
                    //prints the reason for transaction failure in addition to the account details
                    outFile.printf("%s\t\t\t%19s\t\t\t%6.2f\t\t\t%6s\t\t\t%7.2f\t\t\t%12s", dateOfToday, receipt.getTransactionTicket().getTypeOfTransaction(), receipt.getTransactionTicket().getAmountOfTransaction(), indicator, receipt.getPostTransactionBalance(), receipt.getTransactionFailureReason());
                    //if the reason for failure is because of a Post-Dated Check or an old Check, the date will also be printed
                    if(receipt.getTransactionFailureReason().equalsIgnoreCase("Post Dated Check") || receipt.getTransactionFailureReason().equalsIgnoreCase("Check Too Old")) {
                        for(Check ch : account.getChecks()) {
                            if(ch.getAccountNum() == account.getAcctNumber()) {
                                outFile.print(" - " + ch);
                            }
                        }
                    }
                }
                outFile.println();
            }
        }
        outFile.println();
    }

    /* printAccts()
     *   - this method prints the initial and final database of Accounts
     */
    public static void printAccts(Bank bank, PrintWriter outFile) {
        outFile.println("Unsorted Accounts");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < bank.getListOfAccounts().size(); i++) {
            String firstName = bank.getListOfAccounts().get(i).getDepositor().getName().getFirstName();
            String lastName = bank.getListOfAccounts().get(i).getDepositor().getName().getLastName();
            String SSN = bank.getListOfAccounts().get(i).getDepositor().getSSN();
            int accountNumber = bank.getListOfAccounts().get(i).getAcctNumber();
            String accountType = bank.getListOfAccounts().get(i).getAcctType();
            String accountStatus = bank.getListOfAccounts().get(i).getAccountStatus();
            double balance = bank.getListOfAccounts().get(i).getBalance(new TransactionTicket(Calendar.getInstance(), accountNumber, "", 0, 0)).getPostTransactionBalance();
            if(accountType.equalsIgnoreCase("CD")) {
                Calendar cal = bank.getListOfAccounts().get(i).getMaturityDate(new TransactionTicket(Calendar.getInstance(), accountNumber, "", 0, 0));
                String maturityDate = (cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                outFile.printf("%9s\t%14s\t%26s\t%18d\t%16s\t%18s\t%11.2f%17s\n", lastName, firstName, SSN, accountNumber, accountType, accountStatus, balance, maturityDate);
            }  else {
                outFile.printf("%9s\t%14s\t%26s\t%18d\t%16s\t%18s\t%11.2f\n", lastName, firstName, SSN, accountNumber, accountType, accountStatus, balance);
            }
        }
        outFile.println();
    }

    /*
     * The methods below print the Bank database depending on the sort and attribute
     */
    public static void printAcctsByAcctNumSortKey (Bank bank, PrintWriter outFile) {
        outFile.println("Account Numbers Quick Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctByAcctNumSortKey(bank.getAcctNumQuickSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("Account Numbers Bubble Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc =  Bank.getAcctByAcctNumSortKey(bank.getAcctNumBubbleSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("Account Numbers Insertion Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc =  Bank.getAcctByAcctNumSortKey(bank.getAcctNumInsertionSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
    }

    public static void printAcctsBySSNSortKey(Bank bank, PrintWriter outFile) {
        outFile.println("SSN Quick Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctBySSNSortKey(bank.getSsnQuickSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("SSN Bubble Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctBySSNSortKey(bank.getSsnBubbleSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("SSN Insertion Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctBySSNSortKey(bank.getSsnInsertionSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
    }

    public static void printAcctsByNameSortKey(Bank bank, PrintWriter outFile) {
        outFile.println("Name Quick Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctByName(bank.getNameQuickSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("Name Bubble Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctByName(bank.getNameBubbleSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("Name Insertion Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctByName(bank.getNameInsertionSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
    }

    public static void printAcctsByBalanceSortKey(Bank bank, PrintWriter outFile) {
        outFile.println("Balance Quick Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctByBalanceSortKey(bank.getBalanceQuickSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("Balance Bubble Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctByBalanceSortKey(bank.getBalanceBubbleSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
        outFile.println("Balance Insertion Sort");
        outFile.println("Last Name\t\tFirst Name\t\tSocial Security Number\t\tAccount Number\t\tAccount Type\t\tAccount Status\t\tBalance\t\tMaturityDate");
        for(int i = 0; i < Bank.getNumAccts(); i++) {
            Account acc = Bank.getAcctByBalanceSortKey(bank.getBalanceInsertionSortKey().get(i));
            outFile.println(acc);
        }
        outFile.println();
    }

}