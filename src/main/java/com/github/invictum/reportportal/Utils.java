package com.github.invictum.reportportal;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

    /**
     * Builds a stack trace for Throwable
     *
     * @param cause should be verbosed
     * @return stack trace represented as String
     */
    public static String verboseError(Throwable cause) {
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
