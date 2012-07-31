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
		} else if (isFormula(value)) {
			return evalFormula(value);
		} else if (isNumeric(value)) {
			return evalNumeric(value);
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
		return evalExpression(formula.substring(1));
	}

	private String evalExpression(String expr) {
		if (hasParens(expr)) {
			return evalParens(expr);
		} else if (isNumeric(expr)) {
			return evalNumeric(expr);
		} else {
			return expr;
		}
	}
	
	private boolean hasParens(String expr) {
		if (expr.indexOf('(') != -1) {
			return true;
		} else {
			return false;
		}
	}

	private String evalParens(String expr) {
		// find last open paren
		int open = expr.lastIndexOf('(');
		// find closing paren
		int close = open + 1;
		while (expr.charAt(close) != ')') { close++; }
		String subexpr = expr.substring(open + 1, close);
		String evalsub = evalExpression(subexpr);
		return evalExpression(expr.substring(0,open) + evalsub + expr.substring(close + 1));
	}
	
	private boolean isNumeric(String value) {
		try {
			Integer.parseInt(value.trim());
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	private String evalNumeric(String value) {
		return value.trim();
	}

}
