package com.ssuog.util;

import com.ssuog.po.EServiceResponseCode;

public class Response implements java.io.Serializable {
	
	private static final long serialVersionUID = 1641609867535785624L;

	public Response() {
		this(EServiceResponseCode.C200);
	}
	public Response(EServiceResponseCode code) {
		result = "200".equals(code.toString());
		setStatus(code.toString());
		setMessage(code.toZhName());
	}

	public void valueOf(EServiceResponseCode code) {
		result = "200".equals(code.toString());
		setStatus(code.toString());
		setMessage(code.toZhName());
	}

	protected long total;
	protected long count;

	/**
	 * state
	 */
	protected boolean result;

	/**
	 * message
	 */
	protected String message;

	/**
	 * Corresponding marking
	 */
	protected String status;

	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Judge success or not
	 * @return
	 */
	public boolean checkSuccess(){
		return "200".equals(status);
	}
}
