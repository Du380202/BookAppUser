package com.example.appbansach.model;

public class ApiResponseObject {
	private int status;
    private String message;
    private Order order;
    
	public ApiResponseObject(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public ApiResponseObject(int status, String message, Order order) {
		super();
		this.status = status;
		this.message = message;
		this.order = order;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "ApiResponseObject{" +
				"status=" + status +
				", message='" + message + '\'' +
				", order=" + order +
				'}';
	}
}
