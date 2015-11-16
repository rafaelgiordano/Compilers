package AST;

// FUNCTION pid args ':' stdtype
public class Function extends Subhead {
	private String name;
	private Args args;
	private StdType type;
	
	public Function(String name, Args args, StdType type) {
		this.name = name;
		this.args = args;
		this.type = type;
	}
	
	
	public void genC(PW pw) {
		pw.print(type.getType() + " " + name);

		if (args != null) {
			args.genC(pw);
		}
	}

	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return type.getType();
	}

    
}
