package com.stocks.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class BadRequestException extends AbstractThrowableProblem {


    public BadRequestException(String cause) {
        super(null, cause, Status.BAD_REQUEST);
    }
}
