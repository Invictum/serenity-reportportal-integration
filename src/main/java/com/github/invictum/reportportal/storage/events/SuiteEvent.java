package com.github.invictum.reportportal.storage.events;

import com.github.invictum.reportportal.ItemType;

public abstract class SuiteEvent extends DefaultEvent {

    @Override
    public boolean isCompatible(ItemType type) {
        return ItemType.SUITE == type;
    }
}
