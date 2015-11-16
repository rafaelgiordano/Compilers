
package AST;

//body ::= [dclpart] compstmt

public class Body{

	private Dcl dcl_;
	private CompStmt compstmt_;
	
	
	public Body(Dcl d, CompStmt c){
		this.dcl_ = d;
		this.compstmt_ = c;
	}


}