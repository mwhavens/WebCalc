package ExpressionTokenizer;
import java.util.LinkedList;


/*
 * An Object class which tokenizes a given string into useful expression
 * tokens. Includes many operations useful when dealing with lists of Tokens.
 */

public class ExpressionTokenizer {
	private LinkedList<ExpressionToken>tokens;
	private int pos;
	
	public ExpressionTokenizer(){
		pos = 0;
		tokens = new LinkedList<ExpressionToken>();
	}
	public ExpressionTokenizer(String toTokenize){
		pos = 0;
		tokens = new LinkedList<ExpressionToken>();
		tokenize(toTokenize);
	}
	@SuppressWarnings("unchecked")
	public ExpressionTokenizer(ExpressionTokenizer etVal){
		pos = 0;
		tokens = (LinkedList<ExpressionToken>) etVal.getTokenList().clone();
	}
	
	public LinkedList<ExpressionToken> getTokenList(){
		return tokens;
	}
	public String getToken(){
		return (tokens.get(pos)).token();
	}
	public String getType(){
		return (tokens.get(pos)).type();
	}
	public boolean isEmpty(){
		return tokens.isEmpty();
	}
	/*
	 * returns true if there is another token past current position
	 * false otherwise
	 */
	public boolean isNext(){
		return (pos+1 < tokens.size());
	}
	
	public boolean isLast(){
		return pos >= tokens.size()-1;
	}
	
	public void setTokenList(LinkedList<ExpressionToken> setVal){
		tokens = setVal;
	}
	public void gotoLastToken(){
		pos = tokens.size() - 1;
	}
	public void gotoFirstToken(){
		pos = 0;
	}
	/*
	 * removes current token and makes current position, the previous token
	 * returns the token which was removed
	 */
	public ExpressionToken remove(){
		//print();
		if(pos < tokens.size()){// && pos >= 0
			//System.out.println("pos:" + pos);
			if(pos == 0)
				return tokens.remove(pos);
			pos--;
			return tokens.remove(pos+1);
				
		}
		System.out.println("Remove failed, Element past end");
		return null;
	}
	public void print(){
		for(int i = 0; i < tokens.size(); ++i){
			System.out.print("Node[" + i + "]:Val = " + 
				tokens.get(i).token() + " \t Type = " + tokens.get(i).type() + " ");
		}
		System.out.println("");
	}
	
	public String toString(){
		String myString = "";
		for(int i = 0; i < tokens.size(); ++i){ 
			myString = myString + tokens.get(i).token();
		}
		return myString;
	}
	/*
	 * Moves position to indicate next token is current token 
	 */
	public void nextToken(){
		pos++;
	}
	public void previousToken(){
		pos--;
	}
	public void setToken(String setVal){
		tokens.get(pos).setToken(setVal);
		setType(setVal);
	}
	public void setType(String setVal){
		setType(setVal.charAt(0));
	}
	/*
	 * Adds an addition attribute to each token, useful for more adv. operations
	 * being used with the tokens.  Generalized information about the type of token
	 * is stored as value.
	 */
	public void setType(char setVal){
		switch(setVal){
		case '-':
		case '+': tokens.get(pos).setType("ExprOp");
			break;
		case '^':
		case '*':
		case '/': tokens.get(pos).setType("TermOp");
			break;
		case '(':
		case ')': tokens.get(pos).setType("Parenthesis");
			break;
		case '=': tokens.get(pos).setType("Equal");
			break;
		case '$': tokens.get(pos).setType("Absolute");
			break;
		case ':': tokens.get(pos).setType("Colon");
			break;
		default:
			if(Character.isDigit(setVal))
				tokens.get(pos).setType("Number");
			else if(Character.isLetter(setVal))
				tokens.get(pos).setType("Ident");
			else{
				tokens.get(pos).setType("Unknown");
			}
		}
	}
	public void appendToken(String appendVal){
		tokens.get(pos).appendToken(appendVal);
	}
	/*
	 * Creates a new token at end of token list.
	 */
	public void newToken(){
		tokens.add(new ExpressionToken());
		pos = tokens.size() - 1;
	}
	public void newToken(ExpressionToken newToken){
		tokens.add(newToken);
		pos = tokens.size() - 1;
	}
	public void newToken(String tokenVal){
		tokens.add(new ExpressionToken());
		pos = tokens.size() - 1;
		setToken(tokenVal);
	}
	/*
	 * Splits the list of tokens into 2 seperate lists around current position.
	 * Returns a list of tokens after the current position. 
	 * The class list was all tokens after current position removed from its list.
	 */
	public ExpressionTokenizer split(){//Return the list after current pos.
		ExpressionTokenizer tailTokens = new ExpressionTokenizer();
		while(isNext()){
			tailTokens.newToken(tokens.remove(pos+1));
		}
		return tailTokens;
	}
	//Removes spaces from an input string and returns the result
	private String trimSpaces(String toTrim){
		return toTrim.replace(" ", "");
	}
	
	//Actual method used to tokenize Characters.
	/*
	 * Assumes a sum what formated string, numbers only have 1 decimal point 
	 * and the grammer is mainly adheared too.
	 * insures that the token list is at the beging when it returns.
	 */
	public void tokenize(String toTokenize){
		String toToken = trimSpaces(toTokenize);
		int strPos = 0;
		char curChar, nextChar;
		if(toToken.isEmpty()){
			System.out.println("Tokenize String is empty!");
		} else {
			do{
				curChar = toToken.charAt(strPos);
				//set current token to base character.
				newToken(""+curChar);
				if(strPos+1 < toToken.length()){
					//while current token is a number and there's more 
					//numbers or a decimal point, add charaters to current token
					//and move to next part of string to tokenize
					if(getType().compareTo("Number")==0){
						nextChar = toToken.charAt(strPos+1);
						while(Character.isDigit(nextChar) ||nextChar == '.'){
							appendToken(""+nextChar);
							strPos++;
							if(strPos+1 < toToken.length())
								nextChar = toToken.charAt(strPos+1);
							else
								break;
						}
					//while current token is an ident and theres more letters or numbers
					//add characters to current token and more forward in the string 
					//which needs to be tokenized
					} else if(getType().compareTo("Ident")==0){
						nextChar = toToken.charAt(strPos+1);
						while(Character.isLetterOrDigit(nextChar)){
							appendToken(""+nextChar);
							strPos++;
							if(strPos+1 < toToken.length())
								nextChar = toToken.charAt(strPos+1);
							else
								break;
						}
					}
				}
			++strPos;
			}while(strPos < toToken.length());
		}
		formatTokens();
		gotoFirstToken();
	}
	/*
	 * Helps to ensure robustness and prevents breaks from certain flaws. Corrects
	 * multiple +/- signs and ensure that there is always a balanced number of 
	 * parenthesis by added the deficit open/close parenthesis to start/end of
	 * token list respectively
	 */
	private void formatTokens(){
		int openParen = 0;
		String curToken, nextToken;
		for(int i = 0; i < tokens.size(); ++i){
			curToken = tokens.get(i).token();
			if(curToken.compareTo("(") == 0)
				openParen++;
			else if(curToken.compareTo(")") == 0)
				openParen--;
			else if(curToken.compareTo("+") == 0){
				if(i+1 < tokens.size()){
					nextToken =  tokens.get(i+1).token();
					if(nextToken.compareTo("+") == 0)
						tokens.remove(i+1);
					else if(nextToken.compareTo("-") == 0)
						tokens.remove(i);
				}
			}else if(curToken.compareTo("-") == 0){
				if(i+1 < tokens.size()){
					nextToken =  tokens.get(i+1).token();
					if(nextToken.compareTo("+") == 0)
						tokens.remove(i+1);
					else if(nextToken.compareTo("-") == 0){
						tokens.get(i).setToken("+");
						tokens.remove(i+1);
					}
				}
			}
		}
		while(openParen > 0){
			tokens.add(new ExpressionToken(")", "Parenthesis"));
			openParen--;
		}
		while(openParen < 0){
			tokens.addFirst(new ExpressionToken("(", "Parenthesis"));
			openParen++;
		}
	}
	
	
}

/*
 * Primative Object type created to hold a particular Token. Useful tokenizing methods
 * built into to help tokenizer. holds the token value and an additional data field
 * which contains generalized information about that token.
 */
class ExpressionToken {
	private String type, token;
	
	public ExpressionToken(){
		type = "Number";
		token = "0";
	}
	public ExpressionToken(String val, String toType){
		type = toType;
		token = val;
	}
	public void setType(String val){
		type = val;
	}
	public void setToken(String val){
		token = val;
	}
	public void appendToken(String val){
		token = token.concat(val);
	}
	public String type(){
		return type;
	}
	public String token(){
		return token;
	}	
}