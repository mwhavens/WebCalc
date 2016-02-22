/**
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import ExpressionObject.*;
import ExpressionTokenizer.ExpressionTokenizer;
/**
 * @author Michael W. Havens
 *
 */
public class WebCalcServer extends Thread{
	private int clientNumber = 0;
	private int port = 8999;
	ExpressionCalculator ExprCalc;
	public WebCalcServer() {
		this(8999);
	}

	public WebCalcServer(int myport) {
		port = myport;
		ExprCalc = new ExpressionCalculator();
	}

	public void run(){
		System.out.println("WebCalc Server is Running!");
        try {
        	ServerSocket listener = new ServerSocket(port);
        	try {
        		while (true) {
					new Server_Session(listener.accept(), clientNumber++).start();
        		}
			} finally {
				listener.close();
			}
        } catch (IOException e) {
        	System.out.println("WebCalc Server: Open listener Failed - Port(" + port + ")!");
        }
	}
	
	class Server_Session extends Thread {
		private Socket socket;
        private int clientNumber;
	
        public Server_Session(Socket socket, int clientNumber) {
        	this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);
            
        }
        
        public void run() {
        	try {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Hello, you are client #" + clientNumber + ".");
                out.println("Enter Expression to Calc(\".\" exits): ");
                out.println("");
                double result;
                while (true) {
	                String input = in.readLine();
	                if (input == null || input.equals(".")) {
	                    break;
	                }
	                //System.out.println("Thread("+ clientNumber+ ") GOT: " +  input +"");
	                result = ExprCalc.calcExpr(input);
	                //System.out.println("Thread("+ clientNumber+ ") Result: " + result +"");
	                out.println(result);
	                out.println("");
                }
        	} catch (IOException e) {
        		System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                	System.out.println("Failed to close a socket");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        	
        }
        
	}
	
	class ExpressionCalculator {
		//Holds values of all the idents in this expression calculator.
		private IdentTable IDTable;
		
		public ExpressionCalculator(){
			IDTable = new IdentTable();
		}
		
		public ExpressionCalculator(IdentTable newIDTable){
			IDTable = newIDTable;
		}
		
		/*
		 * Given a String of an expression grammar, remembers assignments to idents
		 * and returns the value of the expression string.
		 * 
		 */
		public double calcExpr(String toCalc){
			ExpressionTokenizer tokens;
			tokens = new ExpressionTokenizer(toCalc);
			String name = "";
			if(tokens.getType().compareTo("Ident") ==0 && tokens.isNext()){
				name = tokens.getToken();
				tokens.nextToken();
				if(tokens.getType().compareTo("Equal") ==0){
					IDTable.add(name, new Expr(tokens.split(), IDTable));
					return IDTable.getIdentValue(name).getValue();
				} else {
					tokens.gotoFirstToken();
				}
			}
			return new Expr(tokens, IDTable).getValue();
		}
		
		public void setIDTable(IdentTable newIDTable){
			IDTable = newIDTable;
		}
		
		public IdentTable getIDTable(){
			return IDTable;
		}
		
		//returns the expr of a given Ident by its name.
		public String getIDExpr(String idName){
			return IDTable.getIdentValue(idName).toString();
		}
		
		//returns the value of an Ident held in the table
		public double getIDValue(String idName){
			return IDTable.getIdentValue(idName).getValue();
		}
		
		//File store/save methods
		public void loadIDTable(String fileName){
			IDTable.loadIDTable(fileName);
		}
		public void saveIDTable(String fileName){
			IDTable.saveIDTable(fileName);
		}
		
		public void clearIDTable(){
			IDTable = new IdentTable();
		}
		
		/*
		 * Function given an Ident name and expr String value, determines if
		 * an InvalidDependance will exist if the expr String value is assigned
		 * to the Ident.
		 */
		public boolean isInvalidDependance(String idName, String newExpr){
			return isCircularDependance(idName, newExpr);
		}
		
		public boolean isCircularDependance(String idName, String newExpr){
			String myDependancies = "";
			List<Ident> myNewDependanceList;
			Boolean rtnVal = new Boolean(false);
			
			//get the dependencies of what the new Ident will have
			Ident tempIdent = new Ident(idName);
			tempIdent.setEquation(newExpr);
			myNewDependanceList = tempIdent.getDependancies();
			for(Ident dependantceIdent : myNewDependanceList){
				myDependancies = myDependancies + " " + dependantceIdent.Name();
				myDependancies = myDependancies + " " + childDependance(
													rtnVal,
													dependantceIdent.Name());
			}
			//if the children had a circular dependency rtnVal will be true
			if(rtnVal.booleanValue())
				return true;
			//if cell is dependent on himself some how. name will be in dependency list
			return myDependancies.indexOf(idName) >= 0;
		}
		/*Obtains the idents dependencies and recursively finds the dependencies, dependencies 
		 * searches the results for itself, if it exists in its dependencies or
		 *  dependencies, dependencies sets isCircular to true. returns its dependencies and its
		 *  dependencies, dependencies.
		 */
		
		public String childDependance(Boolean isCircular, String idName){
			//if isCircular is true no need to do work.
			if(isCircular.booleanValue())
				return "";
			String myDependancies = "";
			List<Ident> myDependanciesList = IDTable.getIdent(idName).getDependancies();
			for(int i = 0; i < myDependanciesList.size(); ++i){
				//Concatenates idents dependencies and its dependencies dependencies
				myDependancies = myDependancies + " " + myDependanciesList.get(i).Name();
				myDependancies = myDependancies + " " + childDependance(
													isCircular,
													myDependanciesList.get(i).Name());
			}
			isCircular = new Boolean(isCircular.booleanValue() || myDependancies.indexOf(idName)>=0);
			return myDependancies;
		}
	}
}
