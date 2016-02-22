package ExpressionObject;

//Primative Object used solely for the purpose of hold the operation value.
public class Operation {
	private char op;
	//private String value; not needed yet.
	public Operation(){
		op = ' ';
	}
	public Operation(String myVal){
		op = myVal.charAt(0);
	}
	public Operation(char myVal){
		op = myVal;
	}
	public char getOp(){
		return op;
	}
	public void setOp(char myVal){
		op = myVal;
	}
	public void setOp(String myVal){
		op = myVal.charAt(0);
	}
	public String toString()
	{
		return "Operation";
	}
	/*public double evaluate(double num1, double num2){
		return num1+num2;  //Not needed, so Implementation stopped :'-C
	}*/ 
}
