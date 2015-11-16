package AST;

// subdcl ::= subhead ';' body ';'
public class SubDcl {
	private Subhead subhead;
	private Body body;
	
	public SubDcl(Subhead subhead, Body body) {
		this.subhead = subhead;
		this.body = body;
	}
	
	public void genC(PW pw) {
		subhead.genC(pw);
		body.genC(pw);
		
		if (!body.hasReturn()) {
			System.out.println("}\n");
		}
	}
}
