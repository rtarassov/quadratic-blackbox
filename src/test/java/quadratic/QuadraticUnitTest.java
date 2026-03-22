package quadratic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuadraticUnitTest {
    private static final String DLL_PATH = "src/main/resources/quadratic.dll";
    private static final double EPSILON = 1e-9;
    private static final double CASE12_EPSILON = 1e-6;

    private QuadraticService service;

    @BeforeEach
    void setUp() {
        service = new QuadraticService(DLL_PATH);
    }

    @Test
    void case1_shouldReturnTwoRealRoots() {
        SolutionResult result = service.solve(1, 0, -4);

        assertAll(
                // Works most of the time, fails randomly with wrong decimal point in roots (-2.000002 instead of -2.000000)
                // Sometimes returns code 1 instead of code 0.
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, -2, 2)
        );
    }

    @Test
    void case2_shouldReturnDoubleRootOne() {
        SolutionResult result = service.solve(1, -2, 1);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, 1, 1)
        );
    }

    @Test
    void case3_shouldFailWhenAIsZero() {
        SolutionResult result = service.solve(0, 2, 1);

        assertEquals(SolutionResult.ERROR_A_IS_ZERO, result.code());
    }

    @Test
    void case4_shouldFailWhenNoRealRoots() {
        SolutionResult result = service.solve(1, 0, 1);

        assertEquals(SolutionResult.ERROR_NO_REAL_ROOTS, result.code());
    }

    @Test
    void case5_shouldReturnNegativeRoots() {
        SolutionResult result = service.solve(1, 5, 6);

        assertAll(
                // FAIL, returns code 2 instead of code 0. Correct roots. Same problem manually.
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, -3, -2)
        );
    }

    @Test
    void case6_shouldReturnPositiveRoots() {
        SolutionResult result = service.solve(1, -5, 6);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, 2, 3)
        );
    }

    @Test
    void case7_shouldReturnZeroAndNegativeTwo() {
        SolutionResult result = service.solve(1, 2, 0);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, 0, -2)
        );
    }

    @Test
    void case8_shouldReturnDoubleRootZero() {
        SolutionResult result = service.solve(1, 0, 0);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, 0, 0)
        );
    }

    @Test
    void case9_shouldReturnTwoAndThreeForNegativeA() {
        SolutionResult result = service.solve(-1, 5, -6);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                // FAIL, returns roots 3 and 3 instead of 2 and 3. Correct code. Same problem manually.
                () -> assertRootsUnordered(result, 2, 3)
        );
    }

    @Test
    void case10_shouldReturnOneAndThree() {
        SolutionResult result = service.solve(2, -8, 6);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, 1, 3)
        );
    }

    @Test
    void case11_shouldReturnMinusOneAndHalf() {
        SolutionResult result = service.solve(2, 1, -1);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, -1, 0.5)
        );
    }

    @Test
    void case12_shouldReturnCloseRootsWithLooserTolerance() {
        SolutionResult result = service.solve(1, 2.000001, 1);

        assertAll(
                // FAIL, returns correct roots but code 2 instead of code 0. Same problem manually.
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, -1.0010005001251, -0.99900049987492, CASE12_EPSILON)
        );
    }

    @Test
    void case13_shouldFailWhenDiscriminantIsSlightlyNegative() {
        SolutionResult result = service.solve(1, 1.999999, 1);

        assertAll(
                () -> assertEquals(SolutionResult.ERROR_NO_REAL_ROOTS, result.code())
        );
    }

    @Test
    void case14_shouldReturnTwoAndThreeForScaledCoefficients() {
        SolutionResult result = service.solve(10, -50, 60);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                () -> assertRootsUnordered(result, 2, 3)
        );
    }

    @Test
    void case15_shouldReturnTwoAndThreeForLargeCoefficients() {
        SolutionResult result = service.solve(100000000, -500000000, 600000000);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code()),
                // FAIL, returns 3.000300 and 2.000000 instead of 3.000000 and 2.000000. Same problem manually. Correct code.
                () -> assertRootsUnordered(result, 2, 3)
        );
    }

    @Test
    void case16_shouldNotLeakStateBetweenSequentialCalls() {
        SolutionResult firstResult = service.solve(1, -2, 1);
        SolutionResult secondResult = service.solve(1, 0, -4);

        assertAll(
                () -> assertEquals(SolutionResult.SOLUTION_OK, firstResult.code()),
                () -> assertRootsUnordered(firstResult, 1, 1),
                // FAIL, state leaks from first call. Returns code 2 instead of code 0. Works correctly manually
                () -> assertEquals(SolutionResult.SOLUTION_OK, secondResult.code()),
                // FAIL, state leaks from first call. Returns -2 and -2 instead of -2 and 2. Works correctly manually
                () -> assertRootsUnordered(secondResult, -2, 2)
        );
    }

    private void assertRootsUnordered(SolutionResult result, double expectedRoot1, double expectedRoot2) {
        assertRootsUnordered(result, expectedRoot1, expectedRoot2, EPSILON);
    }

    private void assertRootsUnordered(SolutionResult result, double expectedRoot1, double expectedRoot2, double epsilon) {
        boolean sameOrder = almostEqual(result.x1(), expectedRoot1, epsilon)
                && almostEqual(result.x2(), expectedRoot2, epsilon);
        boolean swappedOrder = almostEqual(result.x1(), expectedRoot2, epsilon)
                && almostEqual(result.x2(), expectedRoot1, epsilon);

        assertTrue(sameOrder || swappedOrder,
                String.format("Expected roots [%f, %f] in any order but got [%f, %f]",
                        expectedRoot1, expectedRoot2, result.x1(), result.x2()));
    }

    private boolean almostEqual(double actual, double expected, double epsilon) {
        return Math.abs(actual - expected) <= epsilon;
    }
}
