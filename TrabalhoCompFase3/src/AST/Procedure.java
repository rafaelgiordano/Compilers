package AST;

// PROCEDURE pid args
public class Procedure extends Subhead {
	private String name;
	private Args args;
        private StdType type;

	public Procedure(String name, Args args, StdType type) {
		this.name = name;
		this.args = args;
	}

	@Override
	public void genC(PW pw) {
		pw.print("void " + name);

		if (args != null) {
			args.genC(pw);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}
}
