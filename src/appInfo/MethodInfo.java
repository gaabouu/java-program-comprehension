package appInfo;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodInfo extends Info {
	public int nbLines;
    public int nbParameters;
    public String cls;
    public ArrayList<MethodInvocation> calledMethods = new ArrayList<>();
    public HashMap<String, String> variablesTypes = new HashMap<String, String>();
    public ArrayList<EnhancedForInfo> forLoops = new ArrayList<EnhancedForInfo>();

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
    
    public ArrayList<String> getVarsName(){
    	ArrayList<String> result = new ArrayList<String>();
    	for(String name: variablesTypes.keySet()){
    		result.add(name);
    	}
    	return result;
    }
    
    public ArrayList<String> getVarsNameByType(String type){
    	ArrayList<String> result = new ArrayList<String>();
    	//System.out.println("In getVarsNameByType");
    	//System.out.println(variablesTypes);
    	for(String name: variablesTypes.keySet()){
    		//System.out.println(variablesTypes.get(name));
    		if(variablesTypes.get(name).equals(type)){
    			result.add(name);
    		} else if(variablesTypes.get(name).contains("ArrayList<")){
    			String nameSubType = variablesTypes.get(name).substring(10);
    			nameSubType = nameSubType.substring(0, nameSubType.length() - 1);
    			//System.out.println("ICIIIIIIIIIIIII " + nameSubType);
    			if(nameSubType.equals(type))
    			result.add(nameSubType);
    		}
    	}
    	return result;
    }
    
    public ArrayList<EnhancedForInfo> getFors(){
    	return this.forLoops;
    }
    
    public void addCouple(String _name, String _type){
    	this.variablesTypes.put(_name, _type);
    }
}
