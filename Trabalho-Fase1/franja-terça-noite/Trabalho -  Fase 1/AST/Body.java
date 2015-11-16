
package AST;

//body ::= [dclpart] compstmt

public class Body{

	private ArrayList<Dcl> dcl_;
	private CompStmt compstmt_;
	
	
	public Body(ArrayList<Dcl> d, CompStmt c){
		this.dcl_ = d;
		this.compstmt_ = c;
	}


}