package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import appInfo.AppInfo;
import appInfo.ClassInfo;
import appInfo.FileInfo;
import appInfo.Info;
import appInfo.MethodInfo;
import appInfo.PackageInfo;
import visitors.MethodInvocationVisitor;
import visitors.TypeDeclarationVisitor;

public class Utils {
	protected static File root;
	protected static String projectSourcePath;
	protected static String jrePath;
	
	protected static AppInfo app;
	
	public static Info getInfo(File file) throws IOException {

	    if (file.getName().equals("src")) {
	      AppInfo info = new AppInfo();
	      info.name = "app";
	      for (File fileEntry : file.listFiles()) {
	        info.packages.add((PackageInfo) getInfo(fileEntry));
	      }
	      return info;

	    } else if (file.isDirectory()) {
	      PackageInfo info = new PackageInfo();
	      info.name = file.getName();
	      for (File fileEntry : file.listFiles()) {
	        info.files.add((FileInfo) getInfo(fileEntry));
	      }
	      return info;

	    } else if (file.getName().endsWith(".java")) {
	      FileInfo info = new FileInfo();
	      String content = FileUtils.readFileToString(file);
	      CompilationUnit parser = parse(content.toCharArray());
	      info.nbLines = parser.getLineNumber(parser.getLength() - 1);
	      for (TypeDeclaration type : TypeDeclarationVisitor.perform(parser)) {
	        ClassInfo clsInfo = new ClassInfo();
	        if (!(type.isInterface())) {
	          clsInfo.name = type.getName().toString();
	          clsInfo.nbLines = type.toString().split("\n").length;
	          for (MethodDeclaration meth : type.getMethods()) {
	            MethodInfo methodInfo = new MethodInfo();
	            methodInfo.name = meth.getName().toString();
	            methodInfo.nbParameters = meth.parameters().size();
	            methodInfo.nbLines = meth.getBody().toString().split("\n").length;
	            for(MethodInvocation inv: MethodInvocationVisitor.perform(parser)) {
	              //System.out.println(inv);
	              methodInfo.calledMethods.add(inv.getName().toString());
	            }
	            clsInfo.methods.add(methodInfo);
	          }
	          clsInfo.nbFields = type.getFields().length;
	        }
	        info.classes.add(clsInfo);
	      }
	      return info;
	    }
	    return null;
	  }

	  // create AST
	  private static CompilationUnit parse(char[] classSource) {
	    ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
	    parser.setResolveBindings(true);
	    parser.setKind(ASTParser.K_COMPILATION_UNIT);
	    parser.setBindingsRecovery(true);
	    Map options = JavaCore.getOptions();
	    parser.setCompilerOptions(options);
	    parser.setUnitName("");
	    String[] sources = {projectSourcePath};
	    String[] classpath = {jrePath};
	    parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
	    parser.setSource(classSource);
	    return (CompilationUnit) parser.createAST(null); // create and parse
	  }

	  

	  public static ArrayList<ClassInfo> sortClassesByMethodNumber(ArrayList<ClassInfo> classes) {

	    for (int i = 0; i < classes.size() - 1; i++) {
	      for (int j = i + 1; j < classes.size(); j++) {
	        if (classes.get(i).getNbMethods() < classes.get(j).getNbMethods()) {
	          ClassInfo t = classes.get(i);
	          classes.set(i, classes.get(j));
	          classes.set(j, t);
	        }
	      }
	    }
	    return classes;
	  }

	  public static ArrayList<ClassInfo> sortClassesByFieldsNumber(ArrayList<ClassInfo> classes) {

	    for (int i = 0; i < classes.size() - 1; i++) {
	      for (int j = i + 1; j < classes.size(); j++) {
	        if (classes.get(i).getNbFields() < classes.get(j).getNbFields()) {
	          ClassInfo t = classes.get(i);
	          classes.set(i, classes.get(j));
	          classes.set(j, t);
	        }
	      }
	    }
	    return classes;
	  }

	  public static ArrayList<MethodInfo> sortMethodsByParameterNumber(ArrayList<MethodInfo> methods) {
	    for (int i = 0; i < methods.size() - 1; i++) {
	      for (int j = i + 1; j < methods.size(); j++) {
	        if (methods.get(i).nbParameters < methods.get(j).nbParameters) {
	          MethodInfo t = methods.get(i);
	          methods.set(i, methods.get(j));
	          methods.set(j, t);
	        }
	      }
	    }
	    return methods;
	  }

	  
	  
	  /*
	  public static void tenPercentClassGreaterMethodNumber(){
		double percent = (double) app.getNbClasses() * 0.1;
	    int i = 0;
	    ArrayList<ClassInfo> classesOrderedByMethods = sortClassesByMethodNumber(app.getClasses());
	    ArrayList<ClassInfo> topClassesByMethods = new ArrayList<>();
	    while (i < percent - 1) {
	      topClassesByMethods.add(classesOrderedByMethods.get(i));
	      i++;
	    }
	    System.out.print("10% de classes avec le plus de méthodes: ");
	    for (ClassInfo cls : topClassesByMethods) {
	      System.out.print(cls.name + " | ");
	    }
	    System.out.println();
	  }
	  
	  public static void tenPercentClassGreaterAttributeNumber(){
		int i = 0;
		double percent = (double) app.getNbClasses() * 0.1;
	    ArrayList<ClassInfo> classesOrderedByFields = Utils.sortClassesByFieldsNumber(app.getClasses());
	    ArrayList<ClassInfo> topClassesByFields = new ArrayList<>();
	    while (i < percent - 1) {
	      topClassesByFields.add(classesOrderedByFields.get(i));
	      i++;
	    }
	    System.out.print("10% de classes avec le plus d'attributs: ");
	    for (ClassInfo cls : topClassesByFields) {
	      System.out.print(cls.name + " | ");
	    }
	    System.out.println();
	  }*/
}
