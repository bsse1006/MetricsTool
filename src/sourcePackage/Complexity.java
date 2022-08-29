package sourcePackage;

public class Complexity {

    public static void main(String[] args) {
        int a = 2;
        switch(a)
        {
            case 1 :
                System.out.println("a is 1");
                break;

            case 2 :
                System.out.println("a is 2");
                break; // break is optional

            // We can have any number of case statements
            // below is default statement, used when none of the cases is true.
            // No break is needed in the default case.
            default :
                System.out.println("a is default");
                // Statements
        }
    }
}
