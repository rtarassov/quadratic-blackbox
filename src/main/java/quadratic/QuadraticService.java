package quadratic;

import com.sun.jna.ptr.DoubleByReference;

public class QuadraticService {
    private final QuadraticLibrary lib;

    public QuadraticService(String dllPath) {
        this.lib = QuadraticLibrary.load(dllPath);
    }

    public SolutionResult solve(double a, double b, double c) {
        lib.setA(a);
        lib.setB(b);
        lib.setC(c);

        DoubleByReference x1 = new DoubleByReference();
        DoubleByReference x2 = new DoubleByReference();

        int code = lib.getSolution(x1, x2);
        return new SolutionResult(code, x1.getValue(), x2.getValue());
    }
}
