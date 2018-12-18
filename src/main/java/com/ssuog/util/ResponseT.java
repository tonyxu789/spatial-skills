package com.ssuog.util;

import com.ssuog.po.EServiceResponseCode;

public class ResponseT<T> extends Response{

	private static final long serialVersionUID = 3023156788893226516L;
	
	public ResponseT() {
		super();
    }

    public ResponseT(EServiceResponseCode code) {
        super(code);
    }
	
	protected T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
