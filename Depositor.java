public class Depositor {
    private Name name;
    private String SSN;

    public Depositor(Name name, String SSN) {
        this.name = name;
        this.SSN = SSN;
    }

    public Depositor(Depositor d) {
        this(d.getName(), d.getSSN());
    }

    //getName() method is an accessor method that returns a copy of the reference to the Name object
    public Name getName() {
        Name n = new Name(name);
        return n;
    }

    //getSSN() method is an accessor method that returns SSN
    public String getSSN() {
        return SSN;
    }

    //toString() method is overridden and returns a String stating the name and social security number of the depositor
    public String toString() {
        return "Name: " + name + "\nSocial Security Number: " + SSN;
    }

    /*
     * equals() method accepts:
     *   - o: reference to Object class object
     * process:
     *  - checks if o is of Account type
     *  - if not, the object calling the equals() method and o are compared to each other looking at the fields:
     *     + name
     *     + SSN
     *  - returns true if all of the fields are equal and false otherwise
     */
    public boolean equals(Object o) {
        if(!(o instanceof Depositor)) {
            return false;
        }
        Depositor d = (Depositor) o;
        return this.getName().equals(d.getName()) && this.getSSN().equals(d.getSSN());
    }
}
