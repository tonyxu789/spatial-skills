package com.ssuog.po;

/**
 * User's identity
 */
public enum UserRole {
	STUDENT("STUDENT","student"), 
	ADMIN("ADMIN","admin"), 
	;

	private final String name;
	private final String zhName;

	private UserRole(String name, String zhName) {
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
