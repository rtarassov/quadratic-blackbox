package quadratic;

public record SolutionResult(int code, double x1, double x2) {
    public static final int SOLUTION_OK = 0;
    public static final int ERROR_A_IS_ZERO = 1;
    public static final int ERROR_NO_REAL_ROOTS = 2;
}
