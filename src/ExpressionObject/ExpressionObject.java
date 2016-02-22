package ExpressionObject;
import ExpressionTokenizer.ExpressionTokenizer;

/*
 * Assumed parts of all pieces of an Expression by tokens.
 */
public interface ExpressionObject {
	void setEquation(String myEq);
	double getValue();
	String getType();
	ExpressionTokenizer getEquation();
	ExpressionObject parse();
}	


