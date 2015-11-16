package AST;

import java.util.*;

public class SubDclList {
	private ArrayList<SubDcl> subdclList;
	
	public SubDclList(ArrayList<SubDcl> subdclList) {
		this.subdclList = subdclList;
	}

	public void genC(PW pw) {
		for (SubDcl v:subdclList) {
			v.genC(pw);
		}
	}
}
