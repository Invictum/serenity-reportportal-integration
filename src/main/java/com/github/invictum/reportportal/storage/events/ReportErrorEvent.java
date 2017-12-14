package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.MessageLevel;
import com.github.invictum.reportportal.Utils;
import com.github.invictum.reportportal.storage.Message;
import com.github.invictum.reportportal.storage.TreeNode;

/**
 * Sends a message with {@link Throwable} details to current active item.
 */
public class ReportErrorEvent extends DefaultEvent {

    private Throwable cause;
    private String title;

    public ReportErrorEvent(Throwable cause) {
        this.cause = cause;
    }

    public ReportErrorEvent(String title, Throwable cause) {
        this.cause = cause;
        this.title = title;
    }

    @Override
    public void proceed(TreeNode node) {
        String data = title == null ? Utils.verboseError(cause) : title + "\n" + Utils.verboseError(cause);
        Message message = new Message(data);
        message.setLevel(MessageLevel.ERROR);
        node.getMessages().add(message);
    }

    @Override
    public boolean isCompatible(ItemType type) {
        return true;
    }
}
