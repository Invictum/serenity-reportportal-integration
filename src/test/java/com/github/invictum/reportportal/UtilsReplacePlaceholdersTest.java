package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class UtilsReplacePlaceholdersTest {

    private final String input;
    private final List<String> replacements;
    private final String expectedResult;

    public UtilsReplacePlaceholdersTest(String input, List<String> replacements, String expectedResult) {
        this.input = input;
        this.replacements = replacements;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"Add two numbers <num1> & <num2>", List.of("10", "20"), "Add two numbers 10 & 20"},
                {"This is <placeholder1> and <placeholder2>.", List.of("first", "second", "third"),
                        "This is first and second."},
                {"Hello <name>, welcome to <place>!", List.of("Alice", "Wonderland"),
                        "Hello Alice, welcome to Wonderland!"},
                {"No placeholders here", List.of(), "No placeholders here"},
                {"No placeholders here", List.of("first"), "No placeholders here"},
                {"<single>", List.of("only"), "only"}
        });
    }

    @Test
    public void testReplacePlaceholders() {
        String actualResult = Utils.replacePlaceholders(input, replacements);
        Assert.assertEquals(expectedResult, actualResult);
    }
}
