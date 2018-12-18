package com.ssuog.po;

public enum EServiceResponseCode {
	C200("200","success"), //success
	C401("401","parameter error"), //parameter error
	C403("403","not loggin"),//not loggin
	C500("500","server error"), //server error
	;

	private final String name;
	private final String zhName;

	private EServiceResponseCode(String name, String zhName) {
		this.name = name;
		this.zhName = zhName;
	}

	@Override
	public String toString(){
		return name;
	}

	public String toZhName(){
		return zhName;
	}


}
