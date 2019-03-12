package com.edot.models;

public class DuplicateValueFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = -53656301288L;
    private static final String ERROR_MSG = "Duplicate values Found: ";

    public DuplicateValueFoundException()
    {
        super(ERROR_MSG);
    }

    /**
     *
     * @param e - duplicateValue that is present in Collection
     */

    public DuplicateValueFoundException(String e)
    {
        super(ERROR_MSG + e);
    }

    /**
     *
     * @param data - duplicateValue that is present in Collection
     * @param cause - Exception caused
     */

    public DuplicateValueFoundException(String data, Throwable cause) {
        super(ERROR_MSG + data, cause);
    }

    public DuplicateValueFoundException(Throwable cause) {
        super(ERROR_MSG,cause);
    }

}
