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
		if (value == null || value.equals("")) {
			return "";
		} else if (isNumeric(value)) {
			return value.trim();
		} else if (isFormula(value)) {
			return evalFormula(value);
		} else {
			return value;
		}
	}

	private boolean isFormula(String value) {
		if (value == null || value.equals("")) {
			return false;
		} else {
			return value.charAt(0) == '=';
		}
	}
	
	private String evalFormula(String formula) {
		return evaluate (formula.substring(1));
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
