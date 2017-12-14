package com.github.invictum.reportportal.storage.events;

import com.epam.reportportal.message.ReportPortalMessage;
import com.epam.reportportal.message.TypeAwareByteSource;
import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.MessageLevel;
import com.github.invictum.reportportal.Utils;
import com.github.invictum.reportportal.storage.Message;
import com.github.invictum.reportportal.storage.TreeNode;
import rp.com.google.common.io.ByteSource;

/**
 * Sends a message with {@link Throwable} details to current active item.
 */
public class ReportErrorEvent extends DefaultEvent {

    private Throwable cause;

    public ReportErrorEvent(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public void proceed(TreeNode node) {
        TypeAwareByteSource source = new TypeAwareByteSource(ByteSource.empty(), "");
        ReportPortalMessage body = new ReportPortalMessage(source, Utils.verboseError(cause));
        Message message = new Message(body);
        message.setLevel(MessageLevel.ERROR);
        node.getMessages().add(message);
    }

    @Override
    public boolean isCompatible(ItemType type) {
        return true;
    }
}
