package ExpressionObject;
import ExpressionTokenizer.*;
import java.util.List;

/*
 * ExpressionObject used to contain an Expression Ident. This includes the name(UNIQUE)
 *  of the Ident, an Expr associated with the Ident, if there is one. A map of previously
 *  assigned Idents and values, for use in future calculations. A set of names this current
 *  Ident's value is dependent on. 
 */

public class Ident implements ExpressionObject {
	private ExpressionTokenizer myEquation;
	private String name;
	private Expr myExpr;
	private IdentTable IDTable;
	private IdentTable dependants;
	
	public Ident(){
		dependants = new IdentTable();
		IDTable = new IdentTable();
		myExpr = null;
		myEquation = null;
		name = "";
	}
	
	public Ident(String myEq){
		dependants = new IdentTable();
		IDTable = new IdentTable();
		myExpr = null;
		myEquation = null;
		name = myEq;
	}
	
	public Ident(ExpressionTokenizer myEq){
		dependants = new IdentTable();
		IDTable = new IdentTable();
		myExpr = null;
		myEquation = null;
		name = myEq.getToken();
	}
	public Ident(IdentTable myIDTable){
		dependants = new IdentTable();
		IDTable = myIDTable;
		myExpr = null;
		myEquation = null;
		name = "";
	}
	
	public Ident(String myEq,IdentTable myIDTable){
		dependants = new IdentTable();
		IDTable = myIDTable;
		name = myEq;
		if(IDTable.getIdentValue(name)==null){
			myExpr = null;
			myEquation = null;
		}else{
			myExpr = new Expr(IDTable.getIdentValue(name).getEquation(), IDTable);
			myExpr.setIDTable(myIDTable);
			myEquation = myExpr.getEquation();
			setDependants();
		}
	}
	
	public Ident(ExpressionTokenizer myEq, IdentTable myIDTable){
		dependants = new IdentTable();
		IDTable = myIDTable;
		myEquation = myEq;
		name = myEq.getToken();
		if(IDTable.getIdentValue(name)==null){
			myExpr = null;
			myEquation = null;
		}else{
			myExpr = new Expr(IDTable.getIdentValue(name).getEquation(), IDTable);
			myExpr.setIDTable(myIDTable);
			myEquation = myExpr.getEquation();
			setDependants();
		}
	}
	
	public void setEquation(String myEq){
		myExpr = new Expr(myEq, IDTable);
		myEquation = myExpr.getEquation();
		setDependants();
	}
	
	/*
	 * Method which returns the value of the Idents expr value, if there is one; otherwise
	 * returns 0, with error msg.
	 */
	public double getValue(){
		if(myExpr == null){
			System.out.println("getVal called for ident with no Expr assossiated!");
			return 0;
		}
		return myExpr.getValue();
	}
	
	public String getType(){
		return "Ident";
	}
	
	public String Name(){
		return name;
	}
	
	public void setName(String toName){
		name = toName;
	}
	
	public ExpressionTokenizer getEquation(){
		return myEquation;
	}
	
	public void setEquation(ExpressionTokenizer newEquation){
		myExpr = new Expr(newEquation, IDTable);
		myEquation = myExpr.getEquation();
		setDependants();
	}
	
	public Expr getExpr(){
		return myExpr;
	}
	
	public void setExpr(Expr newExpr){
		myExpr = new Expr(newExpr.getEquation(), IDTable);
		myEquation = myExpr.getEquation();
		setDependants();
	}
	
	public void setExpr(String myEq){
		myExpr = new Expr(myEq, IDTable);
		myEquation = myExpr.getEquation();
		setDependants();
	}
	
	/*
	 * Meant to parse the Idents Equation to set its name and expr value. not needed but left
	 * as every other ExpressionObject has a parse method, so that assuption should hold for
	 * the Ident ExpressionObject too.
	 */
	public ExpressionObject parse(){
		if(myEquation != null)
			name = myEquation.getToken();
		return this;
	}
	
	/*
	 * returns a String with the name of the Ident an Equal sign and the Expr value's string
	 * in the form name=expr, EXAMPLE A2=sin(B1/10)+9 
	 */
	public String toString(){
		if(myExpr == null)
			return name;
		return name + "=" + myExpr.toString();
	}
	
	/*
	 * Useful method when dealing with Idents to determine if they are a Function or not.
	 * Returns true if the name matches that of a function. False otherwise.
	 */
	public static boolean isFunction(String isName){
		if(isName.compareToIgnoreCase("sin") == 0 || 
				isName.compareToIgnoreCase("cos") == 0 ||
				isName.compareToIgnoreCase("sqrt") == 0 ||
				isName.compareToIgnoreCase("min") == 0 ||
				isName.compareToIgnoreCase("max") == 0 ||
				isName.compareToIgnoreCase("avg") == 0)
				{
			return true;
		}
		return false;
	}
	
	/*
	 * Method used to determine if this Ident is a Function, checks name compared to reserved
	 * function names. Returns true is Ident's name is the same an a function name. 
	 * False otherwise;
	 */
	public boolean isFunction(){
		if(name.compareToIgnoreCase("sin") == 0 || 
				name.compareToIgnoreCase("cos") == 0 ||
				name.compareToIgnoreCase("sqrt") == 0 ||
				name.compareToIgnoreCase("min") == 0 ||
				name.compareToIgnoreCase("max") == 0 ||
				name.compareToIgnoreCase("avg") == 0)
				{
			return true;
		}
		return false;
	}
	
	/*
	 * Parses the Ident's Expression value for Idents this Ident is dependent on.
	 * Returns a list of idents this Ident is dependent on or null if no Expression value
	 * has been assigned to this Ident yet.
	 */
	public List<Ident> getDependancies(){
		IdentTable dependancyTable = new IdentTable();
		if(myEquation == null || myEquation.isEmpty())
			return null;
		myEquation.gotoFirstToken();
		if(myEquation.getType().compareTo("Ident")==0)
			addDependancies(myEquation.getToken(), dependancyTable);
		while(myEquation.isNext()){
			myEquation.nextToken();
			if(myEquation.getType().compareTo("Ident")==0)
				addDependancies(myEquation.getToken(), dependancyTable);
		}
		myEquation.gotoFirstToken();
		return dependancyTable.getIDList();
	}
	
	/*
	 * returns the list of Idents that depend on this Ident and its value to generate its own
	 * value correctly.
	 */
	public List<Ident> getDependants(){
		return dependants.getIDList();
	}
	
	private void addDependancies(String idName, IdentTable idTable){
		if(!Ident.isFunction(idName)){
			idTable.add(idName);
		}
	}
	
	/*
	 * Called by other idents to notify this ident that it requires its value,
	 * to generate its own value correctly. Adds the name of the dependent ident to its set
	 * of dependents.
	 */
	public void addDependants(String idName){
		if(!Ident.isFunction(idName)){
			dependants.add(idName);
		}
	}
	
	/*
	 * Method used to notify the idents, this Ident is dependent on to generate its value
	 * Correctly, that it dependent on it. That ident adds the name of this Ident to its list
	 * of Dependents.
	 */
	private void setDependants(){
		List<Ident> myDependancies = getDependancies();
		Ident tempIdent;
		if(myDependancies != null){
			System.out.println("Size:"+myDependancies.size());
			for(int i = 0; i < myDependancies.size(); ++i){
				System.out.println("Dependant["+ i + "]:" + myDependancies.get(i).Name());
				tempIdent = IDTable.getIdent(myDependancies.get(i).Name());
				if(tempIdent != null)
					tempIdent.addDependants(name);
			}
		}
	}
	
	/*
	 * Method useful when dealing with Idents to convert the Column name into a number.
	 * Expects a string of all capital letters and converts to a number using base 26.
	 */
	public static int getIDNameLetterValue(String myName){
		String nameLetters = getIDNameLetters(myName);
		int rtnval = 0;
		for(int i = nameLetters.length()-1; i >= 0; --i){
			rtnval += ((int)(nameLetters.charAt(i) - 'A'+ 1)) * Math.pow(26, nameLetters.length()-i-1);
		}
		return rtnval-1;
	}
	
	/*
	 * Method useful when dealing with Idents to obtain the row or # part of an Ident's name.
	 * Expects a String which follows the grammar associated with Idents. Returns the
	 * row or # of an Ident's name.
	 */
	public static int getIDNameNumber(String myName){
		try{
			for(int i=0; i < myName.length(); ++i){
				if(Character.isDigit(myName.charAt(i)))
					return Integer.parseInt(myName.substring(i));
			}
		}catch(NumberFormatException err){
			;
		}
		return -1;
	}
	
	/*
	 * Method useful when dealing with Idents to obtain the column or letter part of 
	 * an Ident's name. Expects a String which follows the grammar associated with Idents.
	 *  Returns the column or letter part of an Ident's name.
	 */
	public static String getIDNameLetters(String myName){
		for(int i = 0; i < myName.length(); ++i){
			if(!Character.isLetter(myName.charAt(i)))
				return myName.substring(0, i);
		}
		return myName;
	}
	
	/*
	 * method useful when dealing with Idents to get the Letter part of an Ident name
	 * given only a number. Expects a positive integer argument. Returns the string of
	 * letters associated with that number uniquely
	 */
	public static String getIDNameLetters(int letterValue){
		String rtnval = "";
		int length, digit, tempvalue = letterValue;
		for(length = 1; tempvalue / Math.pow(26, length) >= 1; ++length)
			tempvalue = (int) (tempvalue - Math.pow(26, length));
		tempvalue = letterValue;
		for(int i = 0; i < length; ++i){
			digit = tempvalue%26;
			rtnval = (char)('A'+ digit) + rtnval;
			tempvalue =tempvalue/26-1;
		}
		return rtnval;
	}
}
