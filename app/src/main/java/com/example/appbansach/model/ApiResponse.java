package com.example.appbansach.model;

public class ApiResponse {
	private int status;
    private String message;
//	private Object object;
 
	public ApiResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

//	public ApiResponse(int status, String message, Object object) {
//		this.status = status;
//		this.message = message;
//		this.object = object;
//	}

//	public Object getObject() {
//		return object;
//	}
//
//	public void setObject(Object object) {
//		this.object = object;
//	}

	public int isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
