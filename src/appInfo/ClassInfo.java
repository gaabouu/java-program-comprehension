package appInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassInfo extends Info {
	public ArrayList<MethodInfo> methods = new ArrayList<>();
	public HashMap<String, String> fields = new HashMap<String, String>();
    public int nbLines;
    public int nbFields;

    public String toString() {
      String str = "\t\t\tClass: " + this.name + ":\n";
      str += "\t\t\tlines: " + this.nbLines + "\n";
      for (MethodInfo method : methods) {
        str += method.toString() + "\n";
      }
      str += "\n";
      return str.substring(0, str.length() - 1);
    }

    public int getLines() {
      return nbLines;
    }

    public int getNbMethods() {
      return methods.size();
    }

    public int getNbFields() {
      return nbFields;
    }
    
    public ArrayList<String> getFieldsName(){
    	ArrayList<String> result = new ArrayList<String>();
    	for(String name: fields.keySet()){
    		result.add(name);
    	}
    	return result;
    }
    
    public ArrayList<String> getFieldsNameByType(String type){
    	ArrayList<String> result = new ArrayList<String>();
    	//System.out.println("In getFieldsNameByType");
    	for(String name: fields.keySet()){
    		if(fields.get(name).equals(type)){
    			result.add(name);
    		} else if(fields.get(name).contains("ArrayList<")){
    			String nameSubType = fields.get(name).substring(10);
    			nameSubType = nameSubType.substring(0, nameSubType.length() - 1);
    			//System.out.println("ICIIIIIIIIIIIII " + nameSubType);
    			if(nameSubType.equals(type))
    			result.add(name);
    		}
    	}
    	return result;
    }

    public ArrayList<MethodInfo> getMethods() {
      return methods;
    }
}
