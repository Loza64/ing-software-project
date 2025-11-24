package com.pnc.project.exception;

public class DuplicateFieldException extends RuntimeException {
    private final String field;
    private final String detailMessage;

    public DuplicateFieldException(String field, String detailMessage) {
        super(detailMessage);
        this.field = field;
        this.detailMessage = detailMessage;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return detailMessage;
    }
}
