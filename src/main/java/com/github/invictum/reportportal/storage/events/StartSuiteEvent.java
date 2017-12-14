package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.storage.TreeNode;

import java.util.Calendar;

/**
 * Updates current root suite with details.
 */
public class StartSuiteEvent extends SuiteEvent {

    private String name;

    public StartSuiteEvent(String name) {
        this.name = name;
    }

    @Override
    public void proceed(TreeNode node) {
        node.setType(ItemType.SUITE);
        node.setStartTime(Calendar.getInstance().getTime());
        node.setName(name);
    }
}
