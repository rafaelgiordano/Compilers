package AST;

// pid '(' [exprlist] ')'
public class ProcedureCall extends ProcFunction {
	private Procedure procedure;
	private ExprList exprList;
	
	public ProcedureCall(Procedure procedure, ExprList exprList) {
		this.procedure = procedure;
		this.exprList = exprList;
	}
	
	@Override
	public void genC(PW pw) {
		System.out.print(procedure.getName() + "(");
		
		if (exprList != null) {
			exprList.genC(pw);
		}
		System.out.print(")");
	}
}
