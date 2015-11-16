/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author home
 */
public class DclPart {
    private DclList dcls;
    private SubDcl subdcl;
    
    public DclPart(DclList dcls){
        this.dcls = dcls;
    }
    
    
    public void genC(PW pw){
        dcls.genC(pw);
    }
    
    public boolean hasFunction() {
        if(this.subdcl == null)
            return false;
        else
            return true;
    }
}
