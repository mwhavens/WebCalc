package ExpressionObject;
import java.util.LinkedList;

import ExpressionTokenizer.*;

/*
 * Data type to hold Terms of an expression. Contains the equation(by tokens)
 *  of the expression. and a queue of factors in the expression. 
 *  Holds the values of idents previously stored in IDtables 
 *  for refrence of value later in tree
 */
public class Term implements ExpressionObject {
	private double myVal;
	private ExpressionTokenizer myEquation;
	private LinkedList<Object> factors;
	private IdentTable IDTable;
	
	public Term(){
		myVal = 0;
		myEquation = new ExpressionTokenizer();
		factors = new LinkedList<Object>();
		IDTable = new IdentTable();
		parse();
	}
	public Term(String myEq){
		myVal = 0;
		myEquation = new ExpressionTokenizer(myEq);
		factors = new LinkedList<Object>();
		IDTable = new IdentTable();
		parse();
	}
	public Term(ExpressionTokenizer myEq){
		myVal = 0;
		myEquation = myEq;
		factors = new LinkedList<Object>();
		IDTable = new IdentTable();
		parse();	
	}
	public Term(IdentTable myIDTable){
		myVal = 0;
		myEquation = new ExpressionTokenizer();
		factors = new LinkedList<Object>();
		IDTable = myIDTable;
		parse();	
	}
	public Term(String myEq,IdentTable myIDTable){
		myVal = 0;
		myEquation = new ExpressionTokenizer(myEq);
		factors = new LinkedList<Object>();
		IDTable = myIDTable;
		parse();
	}
	public Term(ExpressionTokenizer myEq,IdentTable myIDTable){
		myVal = 0;
		myEquation = myEq;
		factors = new LinkedList<Object>();
		IDTable = myIDTable;
		parse();
	}
	
	public void setEquation(String myEq){
		myEquation = new ExpressionTokenizer(myEq);
	}
	
	/*
	 * returns the value of the term.
	 */
	@SuppressWarnings("unchecked")
	public double getValue(){
		double num1=0, num2=0;
		//ensures Queue of factors is not destroyed after return
		LinkedList<Object> tempFactors = (LinkedList<Object>) factors.clone();
		Operation curOp;
		if(myEquation.isEmpty())
			return 0;
		num1 = ((Factor)factors.removeFirst()).getValue();
		//Gets value of all its factors then computes the value of the result of said
		//factors
		while(!factors.isEmpty()){
			curOp = (Operation)factors.removeFirst();
			num2 = ((Factor)factors.removeFirst()).getValue();
			if(curOp.getOp() == '*'){
				num1 *= num2;
			} else if(curOp.getOp() == '/'){
				if(num2 == 0)
					System.out.println("Divide by 0 error. NAN!");
				num1/=num2;
			} else if(curOp.getOp() == '^'){
				num1 = Math.pow(num1, num2);
			} else {
				System.err.println("Term op not * or /!");
				return 0;
			}
		}
		myVal = num1;
		//sets queue of factors to initial state.
		factors = tempFactors;
		return myVal;
	}
	public String getType(){
		return "Term";
	}
	public ExpressionTokenizer getEquation(){
		
		return myEquation;
	}
	//Parses tokens into a Queue of factors and operations.
	public ExpressionObject parse(){
		int inParanthesis = 0;
		ExpressionTokenizer curTerm = new ExpressionTokenizer(myEquation);
		ExpressionTokenizer tempTerm;
		Operation tempOp;
		while(curTerm.isNext()){
			if(curTerm.getToken().compareTo("(") == 0){
				inParanthesis++;
				curTerm.nextToken();
			}
			else if(curTerm.getToken().compareTo(")") == 0){
				inParanthesis--;
				curTerm.nextToken();
			}
			else if(curTerm.getType().compareTo("TermOp") == 0 && inParanthesis == 0){
				tempTerm = curTerm.split();
				tempOp = new Operation(curTerm.getToken());
				curTerm.remove();
				factors.add(new Factor(curTerm,IDTable));
				factors.add(tempOp);
				curTerm = tempTerm;
				curTerm.gotoFirstToken();
			} else {
				curTerm.nextToken();
			}
		}
		factors.add(new Factor(curTerm,IDTable));
		return this;
	}

}
