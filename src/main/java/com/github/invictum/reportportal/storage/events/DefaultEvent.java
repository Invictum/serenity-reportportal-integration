package com.github.invictum.reportportal.storage.events;

public abstract class DefaultEvent implements Event {

    @Override
    public SearchMode searchMode() {
        return SearchMode.CHILDREN_FIRST;
    }
}
