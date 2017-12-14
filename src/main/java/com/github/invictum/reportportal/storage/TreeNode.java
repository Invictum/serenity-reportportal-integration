package com.github.invictum.reportportal.storage;

import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.Status;

import java.util.*;

/**
 * Simple building block used to create items tree structure. Used by {@link Storage} class.
 */
public class TreeNode {

    private String name;
    private Date startTime;
    private Date endTime;
    private Status status = Status.PASSED;
    private ItemType type;
    private Deque<TreeNode> children = new ArrayDeque<>();
    private List<Message> messages = new ArrayList<>();
    private boolean open = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Deque<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Deque<TreeNode> children) {
        this.children = children;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
