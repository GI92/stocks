package com.stocks.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UnauthorizedAccessException extends AbstractThrowableProblem {

    public UnauthorizedAccessException() {
        super(null, "Access unauthorized!", Status.UNAUTHORIZED);
    }
}
