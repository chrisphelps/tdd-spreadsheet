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
			return evalNumeric(value).toString();
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
		Stack<Integer> valueStack = new Stack<Integer>();
		StringTokenizer strtok = new StringTokenizer(expr, "()*+", true);
		
		while (strtok.hasMoreTokens()) {
			String token = strtok.nextToken();
			
			if (token.equals("(")) {
				opStack.push("(");
			} else if (token.equals(")")) {
				processStack(opStack, valueStack);
			} else if (token.equals("*")) {
				opStack.push("*");
			} else if (token.equals("+")) {
				processStackWhileLowerPrecedence("+", opStack, valueStack);
				opStack.push("+");
			} else if (isNumeric(token)) {
				valueStack.push(evalNumeric(token));
			}
		}
		
		processStack(opStack, valueStack);
		return valueStack.pop().toString();
	}

	private int getOperatorPrecedence(String op) {
		if (op.equals("+")) return 1;
		if (op.equals("*")) return 2;
		if (op.equals("(")) return 3;
		return 0;
	}
		
	private void processStack(Stack<String> opStack, Stack<Integer> valueStack) {
		while (!opStack.isEmpty()) {
			String op = opStack.pop();
			if (op.equals("(")) {
				return;
			}
			if (op.equals("*")) {
				valueStack.push(evalMultiplication(valueStack));
			}
			else if (op.equals("+")) {
				valueStack.push(evalAddition(valueStack));
			}
		}
	}
	
	private void processStackWhileLowerPrecedence(String op, Stack<String> opStack, Stack<Integer> valueStack) {
		while (!opStack.isEmpty()) {
			String nextOp = opStack.peek();
			
			if (getOperatorPrecedence(op) < getOperatorPrecedence(nextOp)) {
				if (nextOp.equals("(")) {
					return;
				} else if (nextOp.equals("*")) {
					opStack.pop();
					valueStack.push(evalMultiplication(valueStack));
				} else if (nextOp.equals("+")) {
					opStack.pop();
					valueStack.push(evalAddition(valueStack));
				}
			} else {
				return;
			}
		}
	}
	
	private Integer evalMultiplication(Stack<Integer> valueStack) {
		Integer rhs = valueStack.pop();
		Integer lhs = valueStack.pop();
		int prod = lhs.intValue() * rhs.intValue();
		return new Integer(prod);
	}

	private Integer evalAddition(Stack<Integer> valueStack) {
		Integer rhs = valueStack.pop();
		Integer lhs = valueStack.pop();
		int prod = lhs.intValue() + rhs.intValue();
		return new Integer(prod);
	}
	
	private boolean isNumeric(String value) {
		try {
			Integer.parseInt(value.trim());
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	private Integer evalNumeric(String value) {
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

}
