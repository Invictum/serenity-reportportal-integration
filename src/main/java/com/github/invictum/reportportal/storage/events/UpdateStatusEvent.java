package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.Status;
import com.github.invictum.reportportal.storage.TreeNode;

/**
 * Updates status of current active item or nearest parent of specified type.
 * Caution: result is undefined if used in nested steps with Step type.
 */
public class UpdateStatusEvent implements Event {

    private Status status;
    private ItemType type;
    private SearchMode mode;

    public UpdateStatusEvent(ItemType type, Status status) {
        this.status = status;
        this.type = type;
        this.mode = SearchMode.PARENT_FIRST;
    }

    public UpdateStatusEvent(Status status) {
        this.status = status;
        this.mode = SearchMode.CHILDREN_FIRST;
    }

    @Override
    public void proceed(TreeNode node) {
        node.setStatus(status);
    }

    @Override
    public boolean isCompatible(ItemType type) {
        return this.type == null || this.type == type;
    }

    @Override
    public SearchMode searchMode() {
        return mode;
    }
}
