package quadratic;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;

public interface QuadraticLibrary extends Library {
    static QuadraticLibrary load(String dllPath) {
        return Native.load(dllPath, QuadraticLibrary.class);
    }

    void setA(double a);
    void setB(double b);
    void setC(double c);

    int getSolution(DoubleByReference x1, DoubleByReference x2);
}
