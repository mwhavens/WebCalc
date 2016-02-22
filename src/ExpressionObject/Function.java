package ExpressionObject;
import ExpressionTokenizer.*;

/*
 * Expression Object used to hold the value/expression of a given function. Contains the
 * name of the function, the expression to apply the function on, and holds the values
 * of all the previously assigned idents for use in future calculations.
 */
public class Function implements ExpressionObject {
	private ExpressionTokenizer myEquation;
	private String name;
	private Expr[] myExpr;
	private IdentTable IDTable;
	
	public Function(){
		IDTable = new IdentTable();
		myEquation = new ExpressionTokenizer();
		name = "";
		myExpr = null;
	}
	
	public Function(String myEq){
		IDTable = new IdentTable();
		myEquation = new ExpressionTokenizer(myEq);
		myEquation.gotoFirstToken();
		parse();
	}
	
	public Function(ExpressionTokenizer myEq){
		IDTable = new IdentTable();
		myEquation = myEq;
		myEquation.gotoFirstToken();
		parse();
	}
	public Function(IdentTable myIDTable){
		IDTable = myIDTable;
		myEquation = new ExpressionTokenizer();
		name = "";
		myExpr = null;
	}
	
	public Function(String myEq, IdentTable myIDTable){
		IDTable = myIDTable;
		myEquation = new ExpressionTokenizer(myEq);
		myEquation.gotoFirstToken();
		parse();
	}
	
	public Function(ExpressionTokenizer myEq, IdentTable myIDTable){
		IDTable = myIDTable;
		myEquation = myEq;
		myEquation.gotoFirstToken();
		parse();
	}
	
	public void setEquation(String myEq){
		myEquation = new ExpressionTokenizer(myEq);
	}
	
	/*
	 * returns the value of the function applied to its expression.
	 */
	public double getValue(){
		double myVal = 0, tempVal = 0;
		if(name.compareToIgnoreCase("sin")==0)
			myVal = Math.sin(myExpr[0].getValue());
		else if(name.compareToIgnoreCase("cos")==0)
			myVal = Math.cos(myExpr[0].getValue());
		else if(name.compareToIgnoreCase("sqrt")==0)
			myVal = Math.sqrt(myExpr[0].getValue());
		else if(name.compareToIgnoreCase("min")==0){
			for(int i = 0; i < myExpr.length; ++i){
				if(i == 0)
					myVal = myExpr[0].getValue();
				else{
					tempVal = myExpr[i].getValue();
					if(tempVal < myVal)
						myVal = tempVal;
				}
			}
			
		}else if(name.compareToIgnoreCase("max")==0){
			for(int i = 0; i < myExpr.length; ++i){
				if(i == 0)
					myVal = myExpr[0].getValue();
				else{
					tempVal = myExpr[i].getValue();
					if(tempVal > myVal)
						myVal = tempVal;
				}
			}
		}else if(name.compareToIgnoreCase("avg")==0){
			for(int i = 0; i < myExpr.length; ++i){
					myVal += myExpr[i].getValue();
			}
			myVal /= myExpr.length;
		}		
		return myVal;
	}
	public String getType(){
		return "Function";
	}
	public ExpressionTokenizer getEquation(){
		return myEquation;
	}
	
	/*
	 * parses the Equation into a name of the function and the expression to apply the
	 * function on.
	 */
	public ExpressionObject parse(){
		String[] parsedExpr;
		ExpressionTokenizer myTokens = new ExpressionTokenizer(myEquation);
		myTokens.gotoFirstToken();
		name = myTokens.getToken();
		myTokens.remove(); //function of name
		myTokens.remove();//'('
		myTokens.gotoLastToken();
		myTokens.remove(); //')'
		parsedExpr = myTokens.toString().split(":");
		myExpr = new Expr[parsedExpr.length];
		for(int i = 0; i < myExpr.length; ++i){
			myExpr[i] = new Expr(parsedExpr[i], IDTable);
		}
		//myExpr = new Expr(myTokens,IDTable);
		return this;
	}

}
