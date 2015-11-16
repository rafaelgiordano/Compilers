package AST;

// subhead ::= FUNCTION pid args ':' stdtype | PROCEDURE pid args
public abstract class Subhead {
	public abstract void genC(PW pw);
	public abstract String getName();
}
