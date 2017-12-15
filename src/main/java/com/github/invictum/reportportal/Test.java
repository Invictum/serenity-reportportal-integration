package com.github.invictum.reportportal;

import io.reactivex.Maybe;

public class Test {

    private Maybe<String> id;
    private Status status = Status.PASSED;

    public Maybe<String> getId() {
        return id;
    }

    public void setId(Maybe<String> id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
