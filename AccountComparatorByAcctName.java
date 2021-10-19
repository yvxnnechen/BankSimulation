import java.util.*;
public class AccountComparatorByAcctName implements Comparator<Account>{
    //overridden compare() method accepts two Account objects to compare the Names of each Depositor
    public int compare(Account acct1, Account acct2) {
        //Initializing two Name references
        Name n1 = acct1.getDepositor().getName();
        Name n2 = acct2.getDepositor().getName();
        int n = n1.compareTo(n2);
        //if the first and last name are the same, the account numbers are compared instead
        if(n == 0) {
            Integer accNum1 = acct1.getAcctNumber();
            Integer accNum2 = acct2.getAcctNumber();
            n = accNum1.compareTo(accNum2);
        }
        // returns a positive or negative number
        return n;
    }

}
