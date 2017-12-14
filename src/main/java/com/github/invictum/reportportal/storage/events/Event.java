package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.storage.TreeNode;

/**
 * Defines event may be pushed to storage.
 * Event provides some atomic storage state altering.
 */
public interface Event {
    /**
     * Checks event and type compatibility.
     *
     * @param type to check for compatibility.
     * @return {@code true} only in case if event is compatible with provided item type.
     */
    boolean isCompatible(ItemType type);

    /**
     * Modify node in accordance with internal logic.
     *
     * @param node to alter.
     */
    void proceed(TreeNode node);

    /**
     * Defines search mode for node looking for current event.
     *
     * @return related {@link SearchMode}
     */
    SearchMode searchMode();
}
