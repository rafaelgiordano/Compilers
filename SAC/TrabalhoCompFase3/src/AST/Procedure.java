package AST;

// PROCEDURE pid args
public class Procedure extends Subhead {
	private String name;
	private Args args;

	public Procedure(String name, Args args) {
		this.name = name;
		this.args = args;
	}

	
	public void genC(PW pw) {
		System.out.print("void " + name);

		if (args != null) {
			args.genC(pw);
		}
	}

	
	public String getName() {
		return this.name;
	}
}
