package AST;

import java.util.*;
import java.io.*;

public class Program {
    
    public Program( ArrayList<Subroutine> procfuncList ) {
        this.procfuncList = procfuncList;
    }
    
    public void genC( PW pw) {
        
        pw.out.println("#include <stdio.h>");
        pw.out.println("");
        
          // generate code for procedures and functions
        for( Subroutine s : procfuncList ) {
            s.genC(pw);
            pw.out.println("");
            pw.out.println("");
        }
          
    }                             
        
    private ArrayList<Subroutine> procfuncList;
}