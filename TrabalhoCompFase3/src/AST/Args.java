package AST;

// args ::= '(' dcls ')' 
public class Args{
	private DclList dcls;
	
	public Args(DclList dcls) {
            this.dcls = dcls;
	}
	
	public void genC(PW pw) {
		pw.print("(");
		if (dcls != null) {
			dcls.genC(pw);
		}
		pw.println(") {");
	}
}
