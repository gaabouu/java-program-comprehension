package appInfo;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodInfo extends Info {
	public int nbLines;
    public int nbParameters;
    public String cls;
    public ArrayList<MethodInvocation> calledMethods = new ArrayList<>();

    public String toString() {
      String str = "\t\t\t\tMethod: " + this.name + ":\n";
      str += "\t\t\t\tlines: " + this.nbLines + "\n";
      str += "\t\t\t\tparameters: " + this.nbParameters + "\n";
      str += "\n";
      return str.substring(0, str.length() - 1);
    }

    public int getLines() {
      return nbLines;
    }
    
    public String getCls(){
    	return this.cls;
    }
}
