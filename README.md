# Quadratic DLL Testing Project

This project tests the unstable native library `quadratic.dll`, 
which solves quadratic equations in the form `a*x^2 + b*x + c = 0`.

## DLL Interface

- `setA(double a)` - sets coefficient `a`
- `setB(double b)` - sets coefficient `b`
- `setC(double c)` - sets coefficient `c`
- `getSolution(double* px1, double* px2)` - computes roots and returns a status code

Return codes:
- `SOLUTION_OK = 0`
- `ERROR_A_IS_ZERO = 1`
- `ERROR_NO_REAL_ROOTS = 2`

Example: `a=1, b=0, c=-4` should return `SOLUTION_OK` with roots `-2` and `2`.

## Testing Scope

The DLL is known to be unstable and may return incorrect results, so both manual and automated checks are included:
- Console testing via `src/main/java/quadratic/Main.java`
- Unit tests in `src/test/java/quadratic/QuadraticUnitTest.java`
