package ExpressionObject;
import java.util.LinkedList;
import java.io.*;

/*
 * This is an object intended to hold a set of Idents associated with an ExpressionCalculator
 * This is a set of all the previously assigned/created idents.
 */
public class IdentTable implements Serializable {
	private static final long serialVersionUID = -5842874571361544462L;
	private LinkedList<Ident>idents;
	
	public IdentTable(){
		idents = new LinkedList<Ident>();
	}
	
	/*
	 * Adds an ident to the list of idents, if that ident is not already in the table.
	 */
	public void add(String newIdent){
		if(newIdent.compareTo("")!= 0 && !isIn(newIdent))
			idents.add(new Ident(newIdent, this));
		else
			;//System.err.println("Ident already in IDtable!");
	}
	
	/*
	 * Adds an ident to the list of idents, that that ident is not already in the table,
	 * if it is; sets that idents expr to the expr argument of the ident to add.
	 */
	public void add(String newIdent, Expr identVal){
		if(newIdent.compareTo("")!= 0){
			int pos = findIdent(newIdent);
			if(pos >=0){
				idents.get(pos).setExpr(new Expr(identVal.getEquation()));
			} else {
				idents.add(new Ident(newIdent, this));
				idents.getLast().setExpr(new Expr(identVal.getEquation()));
			}
		}
	}
	
	/*
	 * Sets the value of an ident in the table if it exists or adds the ident to the table
	 * if it is not already in and assigns the new value to that ident.
	 */
	public void setIdentValue(String curIdent, Expr val){
		add(curIdent, val);
	}
	
	/*
	 * Returns an Expr object of the Ident if it has a value, returns null if no value is 
	 * assigned to the Ident or the Ident is not in the table.
	 */
	public Expr getIdentValue(String curIdent){
		int pos = findIdent(curIdent);
		if(pos >=0){
			return idents.get(pos).getExpr();
		} else {
			;//System.err.println("Ident not in IDTable!");
			return null;
		}
	}
	
	/*
	 * Returns the position of the Ident if that ident is in the table, Returns -1 otherwise.
	 */
	public int findIdent(String identName){
		if(idents.isEmpty())
			return -1;
		for(int i = 0; i < idents.size(); ++i){
			if(idents.get(i).Name().compareTo(identName) ==0)
				return i;
		}
		return -1;
	}
	
	/*
	 * Returns true if the Ident is in the table already, false otherwise.
	 */
	public boolean isIn(String identName){
		for(int i = 0; i < idents.size(); ++i){
			if(idents.get(i).Name().compareTo(identName) ==0)
				return true;
		}
		return false;
	}
	
	/*
	 * Load an IdentTable from a file, if that file exists. Does nothing otherwise.
	 */
	public void loadIDTable(String fileName){
		String inputLine = null;
		String name = "";
		Expr idExpr;
		try{
			BufferedReader fin =  new BufferedReader(new FileReader(fileName));
			while((inputLine=fin.readLine()) != null){
				name = inputLine.substring(0, inputLine.indexOf("="));
				idExpr = new Expr(inputLine.substring(inputLine.indexOf("=")+1));
				add(name, idExpr);
			}
			fin.close();
		}catch(FileNotFoundException err){
			;
		}catch (IOException e){
			System.err.println ("Unable to read from file");
			System.exit(-1);
		}
	}
	/*
	 * Save a an IdentTable to a file, overwriting any file which exists with the same name.
	 */
	public void saveIDTable(String fileName){
		FileOutputStream fout;
		try{
			fout = new FileOutputStream(fileName);
			for(int i = 0; i<idents.size(); ++i){
				new PrintStream(fout).println(idents.get(i).toString());
			}
			fout.close();
			
		} catch (IOException e)
		{
			System.err.println ("Unable to write to file");
			System.exit(-1);
		}
	}
	
	/*
	 * Returns a list of Idents in this IdentTable so far.		
	 */
	public LinkedList<Ident> getIDList(){
		return idents;
	}
	
	/*
	 * Returns the Ident object matching the name in the argument. Returns null, if no ident
	 * is in the Table with a matching name.
	 */
	public Ident getIdent(String toFind){
		int pos = findIdent(toFind);
		if(pos >=0){
			return idents.get(pos);
		}
		return null;
	}
	
	/*
	 * Prints the idents in the table, useful for debug.
	 */
	public void print(){
		System.out.println("myIDTable");
		for(int i =0; i < idents.size(); ++i){
			System.out.println(idents.get(i).toString());
		}
		System.out.println("myIDTableEnd");
	}
}
