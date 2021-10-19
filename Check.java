import java.util.Calendar;

public class Check {
    private int accountNum;
    private double checkAmount;
    private Calendar dateOfCheck;

    public Check(int accountNum, double checkAmount, Calendar dateOfCheck) {
        this.accountNum = accountNum;
        this.checkAmount = checkAmount;
        this.dateOfCheck = dateOfCheck;
    }

    public Check(Check c) {
        this(c.getAccountNum(), c.getCheckAmount(), c.getDateOfCheck());
    }

    //getAccountNum() is an accessor method that returns accountNum
    public int getAccountNum() {
        return accountNum;
    }

    //getCheckAmount() is an accessor method that returns checkAmount
    public double getCheckAmount() {
        return checkAmount;
    }

    //getCheckAmount() is an accessor method that returns Calendar object, dateOfCheck
    public Calendar getDateOfCheck() {
        return dateOfCheck;
    }

    //toString() method is overridden and returns a String stating the account number, check amount, and date of check
    public String toString() {
        return ((dateOfCheck.get(Calendar.MONTH) + 1) + "/" + dateOfCheck.get(Calendar.DAY_OF_MONTH) + "/" + getDateOfCheck().get(Calendar.YEAR));
    }

}
