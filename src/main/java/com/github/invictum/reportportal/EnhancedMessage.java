package com.github.invictum.reportportal;

import com.epam.reportportal.message.ReportPortalMessage;
import com.epam.reportportal.message.TypeAwareByteSource;
import rp.com.google.common.io.ByteSource;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Improved version of {@link ReportPortalMessage} that extended with details related to log level and date.
 * Isolates all the date required to emit log message to Report Portal server.
 */
public class EnhancedMessage extends ReportPortalMessage {

    private LogLevel level = LogLevel.INFO;
    private Date date = Calendar.getInstance().getTime();

    public EnhancedMessage(String message) {
        super(message);
    }

    public EnhancedMessage(ByteSource data, String mediaType, String message) {
        super(data, mediaType, message);
    }

    public EnhancedMessage(TypeAwareByteSource data, String message) {
        super(data, message);
    }

    public EnhancedMessage(File file, String message) throws IOException {
        super(file, message);
    }

    public EnhancedMessage withLevel(LogLevel level) {
        this.level = level;
        return this;
    }

    public EnhancedMessage withDate(Date date) {
        this.date = date;
        return this;
    }

    public LogLevel getLevel() {
        return level;
    }

    public Date getDate() {
        return date;
    }
}
