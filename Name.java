public class Name {
    private String firstName;
    private String lastName;

    public Name() {
        this.firstName = "";
        this.lastName = "";
    }

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //copy constructor
    public Name(Name name) {
        this(name.getFirstName(), name.getLastName());
    }

    //getFirstName() method is an accessor method that returns firstName
    public String getFirstName() {
        return firstName;
    }

    //getLastName() is an accessor method that returns lastName
    public String getLastName() {
        return lastName;
    }

    //toString() method is overridden and returns a String stating the first and last name
    public String toString() {
        return firstName + " " + lastName;
    }

    //compareTo() method accepts a Name object
    public int compareTo(Name n) {
        //Declared variables comparing the first and last names
        int compareLast = this.getLastName().compareTo(n.getLastName());
        int compareFirst = this.getFirstName().compareTo(n.getFirstName());
        //returns 0 if the names are equal
        if(compareLast == 0 && compareFirst == 0) {
            return 0;
        }
        if(compareLast == 0){
            return compareFirst;
        } else {
            return compareLast;
        }
    }
    /*
     * equals() method accepts:
     *   - o: reference to Object class object
     * process:
     *  - checks if o is of Account type
     *  - if not, the object calling the equals() method and o are compared to each other looking at the fields:
     *     + firstName
     *     + lastName
     *  - returns true if all of the fields are equal and false otherwise
     */
    public boolean equals(Object o) {
        if(!(o instanceof Name)) {
            return false;
        }
        Name n = (Name) o;
        return this.firstName.equals(n.firstName) && this.lastName.equals(n.lastName);
    }
}
