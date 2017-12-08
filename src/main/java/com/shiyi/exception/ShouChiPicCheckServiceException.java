package com.shiyi.exception;

public class ShouChiPicCheckServiceException extends RuntimeException {

	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	
	public ShouChiPicCheckServiceException() {
        super();
    }

    public ShouChiPicCheckServiceException(String message) {
        super(message);
    }

    public ShouChiPicCheckServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShouChiPicCheckServiceException(Throwable cause) {
        super(cause);
    }

    public ShouChiPicCheckServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
