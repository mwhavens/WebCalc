package ExpressionObject;
import ExpressionTokenizer.*;
import java.util.LinkedList;

/*
 * Base of an expression tree. Contains the equation(by tokens) of the expression.
 * and a queue of terms in the expression. Holds the values of idents previously
 * stored in IDtables for refrence of value later in tree.
 */
public class Expr implements ExpressionObject{
	private double myVal;
	private ExpressionTokenizer myEquation;
	private LinkedList<Object> terms;
	private IdentTable IDTable;
	
	public Expr(){
		myVal = 0;
		myEquation = new ExpressionTokenizer();
		terms = new LinkedList<Object>();
		IDTable = new IdentTable();
		parse();
	}
	
	public Expr(String myEq){
		myVal = 0;
		myEquation = new ExpressionTokenizer(myEq);
		terms = new LinkedList<Object>();
		IDTable = new IdentTable();
		parse();
	}
	
	public Expr(ExpressionTokenizer myEq){
		myVal = 0;
		myEquation = myEq;
		terms = new LinkedList<Object>();
		IDTable = new IdentTable();
		parse();
	}
	
	public Expr(IdentTable myIDTable){
		myVal = 0;
		myEquation = new ExpressionTokenizer();
		terms = new LinkedList<Object>();
		IDTable =  myIDTable;
		parse();
	}
	
	public Expr(String myEq, IdentTable myIDTable){
		myVal = 0;
		myEquation = new ExpressionTokenizer(myEq);
		terms = new LinkedList<Object>();
		IDTable =  myIDTable;
		parse();
	}
	
	public Expr(ExpressionTokenizer myEq, IdentTable myIDTable){
		myVal = 0;
		myEquation = myEq;
		terms = new LinkedList<Object>();
		IDTable =  myIDTable;
		parse();
	}
	
	public void setEquation(String myEq){
		myEquation = new ExpressionTokenizer(myEq);
	}
	
	public void setIDTable(IdentTable newIDTable)
	{
		IDTable = newIDTable;
	}
	
	@SuppressWarnings("unchecked")
	/*
	 * returns the value of the Expression.
	 */
	public double getValue(){
		double num1=0, num2=0;
		//ensures queue of terms is not destroyed after return.
		LinkedList<Object> tempTerms = (LinkedList<Object>) terms.clone();
		Operation curOp;
		
		//Iterates through terms and operations recursively getting values from
		//terms in its queue and calculating the value of the operations between term
		//values.
		if(myEquation.isEmpty())
			return 0;
		try{
			//Handles sign at beging of expression!
			curOp = (Operation)terms.element();
			curOp = (Operation)terms.removeFirst();
			if(myEquation.isEmpty()){
				return 0;
			}
			num1 = ((Term)terms.removeFirst()).getValue();
			if(curOp.getOp() == '-')
				num1 *= -1;
			else
				;//Should be + if not - so do nothing =)
		}catch(java.lang.ClassCastException err){
			num1 = ((Term)terms.removeFirst()).getValue();
		}
		while(!terms.isEmpty()){
			curOp = (Operation)terms.removeFirst();
			num2 = ((Term)terms.removeFirst()).getValue();
			if(curOp.getOp() == '+'){
				num1 += num2;
			} else if(curOp.getOp() == '-'){
				num1-=num2;
			} else {
				System.err.println("Expr op not +/-!");
				return 0;
			}
		}
		myVal = num1;
		//set term queue back to initial state
		terms = tempTerms;
		return myVal;
	}
	public String getType(){
		return "Expr";
	}
	public ExpressionTokenizer getEquation(){	
		return myEquation;
	}
	
	/*
	 * Parses tokens into a queue of terms and operations to be used later.
	 */
	public ExpressionObject parse(){
		int inParanthesis = 0;
		ExpressionTokenizer curTerm = new ExpressionTokenizer(myEquation);
		ExpressionTokenizer tempTerm;
		Operation tempOp;
		if(curTerm.isNext() && curTerm.getType().compareTo("ExprOp") == 0){
			terms.add(new Operation(curTerm.getToken()));
			curTerm.remove();
			curTerm.gotoFirstToken();
		}
			
		while(curTerm.isNext()){
			if(curTerm.getToken().compareTo("(") == 0){
				inParanthesis++;
				curTerm.nextToken();
			}
			else if(curTerm.getToken().compareTo(")") == 0){
				inParanthesis--;
				curTerm.nextToken();
			}
			else if(curTerm.getType().compareTo("ExprOp") == 0 && inParanthesis == 0){
				tempTerm = curTerm.split();
				tempOp = new Operation(curTerm.getToken());
				curTerm.remove(); //Remove the op after its added to the queue.
				terms.add(new Term(curTerm,IDTable));
				terms.add(tempOp);
				curTerm = tempTerm;
				curTerm.gotoFirstToken();
			}else{
				curTerm.nextToken();
			}
		}
		terms.add(new Term(curTerm,IDTable));
		
		return this;
	}
	
	public String toString(){
		return myEquation.toString();
	}
	
	/*
	 * Determines the Expr string of a an Expr string under the rules of Absolute/Relative
	 * positioning, Requires the row and column of the initial cell[0] and the row column of the
	 * cell who's "perspective" is wanted[1]. The Expr of the initial cell is also required.
	 * 
	 */
	public static String absoluteExprConversion(int[] row, int[] column, String curExpr){
		ExpressionTokenizer tokens = new ExpressionTokenizer(curExpr);
		String[] token = new String[2];
		
		while(!tokens.isLast()){
			//check if token is Ident and not a function
			if(tokens.getType().compareTo("Ident") == 0 && !Ident.isFunction(tokens.getToken())){
				tokens.nextToken();
				//check the next token to see if its an "$"
				if(tokens.getType().compareTo("Absolute")==0){
					tokens.previousToken();
					//set the previous token to the converted column token then move to row token
					tokens.setToken(convertColumn(column, tokens.getToken()));
					tokens.nextToken();//'$'
					tokens.nextToken();//'#' <- Already set
				} else {
					//set the ident to the converted column and row ident
					tokens.previousToken();
					token[0] = Ident.getIDNameLetters(tokens.getToken());
					token[1] = "" + Ident.getIDNameNumber(tokens.getToken());
					tokens.setToken(convertColumn(column, token[0])+ 
							convertRow(row, token[1]));
				}
			//check if token is Absolute token
			} else if(tokens.getType().compareTo("Absolute")==0){
				tokens.nextToken(); //ident
				//if there are more tokens and the next token is an absolute move to end of row token
				if(tokens.isNext()){
					tokens.nextToken();
					if(tokens.getType().compareTo("Absolute")==0){
						tokens.nextToken();
					} else {
						//if the next token is not an absolute, convert the previous tokens row.
						tokens.previousToken();
						token[0] = Ident.getIDNameLetters(tokens.getToken());
						token[1] = "" + Ident.getIDNameNumber(tokens.getToken());
						tokens.setToken(token[0] + convertRow(row, token[1]));
					}
				} else {
					//if there are no more tokens after the ident, convert the current ident's row
					token[0] = Ident.getIDNameLetters(tokens.getToken());
					token[1] = "" + Ident.getIDNameNumber(tokens.getToken());
					tokens.setToken(token[0] + convertRow(row, token[1]));
				}
			}
			if(tokens.isNext())
				tokens.nextToken();
		}
		//if i'm checking the last token, just need to determine if its an ident in need of conversion
		if(tokens.getType().compareTo("Ident") == 0 && !Ident.isFunction(tokens.getToken())){
			token[0] = Ident.getIDNameLetters(tokens.getToken());
			token[1] = "" + Ident.getIDNameNumber(tokens.getToken());
			tokens.setToken(convertColumn(column, token[0])+ 
					convertRow(row, token[1]));
		}
		return tokens.toString();
	}
	
	/*
	 * Math function given the initial & new cell's column and the value of the tokens column
	 * gives the resulting column from the perspective of the new cell. If its an invalid cell
	 * returns 0 for the column.
	 */
	private static String convertColumn(int[] column, String curValue){
		//Return (C, + (C - C.))
		int rtnval = column[1] + (Ident.getIDNameLetterValue(curValue) - column[0]);
		if(rtnval < 0)
			return Ident.getIDNameLetters(0);
		return Ident.getIDNameLetters(rtnval);
	}
	
	/*
	 * Math function given the initial & new cell's row and the value of the tokens row
	 * gives the resulting row from the perspective of the new cell. If its an invalid cell
	 * returns 0 for the row.
	 */
	private static int convertRow(int[] row, String curValue){
		//Return (#, + (# - #.))
		int rtnval;
		try{
			rtnval = row[1] + (Integer.parseInt(curValue) - row[0]);
		} catch(NumberFormatException err){
			System.err.println("ConvertRow Fail, # is not a number!");
			return 0;
		}
		if(rtnval < 0)
			return 0;
		return rtnval;
	}
}
