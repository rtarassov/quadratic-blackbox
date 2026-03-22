package quadratic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuadraticUnitTest {
    private static final String DLL_PATH = "src/main/resources/quadratic.dll";
    private static final double EPSILON = 1e-9;

    private QuadraticService service;

    @BeforeEach
    void setUp() {
        service = new QuadraticService(DLL_PATH);
    }

    @Test
    void case1_shouldReturnTwoRealRoots() {
        SolutionResult result = service.solve(1, 0, -4);

        assertAll(
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
                () -> assertRootsUnordered(result, -33, -2),
                () -> assertEquals(SolutionResult.SOLUTION_OK, result.code())
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

    private void assertRootsUnordered(SolutionResult result, double expectedRoot1, double expectedRoot2) {
        boolean sameOrder = almostEqual(result.x1(), expectedRoot1) && almostEqual(result.x2(), expectedRoot2);
        boolean swappedOrder = almostEqual(result.x1(), expectedRoot2) && almostEqual(result.x2(), expectedRoot1);

        assertTrue(sameOrder || swappedOrder,
                String.format("Expected roots [%f, %f] in any order but got [%f, %f]",
                        expectedRoot1, expectedRoot2, result.x1(), result.x2()));
    }

    private boolean almostEqual(double actual, double expected) {
        return Math.abs(actual - expected) <= EPSILON;
    }
}
