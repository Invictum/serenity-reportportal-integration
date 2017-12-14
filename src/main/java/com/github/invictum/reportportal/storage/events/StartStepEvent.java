package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.storage.TreeNode;

import java.util.Calendar;

/**
 * Creates new sub step for specified item.
 */
public class StartStepEvent extends StepEvent {

    private String name;

    public StartStepEvent(String name) {
        this.name = name;
    }

    @Override
    public void proceed(TreeNode node) {
        TreeNode childNode = new TreeNode();
        childNode.setType(ItemType.STEP);
        childNode.setStartTime(Calendar.getInstance().getTime());
        childNode.setName(name);
        node.getChildren().push(childNode);
    }

    @Override
    public boolean isCompatible(ItemType type) {
        return type == ItemType.STEP || type == ItemType.TEST;
    }
}
