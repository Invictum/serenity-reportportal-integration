package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.storage.TreeNode;

import java.util.Calendar;

public class StopSuiteEvent extends SuiteEvent {

    @Override
    public void proceed(TreeNode node) {
        node.setEndTime(Calendar.getInstance().getTime());
        node.setOpen(false);
    }
}
