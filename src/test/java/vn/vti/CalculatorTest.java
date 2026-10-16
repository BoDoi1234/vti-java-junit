package vn.vti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;

//@ParameterizedClass
//@CsvSource({"apple, 1$", "orange, 2$"})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class CalculatorTest {
    static final Logger logger = LogManager.getLogger(CalculatorTest.class);
    static Calculator calculator;
//    String fruit, price;
//
//    CalculatorTest(String fruit, String price) {
//        this.fruit = fruit;
//        this.price = price;
//    }

    @BeforeAll
    // Run before any test case (Run before 1st test case)
    // Run only once in class
    static void prepareTestEnvironment() {
        calculator = new Calculator();
        logger.debug("Initialize Calculator Object {}", calculator);
    }

    @BeforeAll
    static void prepareTestData() {
        System.setProperty("time", "night"); // set system property
        logger.info("2nd precondition");
    }

    @AfterAll
    // Run after last test case
    // Run only once
    static void afterAll() {
        calculator = null;
        logger.debug("Remove Calculator Object {}", calculator);
    }

    @AfterAll
    static void afterAll_1() {
        logger.info("2nd post condition");
    }

    @BeforeEach
    // Run before every test case
    void beforeEach(TestInfo testInfo) {
        logger.info("----------START TESTING CASE {}----------", testInfo.getTestMethod());
    }

    @AfterEach
    // Run after every test case
    void afterEach(TestInfo testInfo) {
        logger.info("----------FINISH TESTING CASE {}----------", testInfo.getTestMethod());
    }

    @Test
    @RepeatedTest(2)
    void testAdd() {
        // Assert result == 2
        Assertions.assertEquals(3, calculator.add(0, 2));
    }

    @Test
    void testMinus() {
        // Assert result == -2
        Assertions.assertEquals(-2, calculator.minus(0, 2));
    }

    // Conditional Execution Test
    @Test
    @EnabledIfSystemProperty(named = "time", matches = "night")
    @Order(2)
    void testConditionExecution() {
        Assertions.assertTrue(true);
        System.out.println("Test is executed");
    }

    // Tag
    // Disabled Test
    // Order
    @Test
    @Tag("smoke")
    @Disabled
    @Order(1)
    void testTag() {
        Assertions.assertTrue(true);
        System.out.println("Smoke test is executed");
    }

    // Soft Test
    @ParameterizedTest(name = "{index}-{displayName}")
    @DisplayName("Testing Soft Assertion Using Junit")
    @ValueSource(ints = {-1, -2, -3, 0, 1, 2, 3})
    @Order(3)
    void testSoftAssert(int arg, TestInfo testInfo) {
        logger.info("Start TC {}", testInfo.getDisplayName());
        Assertions.assertAll(
                () -> {Assertions.assertTrue(arg < 0, "This number " + arg + " is not negative");},
                () -> {Assertions.assertEquals(0, arg, "This number " + arg + " is not zero");},
                () -> {Assertions.assertTrue(arg > 0, "This number " + arg + " is not positive");}
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, 0, 6, 8})
    void testAddFunctionWithZero(int arg) {
        int result = calculator.add(0, arg);
//        Assertions.assertEquals(arg, result);
        Assumptions.assumingThat(arg > 0, () -> {
            Assertions.assertTrue(arg > 0);
        });
        Assumptions.assumingThat(arg <= 0, () -> {
            Assertions.assertTrue(arg <= 0);
        });
        logger.info("Add function works correctly when adding 0 with {}. Result is: {}", arg, result);
    }

    @ParameterizedTest
    @NullSource // Add extra null data
    @EmptySource // Add extra empty data
    @ValueSource(strings = {"-2", "0", "6", "8", "10"})
    void testAddFunctionWithZeroUsingString(String arg) {
        int result = calculator.add(0, Integer.parseInt(arg));
        Assertions.assertEquals(Integer.parseInt(arg), result);
        logger.info("Add function works correctly when adding 0 with {}. Result is: {}", arg, result);
    }

    @ParameterizedTest
    @ValueSource(classes = {Calculator.class})
    void testAddFunctionWithZeroWithClass(Class<?> arg) {
    }

    @ParameterizedTest
    @EnumSource(names = {"ADD", "MULTIPLY", "MINUS", "DIVIDE"})
    public void testEnumSource(MathOperator arg) {
        Assertions.assertTrue(arg == MathOperator.ADD || arg == MathOperator.MINUS);
    }

    @ParameterizedTest
    // Best used for key-value pairs
    @CsvSource(value = {
            "fruit, discount price, fixed price",
            "apple, 1$, 3$",
            "orange, 2$, 5$",
            "cucumber, 1$, 6$"
    }, useHeadersInDisplayName = true)
    void testCsvSource(String fruit, String discountPrice, String fixedPrice) {
        Assertions.assertFalse(fruit.isEmpty());

        // Validate sell price is not negative (fixed - discount)
        int fixed = Integer.parseInt(fixedPrice.replaceAll("\\$", ""));
        int discount = Integer.parseInt(discountPrice.replaceAll("\\$", ""));

        Assertions.assertTrue(calculator.minus(fixed, discount) >= 0);
    }

    @ParameterizedTest
    // Best used for key-value pairs
    @CsvSource(value = {
            "fruit name    | discount price   | fixed price",
            "apple         | 1                | 3",
            "orange        | 2                | 5",
            "cucumber      | 1                | 6"
    }, useHeadersInDisplayName = true, ignoreLeadingAndTrailingWhitespace = true, delimiter = '|')
    void testCsvSource_1(String fruit, int discountPrice, int fixedPrice) {
        Assertions.assertFalse(fruit.isEmpty());

        // Validate sell price is not negative (fixed - discount)

        Assertions.assertTrue(calculator.minus(fixedPrice, discountPrice) >= 0);
    }

    @ParameterizedTest
    // Best used for key-value pairs
    @CsvFileSource(files = {"src/test/resources/fruit price.csv"}, useHeadersInDisplayName = true, delimiter = '|')
    void testCsvFileSource(String fruit, String discountPrice, String fixedPrice) {
        Assertions.assertFalse(fruit.isEmpty());

        // Validate sell price is not negative (fixed - discount)
        int fixed = Integer.parseInt(fixedPrice.replaceAll("\\$", ""));
        logger.info("Fixed Price: {}", fixed);
        int discount = Integer.parseInt(discountPrice.replaceAll("\\$", ""));
        logger.info("Discount Price: {}", discount);

        Assertions.assertTrue(calculator.minus(fixed, discount) >= 0);
    }

    @ParameterizedTest
    // Call external method source: package.className#methodName
    @MethodSource(value = "vn.vti.DataProviderMethod#students")
    void testWithMethodSource(String student) {
        Assertions.assertFalse(student.isEmpty());
    }

    @ParameterizedTest
    // Call external method source: package.className#methodName
    @MethodSource(value = "students")
    void testWithMethodSourceInternal(String student) {
        Assertions.assertFalse(student.isEmpty());
    }

    static List<String> students() {
        return List.of("Long", "Kim", "Bang");
    }

    @ParameterizedTest
    @ArgumentsSource(value = MyDataProvider.class)
    void testWithArgumentSource(int number) {
//        Assertions.assertFalse(fruit.isEmpty());

        Assertions.assertTrue(number > 0);
    }

    @ParameterizedTest
    @ArgumentsSource(value = MyDataProvider.class)
    @MethodSource("students")
    void testWithMultipleSource(String value) {
        Assertions.assertFalse(value.isEmpty());
    }
}
