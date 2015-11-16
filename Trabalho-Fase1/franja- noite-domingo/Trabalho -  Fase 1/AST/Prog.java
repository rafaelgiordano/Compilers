
package AST;

//prog ::= P pid ’;’ body ’.’
public class Prog{

	private Pid pid_;
	private Body body_;
	
	public Program(Pid p, Body b){
		
		this.pid_ = p;
		this.body_ = b;
	}
	
	


}