package com.shiyi.exception;

public class IDCardAndNameServiceException extends RuntimeException {

	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	
	public IDCardAndNameServiceException() {
        super();
    }

    public IDCardAndNameServiceException(String message) {
        super(message);
    }

    public IDCardAndNameServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public IDCardAndNameServiceException(Throwable cause) {
        super(cause);
    }

    public IDCardAndNameServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
