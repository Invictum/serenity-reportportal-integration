package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.storage.TreeNode;

import java.util.Calendar;

/**
 * Creates new test as a sub-item for specified node.
 */
public class StartTestEvent extends TestEvent {

    private String name;

    public StartTestEvent(String name) {
        this.name = name;
    }

    @Override
    public void proceed(TreeNode node) {
        TreeNode childNode = new TreeNode();
        childNode.setType(ItemType.TEST);
        childNode.setStartTime(Calendar.getInstance().getTime());
        childNode.setName(name);
        node.getChildren().push(childNode);
    }

    @Override
    public boolean isCompatible(ItemType type) {
        return type == ItemType.SUITE;
    }
}
