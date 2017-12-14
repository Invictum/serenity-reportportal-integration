package com.github.invictum.reportportal.storage.events;

import com.epam.reportportal.message.ReportPortalMessage;
import com.epam.reportportal.message.TypeAwareByteSource;
import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.MessageLevel;
import com.github.invictum.reportportal.storage.Message;
import com.github.invictum.reportportal.storage.TreeNode;
import rp.com.google.common.io.ByteSource;

/**
 * Sends message to current active item.
 */
public class MessageEvent extends DefaultEvent {

    private String message;
    private MessageLevel level = MessageLevel.INFO;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, MessageLevel level) {
        this.message = message;
        this.level = level;
    }

    @Override
    public void proceed(TreeNode node) {
        TypeAwareByteSource source = new TypeAwareByteSource(ByteSource.empty(), "");
        ReportPortalMessage body = new ReportPortalMessage(source, this.message);
        Message message = new Message(body);
        message.setLevel(level);
        node.getMessages().add(message);
    }

    @Override
    public boolean isCompatible(ItemType type) {
        return true;
    }
}
