import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Introduction to user
        System.out.println("Welcome to Parking System.");
        System.out.println("Please enter your license plate:")
        System.out.println();
        
        //User input: License plate
        Scanner user = new Scanner(System.in);
        String input = user.nextLine();
        input = emptyCheck(input,user);

        // GateManager places it in CustomQueue
        /*  
            @Undo
            Logic Here
        */

        //SlotManager pops the best slot from CustomMinHeap
        /*  
            @Undo
            Logic Here
        */
    
    }


    // Empty check until input is not null
    private static String emptyCheck(String input, Scanner user){
        if(input == null){
            System.out.println("Empty license plate, please try again:");
            input = user.nextLine();
            emptyCheck(input, user);
        }
        return input;
    }

}
