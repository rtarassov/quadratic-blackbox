package quadratic;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String dllPath = "C:\\Coding\\Kool\\quadratic-blackbox\\src\\main\\resources\\quadratic.dll";

        QuadraticService service = new QuadraticService(dllPath);
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter A: ");
        double a = sc.nextDouble();

        System.out.print("Enter B: ");
        double b = sc.nextDouble();

        System.out.print("Enter C: ");
        double c = sc.nextDouble();

        SolutionResult result = service.solve(a, b, c);

        System.out.println("Return code = " + result.code());
        System.out.println("x1 = " + result.x1());
        System.out.println("x2 = " + result.x2());
    }
}
