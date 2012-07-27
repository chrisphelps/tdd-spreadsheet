package com.sutemi;

import java.util.HashMap;

public class Sheet {

	String cell;
	String value;
	HashMap<String,String> cells = new HashMap<String, String>();
	
	public String get(String cell) {
		return evaluate(getLiteral(cell));
	}

	public String getLiteral(String cell) {
		if (cells.get(cell) != null) {
			return cells.get(cell);
		} else {
			return "";
		}
	}
	
	public void put(String cell, String value) {
		cells.put(cell, value);
	}

	private String evaluate(String value) {
		if (isNumeric(value)) {
			return value.trim();
		} else {
			return value;
		}
	}
	
	private boolean isNumeric(String value) {
		try {
			Integer.parseInt(value.trim());
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

}
