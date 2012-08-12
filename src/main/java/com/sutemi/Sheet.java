package com.sutemi;

import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

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
		Stack<String> opStack = new Stack<String>();
		Stack<String> valueStack = new Stack<String>();
		StringTokenizer strtok = new StringTokenizer(expr, "()*", true);
		
		while (strtok.hasMoreTokens()) {
			String token = strtok.nextToken();
			
			if (token.equals("(")) {
				opStack.push("(");
			} else if (token.equals(")")) {
				String op = opStack.pop();
				while(!op.equals("(")) {
					if (op.equals("*")) {
						String rhs = valueStack.pop();
						String lhs = valueStack.pop();
						valueStack.push(evalMultiplication(lhs,rhs));
					}
					op = opStack.pop();
				}
				
				
				
				if (!op.equals("(")) {
					return "#Error";
				}
			} else if (token.equals("*")) {
				opStack.push("*");
			} else if (isNumeric(token)) {
				valueStack.push(evalNumeric(token));
			}
		}
		
		while (!opStack.isEmpty()) {
			String op = opStack.pop();
			if (op.equals("*")) {
				String rhs = valueStack.pop();
				String lhs = valueStack.pop();
				valueStack.push(evalMultiplication(lhs,rhs));
			}
		}
		return valueStack.pop();
	}
	
	private String evalMultiplication(String lhs, String rhs) {
		try {
			int intlhs = Integer.parseInt(lhs);
			int intrhs = Integer.parseInt(rhs);
			int prod = intlhs * intrhs;
			return new Integer(prod).toString();
		} catch (NumberFormatException nfe) {
			return "#Error";
		}
	}

	private boolean hasMultiplication(String expr) {
		if (expr.indexOf('*') != -1) {
			return true;
		} else {
			return false;
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
