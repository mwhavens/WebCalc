package ExpressionObject;
import ExpressionTokenizer.*;

/*
 * Data type to hold Factors of an expression. Contains the equation(by tokens)
 *  of the expression. Holds the values of idents previously stored in IDtables 
 *  for reference of value later in tree.
 */
public class Factor implements ExpressionObject {
	private double myVal;
	private ExpressionTokenizer myEquation;
	private String Type;
	private ExpressionObject myFactor;
	private IdentTable IDTable;
	
	public Factor(){
		myVal = 0;
		myEquation = new ExpressionTokenizer();
		Type = "Number";
		myFactor = null;
		IDTable = new IdentTable();
	}
	
	public Factor(String myEq){
		myVal = 0;
		myEquation = new ExpressionTokenizer(myEq);
		myEquation.gotoFirstToken();
		IDTable = new IdentTable();
		parse();
	}
	
	public Factor(ExpressionTokenizer myEq){
		myVal = 0;
		myEquation =myEq;
		myEquation.gotoFirstToken();
		IDTable = new IdentTable();
		parse();
	}
	
	public Factor(IdentTable myIDTable){
		myVal = 0;
		myEquation = new ExpressionTokenizer();
		Type = "Number";
		myFactor = null;
		IDTable = myIDTable;
	}
	
	public Factor(String myEq,IdentTable myIDTable){
		myVal = 0;
		myEquation = new ExpressionTokenizer(myEq);
		myEquation.gotoFirstToken();
		IDTable = myIDTable;
		parse();
	}
	
	public Factor(ExpressionTokenizer myEq,IdentTable myIDTable){
		myVal = 0;
		myEquation =myEq;
		myEquation.gotoFirstToken();
		IDTable = myIDTable;
		parse();
	}
	
	public void setEquation(String myEq){
		myEquation = new ExpressionTokenizer(myEq);
	}
	
	//returns its value iff it's a number, otherwise returns the factor objects value.
	public double getValue(){
		if(myFactor==null)
			return myVal;
		return myFactor.getValue();
	}
	
	//returns that this ExpressionObject is a factor and appends the type of factor it is
	public String getType(){
		return "Factor:" + Type;
	}
	
	public ExpressionTokenizer getEquation(){
		return myEquation;
	}
	//Determines which type of factor it is. Sets its state accordingly
	public ExpressionObject parse(){
		ExpressionTokenizer curEq = new ExpressionTokenizer(myEquation);
		if(curEq.getType().compareTo("Number")==0){
			Type = "Number";
			myFactor = null;
			myVal = Double.parseDouble(curEq.getToken());
		} else if(curEq.getType().compareTo("Parenthesis")==0){
			curEq.gotoFirstToken();
			curEq.remove(); //"("
			curEq.gotoLastToken();
			curEq.remove(); //")"
			Type = "Expr";
			myFactor = new Expr(curEq,IDTable);
		} else {
			if(Ident.isFunction(myEquation.getToken())){
				Type = "Function";
				myFactor = new Function(myEquation,IDTable);
			} else {
				Type = "Ident";
				myFactor = new Ident(myEquation,IDTable);
			}
		}
		return this;
	}
}
