import java.util.Scanner;

/**
 * @author gagandeep.nagpal
 **/
public class Factorial {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n  = scanner.nextInt();
        int N = n;
        int fact = 1;
        while(n-- > 0) {
            fact*=(n + 1);
        }
        System.out.println(N + "! = " + fact);
    }
}
