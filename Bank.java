import java.util.ArrayList;
import java.util.Calendar;

public class Bank {
    private static double totalAmountInSavingsAccts;
    private static double totalAmountInCheckingAccts;
    private static double totalAmountInCDAccts;
    private static double totalAmountInAllAccts;
    private static ArrayList<Account> listOfAccounts;
    private static ArrayList<Integer> acctNumQuickSortKey;
    private static ArrayList<Integer> acctNumBubbleSortKey;
    private static ArrayList<Integer> acctNumInsertionSortKey;
    private static ArrayList<Integer> ssnQuickSortKey;
    private static ArrayList<Integer> ssnBubbleSortKey;
    private static ArrayList<Integer> ssnInsertionSortKey;
    private static ArrayList<Integer> nameQuickSortKey;
    private static ArrayList<Integer> nameBubbleSortKey;
    private static ArrayList<Integer> nameInsertionSortKey;
    private static ArrayList<Integer> balanceQuickSortKey;
    private static ArrayList<Integer> balanceBubbleSortKey;
    private static ArrayList<Integer> balanceInsertionSortKey;

    //Bank() is a no-args constructor that initializes all static variables and new ArrayLists for the list of accounts and sort keys
    public Bank() {
        totalAmountInSavingsAccts = 0;
        totalAmountInCheckingAccts = 0;
        totalAmountInCDAccts = 0;
        totalAmountInAllAccts = 0;
        listOfAccounts = new ArrayList<>();
        //Initialization of sorting keys
        acctNumQuickSortKey = new ArrayList<>();
        acctNumBubbleSortKey = new ArrayList<>();
        acctNumInsertionSortKey = new ArrayList<>();
        ssnQuickSortKey = new ArrayList<>();
        ssnBubbleSortKey = new ArrayList<>();
        ssnInsertionSortKey = new ArrayList<>();
        nameQuickSortKey = new ArrayList<>();
        nameBubbleSortKey = new ArrayList<>();
        nameInsertionSortKey = new ArrayList<>();
        balanceQuickSortKey = new ArrayList<>();
        balanceBubbleSortKey = new ArrayList<>();
        balanceInsertionSortKey = new ArrayList<>();
    }

    /*
     * openNewAcct() method accepts:
     *   - transactionTicket : reference to the Transaction Ticket to perform this transaction
     *   - depositor: reference to the customer trying to create an account and contains their full name and SSN
     *   - account number: the customer creates their own account number
     *   - accountType: the customer chooses which type of account they want to open
     *   - accountBalance: the customer's desired starting account balance
     *   - maturityDate: if the account type is CD, the customer must choose a maturity date, which indicates when they will be able to next perform a transaction on the account
     */
    public static TransactionReceipt openNewAcct(TransactionTicket transactionTicket, Depositor depositor, int accountNumber, String accountType, String accountStatus, double accountBalance, Calendar maturityDate) {
        try {
            TransactionReceipt tr;
            //adds a new account if the type is Checking, CD, or Savings
            switch (accountType) {
                case "CD":
                    listOfAccounts.add(new CDAccount(maturityDate, depositor, accountNumber, accountType, accountStatus, accountBalance));
                    break;
                case "Savings":
                    listOfAccounts.add(new SavingsAccount(depositor, accountNumber, accountType, accountStatus, accountBalance));
                    break;
                case "Checking":
                    listOfAccounts.add(new CheckingAccount(depositor, accountNumber, accountType, accountStatus, accountBalance));
                    break;
            }
            if (!(accountType.equalsIgnoreCase("CD") || accountType.equalsIgnoreCase("Savings") || accountType.equalsIgnoreCase("Checking"))) {
                throw new InvalidAccountTypeException(accountType);
            } else {
                tr = new TransactionReceipt(transactionTicket, true, null, 0, accountBalance, null);
                // add a new index for every new Account being added
                acctNumQuickSortKey.add(listOfAccounts.size() - 1);
                acctNumBubbleSortKey.add(listOfAccounts.size() - 1);
                acctNumInsertionSortKey.add(listOfAccounts.size() - 1);
                ssnQuickSortKey.add(listOfAccounts.size() - 1);
                ssnBubbleSortKey.add(listOfAccounts.size() - 1);
                ssnInsertionSortKey.add(listOfAccounts.size() - 1);
                nameQuickSortKey.add(listOfAccounts.size() - 1);
                nameBubbleSortKey.add(listOfAccounts.size() - 1);
                nameInsertionSortKey.add(listOfAccounts.size() - 1);
                balanceQuickSortKey.add(listOfAccounts.size() - 1);
                balanceBubbleSortKey.add(listOfAccounts.size() - 1);
                balanceInsertionSortKey.add(listOfAccounts.size() - 1);
                // accounts are sorted
                sortAccounts();
                // a TransactionReceipt is returned indicating that the transaction was successful
                return tr;
            }
        } catch(InvalidAccountTypeException ex) {
            return new TransactionReceipt(transactionTicket, false, ex.getMessage(), 0, 0, null);
        }
    }

    /*
     * deleteAcct() method accepts:
     *   - transactionTicket - reference to TransactionTicket
     *   - accountNumber - used to find if that account exists, and if it does, the account will be deleted
     */

    public static TransactionReceipt deleteAcct(TransactionTicket transactionTicket, int accountNumber) {
        int index = findAcct(accountNumber);
        Account acc = getAcct(index);
        TransactionTicket tt = new TransactionTicket(Calendar.getInstance(), accountNumber, "", 0, 0);
        TransactionReceipt tr;
        // if the account does not exist, a TransactionReceipt object will be returned indicating that the transaction was not successful and the reason for this
        if (index == -1) {
            tr = new TransactionReceipt(transactionTicket, false, "Invalid Account", 0, 0, null);
        } else if (listOfAccounts.get(index).getAccountStatus().equalsIgnoreCase("Opened")) {
            tr = new TransactionReceipt(transactionTicket, false, "Account Is Not Closed", acc.getBalance(tt).getPreTransactionBalance(), acc.getBalance(tt).getPreTransactionBalance(), null);
        } else {
            tr = new TransactionReceipt(transactionTicket, true, null, 0, 0, null);
            //if the account exists and is closed, the account is removed by calling removeAcct() method
            removeAcct(index);
        }
        return tr;
    }

    /*
     *  removeAcct() method accepts an index that indicates the index of the account that needs to be removed
     */
    public static void removeAcct(int index) {
        listOfAccounts.remove(index);
        acctNumQuickSortKey.remove(Integer.valueOf(index));
        acctNumBubbleSortKey.remove(Integer.valueOf(index));
        acctNumInsertionSortKey.remove(Integer.valueOf(index));
        ssnQuickSortKey.remove(Integer.valueOf(index));
        ssnBubbleSortKey.remove(Integer.valueOf(index));
        ssnInsertionSortKey.remove(Integer.valueOf(index));
        nameQuickSortKey.remove(Integer.valueOf(index));
        nameBubbleSortKey.remove(Integer.valueOf(index));
        nameInsertionSortKey.remove(Integer.valueOf(index));
        balanceQuickSortKey.remove(Integer.valueOf(index));
        balanceBubbleSortKey.remove(Integer.valueOf(index));
        balanceInsertionSortKey.remove(Integer.valueOf(index));
        //the values in the sorting keys are reset to match the new size of the list of accounts
        for(int i = 0; i < getNumAccts(); i++) {
            acctNumQuickSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            acctNumBubbleSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            acctNumInsertionSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            ssnQuickSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            ssnBubbleSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            ssnInsertionSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            nameQuickSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            nameBubbleSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            nameInsertionSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            balanceQuickSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            balanceBubbleSortKey.set(i, i);
        }
        for(int i = 0; i < getNumAccts(); i++) {
            balanceInsertionSortKey.set(i, i);
        }
        //accounts are re-sorted
        sortAccounts();
    }

    /*
     * findAcct() method accepts:
     *   - accountNumber: this is the number that will be searched for in the ArrayList of Accounts to find if it exists
     */
    public static int findAcct(int accountNumber) {
        //findAcct() method implements binary search
        int low = 0;
        int high = listOfAccounts.size() - 1;
        while(low <= high) {
            int test = (high + low) / 2;
            //compares the account number of the first index in our sorted keys to the requested account number
            if(listOfAccounts.get(getAcctNumQuickSortKey().get(test)).getAcctNumber() == accountNumber){
                return acctNumQuickSortKey.get(test);
            }
            else if(listOfAccounts.get(getAcctNumQuickSortKey().get(test)).getAcctNumber() > accountNumber) {
                // if the requested account number is smaller, the test value is too high so high is equal to test -1
                high = test - 1;
            }
            else {
                low = test + 1;
            }
        }
        // account not found
        return -1;
    }

    /*
     * findAcct() method accepts:
     *   - SSN (Social Security Number) : this is the SSN that will be searched for in the ArrayList of Accounts to find any accounts that match this SSN
     */
    public static ArrayList<Account> findAcct(String SSN) {
        //A new ArrayList of Accounts is initialized
        ArrayList<Account> accountsOfCustomer = new ArrayList<>();
        //sequential search: goes through every element in the listOfAccounts to found accounts with matching SSNs
        for(int i = 0; i < listOfAccounts.size(); i++) {
            // calls findAcct() method that accepts an int
            int index = findAcct(listOfAccounts.get(i).getAcctNumber());
            //if the SSNs are equal, the account is added to the ArrayList
            if(listOfAccounts.get(i).getDepositor().getSSN().equals(SSN) && index != -1)
            {
                accountsOfCustomer.add(listOfAccounts.get(index));
            }
        }
        return accountsOfCustomer;
    }

    /*
     * getAcct() method accepts:
     *   - index: references the index in the ArrayList of Accounts that we want to retrieve after calling the findAcct(int accountNumber) method
     */
    public static Account getAcct(int index) {
        if(index == -1) {
            return null;
        }
        Account acc = listOfAccounts.get(index);
        String acctType = acc.getAcctType();
        //Creates a copy of the account if the account type is listed
        switch(acctType) {
            case "Savings": {
                SavingsAccount savings = (SavingsAccount) listOfAccounts.get(index);
                acc = new SavingsAccount(savings);
                break;
            }
            case "Checking": {
                CheckingAccount checking = (CheckingAccount) listOfAccounts.get(index);
                acc = new CheckingAccount(checking);
                break;
            }
            case "CD": {
                CDAccount cd = (CDAccount) listOfAccounts.get(index);
                acc = new CDAccount(cd);
                break;
            }
        }
        //Copy of the account is returned
        return acc;
    }

    /*
     * Accessor Methods that to retrieve the ArrayList of Accounts or ArrayList of Integers representing sorting keys
     */

    public static ArrayList<Account> getListOfAccounts() {
        return listOfAccounts;
    }

    public static ArrayList<Integer> getAcctNumQuickSortKey() {
        return acctNumQuickSortKey;
    }

    public static ArrayList<Integer> getAcctNumBubbleSortKey() {
        return acctNumBubbleSortKey;
    }

    public static ArrayList<Integer> getAcctNumInsertionSortKey() {
        return acctNumInsertionSortKey;
    }

    public static ArrayList<Integer> getSsnQuickSortKey() {
        return ssnQuickSortKey;
    }

    public static ArrayList<Integer> getSsnBubbleSortKey() {
        return ssnBubbleSortKey;
    }

    public static ArrayList<Integer> getSsnInsertionSortKey() {
        return ssnInsertionSortKey;
    }

    public static ArrayList<Integer> getNameQuickSortKey() {
        return nameQuickSortKey;
    }

    public static ArrayList<Integer> getNameBubbleSortKey() {
        return nameBubbleSortKey;
    }

    public static ArrayList<Integer> getNameInsertionSortKey() {
        return nameInsertionSortKey;
    }

    public static ArrayList<Integer> getBalanceQuickSortKey() {
        return balanceQuickSortKey;
    }

    public static ArrayList<Integer> getBalanceBubbleSortKey() {
        return balanceBubbleSortKey;
    }

    public static ArrayList<Integer> getBalanceInsertionSortKey() {
        return balanceInsertionSortKey;
    }

    public static Account getAcctByAcctNumSortKey(int key) {
        return getAcct(key);
    }

    public static Account getAcctBySSNSortKey(int key) {
        return getAcct(key);
    }

    public static Account getAcctByName(int key) {
        return getAcct(key);
    }

    public static Account getAcctByBalanceSortKey(int key) {
        return getAcct(key);
    }

    public static int getNumAccts() {
        return listOfAccounts.size();
    }

    //contains and calls all 4 sorting methods
    public static void sortAccounts() {
        sortAccountsByAcctNum();
        sortAccountsBySSN();
        sortAccountsByName();
        sortAccountsByBalance();
    }

    //sorts Accounts by account numbers
    private static void sortAccountsByAcctNum() {
        ArrayList<Integer> accountNumbers = new ArrayList<>();
        accountNumbers.clear();
        for(int i = 0; i < listOfAccounts.size(); i++) {
            accountNumbers.add(getAcct(i).getAcctNumber());
        }
        //calls the 3 sorting methods with their respective sortingKey ArrayLists
        quickSortGeneric(acctNumQuickSortKey, accountNumbers, 0, acctNumQuickSortKey.size() - 1);
        bubbleSortGeneric(acctNumBubbleSortKey, accountNumbers);
        insertionSortGeneric(acctNumInsertionSortKey, accountNumbers);
    }

    //sorts Accounts by Depositor SSNs by passing in a new AccountComparatorByAcctSSN() object
    private static void sortAccountsBySSN() {
        ArrayList<String> SSNs = new ArrayList<>();
        SSNs.clear();
        for(int k = 0; k < listOfAccounts.size(); k++) {
            SSNs.add(getAcct(k).getDepositor().getSSN());
        }
        quickSortGeneric(ssnQuickSortKey, SSNs, 0, ssnQuickSortKey.size() - 1);
        bubbleSortGeneric(ssnBubbleSortKey, SSNs);
        insertionSortGeneric(ssnInsertionSortKey, SSNs);
    }

    //sorts Accounts by Depositor Names by passing in a new AccountComparatorByAcctName() object
    private static void sortAccountsByName() {
        ArrayList<String> names = new ArrayList<>();
        names.clear();
        for(int j = 0; j < listOfAccounts.size(); j++) {
            names.add(getAcct(j).getDepositor().getName().getLastName() + " " + getAcct(j).getDepositor().getName().getFirstName());
        }
        quickSortGeneric(nameQuickSortKey, names, 0, nameQuickSortKey.size() - 1);
        bubbleSortGeneric(nameBubbleSortKey, names);
        insertionSortGeneric(nameInsertionSortKey, names);
    }

    //sorts Accounts by account balances by passing in a new new AccountComparatorByAcctBalance() object
    private static void sortAccountsByBalance() {
        ArrayList<Double> balances = new ArrayList<>();
        for(int p = 0 ; p < listOfAccounts.size(); p++) {
            balances.add(getAcct(p).getBalance(new TransactionTicket(Calendar.getInstance(), 0, "", 0, 0)).getPostTransactionBalance());
        }
        quickSortGeneric(balanceQuickSortKey, balances, 0, balanceQuickSortKey.size() - 1);
        bubbleSortGeneric(balanceBubbleSortKey, balances);
        insertionSortGeneric(balanceInsertionSortKey, balances);
    }

    /*
     * quickSortGeneric() method accepts:
     *   - an ArrayList of Integers that represents the sorting keys
     *   - a Comparator object that will be used to compare the desired attribute (account number, depositor name, depositor SSN, or account balance)
     *   - an int which represents the starting index of the ArrayList, 0
     *   - an int which represents the ending index of the Arraylist, the size of the ArrayList - 1
     */
    public static <E extends Comparable<E>> void quickSortGeneric(ArrayList<Integer> sortingKey, ArrayList<E> values, int start, int end) {
        if (start < end) {
            // p is the pivot point
            int p = partition(sortingKey, values, start, end);
            quickSortGeneric(sortingKey, values, start, p-1);
            quickSortGeneric(sortingKey, values,p+1, end);
        }
    }
    /*
     * partition() method accepts:
     *   - an ArrayList of Integers that represents the sorting keys
     *   - an int which represents the starting index of the partition
     *   - an int which represents the ending index of the partition
     */
    private static <E extends Comparable<E>> int partition(ArrayList<Integer> keys, ArrayList<E> values, int start,  int end) {
        E pivotValue = values.get(keys.get(start));
        int pivotPosition = start;
        for(int i = start + 1; i <= end; i++) {
            if(values.get(keys.get(i)).compareTo(pivotValue) < 0) {
                // swaps the current item to the right of the pivot point
                swap(keys, pivotPosition + 1, i);
                // swaps the current item with the pivot point
                swap(keys, pivotPosition, pivotPosition + 1);
                //pivotPosition iterates if the accountNumber retrieved is smaller than the pivot number
                pivotPosition++;
            }
            else if(values.get(keys.get(start)).compareTo(values.get(keys.get(i))) == 0) {
                //if the values are the same, it checks the name and then the account number to sort
                AccountComparatorByAcctName acctName = new AccountComparatorByAcctName();
                int n = acctName.compare(listOfAccounts.get(keys.get(start)), listOfAccounts.get(keys.get(i)));
                if(n > 0) {
                    swap(keys, pivotPosition + 1, i);
                    swap(keys, pivotPosition, pivotPosition + 1);
                    pivotPosition++;
                }
            }
        }
        return pivotPosition;
    }

    /*
     * swap() method accepts:
     *   - an ArrayList of Integers that represents the sorting keys
     *   - an int which represents the first position
     *   - an int which represents the second position
     */
    private static void swap(ArrayList<Integer> keys, int pos1,  int pos2) {
        int temp = keys.get(pos1);
        keys.set(pos1, keys.get(pos2));
        keys.set(pos2, temp);
    }

    //bubbleSortGeneric accepts an ArrayList of Integers representing sortingKeys and an ArrayList of type E representing values of the chosen attribute
    private static <E extends Comparable<E>> void bubbleSortGeneric(ArrayList<Integer> sortingKeys, ArrayList<E> values) {
        boolean notDone;
        //nested loops
        do {
            notDone = false;
            for(int i = 0; i < sortingKeys.size()-1; i++) {
                //Accounts will be compared depending on the Comparator object passed in
                if(values.get(sortingKeys.get(i)).compareTo(values.get(sortingKeys.get(i+1))) > 0) {
                    //keys are swapped if the value at the current index is larger than the proceeding index
                    int temp = sortingKeys.get(i);
                    sortingKeys.set(i, sortingKeys.get(i+1));
                    sortingKeys.set(i+1, temp);
                    notDone = true;
                } else if(values.get(sortingKeys.get(i)).compareTo(values.get(sortingKeys.get(i+1))) == 0) {
                    //if the values are the same, it checks the name and then the account number to sort
                    AccountComparatorByAcctName acctName = new AccountComparatorByAcctName();;
                    int n = acctName.compare(listOfAccounts.get(sortingKeys.get(i)), listOfAccounts.get(sortingKeys.get(i+1)));
                    if(n > 0) {
                        int temp = sortingKeys.get(i);
                        sortingKeys.set(i, sortingKeys.get(i+1));
                        sortingKeys.set(i+1, temp);
                        notDone = true;
                    }
                }
            }
        } while(notDone);
    }

    //insertionSortGeneric accepts an ArrayList of Integers representing sortingKeys and an ArrayList of type E representing values of the chosen attribute
    private static <E extends Comparable<E>> void insertionSortGeneric(ArrayList<Integer> sortingKeys, ArrayList<E> values) {
        //for loop starts at index 1 instead of 0
        for (int pos = 1; pos < sortingKeys.size(); pos++) {
            int temp = sortingKeys.get(pos);
            int cand = pos;
            //compares the current index with the preceding index
            if (values.get(sortingKeys.get(cand - 1)).compareTo(values.get(temp)) > 0) {
                while (cand > 0 && values.get(sortingKeys.get(cand - 1)).compareTo(values.get(temp)) > 0) {
                    sortingKeys.set(cand, sortingKeys.get(cand - 1));
                    cand--;
                }
            } else if (values.get(sortingKeys.get(cand - 1)).compareTo(values.get(temp)) == 0) {
                //if the values are the same, it checks the name and then the account number to sort
                AccountComparatorByAcctName acctName = new AccountComparatorByAcctName();
                int n = acctName.compare(listOfAccounts.get(sortingKeys.get(cand-1)), listOfAccounts.get(temp));
                if (n > 0) {
                    sortingKeys.set(cand, sortingKeys.get(cand - 1));
                    cand--;
                }
            }
            sortingKeys.set(cand, temp);
        }
    }

    //totalAmounts() method finds the sum of the money in the Savings, Checking, and CD accounts
    public static void totalAmounts(){
        double savingsTotal = 0;
        double checkingTotal = 0;
        double CDTotal = 0;
        //money is added to each variable depending on the account type
        for (int i = 0; i < listOfAccounts.size(); i++) {
            if (listOfAccounts.get(i).getAcctType().equalsIgnoreCase("Savings")) {
                savingsTotal += listOfAccounts.get(i).getBalance(new TransactionTicket(Calendar.getInstance(), listOfAccounts.get(i).getAcctNumber(), "", 0, 0)).getPostTransactionBalance();
            } else if (listOfAccounts.get(i).getAcctType().equalsIgnoreCase("Checking")) {
                checkingTotal += listOfAccounts.get(i).getBalance(new TransactionTicket(Calendar.getInstance(), listOfAccounts.get(i).getAcctNumber(), "", 0, 0)).getPostTransactionBalance();
            } else if (listOfAccounts.get(i).getAcctType().equalsIgnoreCase("CD")) {
                CDTotal += listOfAccounts.get(i).getBalance(new TransactionTicket(Calendar.getInstance(), listOfAccounts.get(i).getAcctNumber(), "", 0, 0)).getPostTransactionBalance();
            }
        }
        //changes the actual class variable value
        totalAmountInSavingsAccts = savingsTotal;
        totalAmountInCheckingAccts = checkingTotal;
        totalAmountInCDAccts = CDTotal;
        //adds the total amount in all CD, Checking, and Savings accounts
        totalAmountInAllAccts = totalAmountInSavingsAccts + totalAmountInCheckingAccts + totalAmountInCDAccts;
    }

    //Accessor method to get the total amount of money in every Checking account
    public double getTotalAmountInCheckingAccts() {
        return totalAmountInCheckingAccts;
    }

    //Accessor method to get the total amount of money in every Saving account
    public double getTotalAmountInSavingsAccts() {
        return totalAmountInSavingsAccts;
    }

    //Accessor method to get the total amount of money in every CD account
    public double getTotalAmountInCDAccts() {
        return totalAmountInCDAccts;
    }

    //Accessor method to get the total amount of money in all accounts
    public double getTotalAmountInAllAccts() {
        return totalAmountInAllAccts;
    }
}