package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.sun.jmx.remote.internal.ArrayQueue;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import appInfo.AppInfo;
import appInfo.ClassInfo;
import appInfo.EnhancedForInfo;
import appInfo.FileInfo;
import appInfo.Info;
import appInfo.MethodInfo;
import appInfo.MethodInvocationInfo;
import appInfo.PackageInfo;
import visitors.EnhancedForStatementVisitor;
import visitors.ForStatementVisitor;
import visitors.MethodInvocationVisitor;
import visitors.TypeDeclarationVisitor;
import visitors.VariableDeclarationFragmentVisitor;

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
                    for (FieldDeclaration field : type.getFields()) {

                        Object o = field.fragments().get(0);
                        String name = null;
                        if (o instanceof VariableDeclarationFragment) {
                            String s = ((VariableDeclarationFragment) o).getName().toString();
                            name = s;
                            //System.out.println("-------------field: " + s);
                        }


                        String typeField = field.getType().toString();
                        //  System.out.println("UN CHAMP DE LA CLASSE " + type.getName().toString() + ": " + name + ": " + typeField);
                        clsInfo.fields.put(name, typeField);
                    }
                    for (MethodDeclaration meth : type.getMethods()) {


                        MethodInfo methodInfo = new MethodInfo();
                        methodInfo.name = meth.getName().toString();
                        methodInfo.cls = type.getName().toString();
                        methodInfo.nbParameters = meth.parameters().size();
                        methodInfo.nbLines = meth.getBody().toString().split("\n").length;

                        //System.out.println("for loops of "+ meth.getName().toString());

                        for (EnhancedForStatement f : EnhancedForStatementVisitor.perform(meth)) {
                            EnhancedForInfo forInfo = new EnhancedForInfo();
                            //System.out.println(f.getParameter().toString());
                            forInfo.meth = meth.getName().toString();
                            String var = f.getParameter().toString().split(" ")[1];
                            String varType = f.getParameter().toString().split(" ")[0];
                            //System.out.println("Variable declarations in this block: " + var + " of type " + varType);
                            methodInfo.variablesTypes.put(var, varType);


                            //methodInfo.forLoops.add(f);
                        }

                        for (MethodInvocation inv : MethodInvocationVisitor.perform(meth)) {

                            //System.out.println(meth.getName().toString() + "   " + inv.toString());
                            methodInfo.calledMethods.add(inv);
                        }
                        // System.out.println("   Les variables de la m�thode: " + meth.getName());
                        for (VariableDeclarationFragment var : VariableDeclarationFragmentVisitor.perform(meth)) {

                            //System.out.println("       " + var.getParent());
                            String name = var.getName().toString();
                            String typeSimpleName = null;
                            if (var.getParent() instanceof FieldDeclaration) {
                                FieldDeclaration decla = ((FieldDeclaration) var.getParent());

                                typeSimpleName = decla.getType().toString();
                                //System.out.println("UN CHAMP DE LA CLASSE: " + name + ": " + typeSimpleName);

                            } else if (var.getParent() instanceof VariableDeclarationStatement) {
                                VariableDeclarationStatement decla = ((VariableDeclarationStatement) var.getParent());

                                typeSimpleName = decla.getType().toString();
                                methodInfo.addCouple(name, typeSimpleName);
                                //System.out.println("UNE VARIABLE: " + name + ": " + typeSimpleName);

                            }
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

    public static double getMetrik(Cluster clsA, Cluster clsB) {
        double metrik = 0;
        for (ClassInfo cls1 : clsA.classes) {
            for (ClassInfo cls2 : clsB.classes) {
                metrik += getMetrik(cls1.name, cls2.name);
            }
        }
        return metrik;
    }

    public static double getMetrik(String clsA, String clsB) {
        double result = 0;

        AppInfo appInfo = Utils.app;
        ArrayList<MethodInfo> methsA = null;
        ArrayList<MethodInfo> methsB = null;

        ArrayList<String> fieldsAOfTypeB = getFieldsAToB(clsA, clsB);
        ArrayList<String> fieldsBOfTypeA = getFieldsAToB(clsB, clsA);

        int sub = 0;

        // System.out.println("Calcul du couplage entre " + clsA + " et " + clsB + "...");
        for (PackageInfo pck : appInfo.packages) {
            for (FileInfo file : pck.files) {
                for (ClassInfo cls : file.classes) {
                    if (cls.name.equals(clsA)) {
                        methsA = new ArrayList<MethodInfo>();
                        methsA.addAll(cls.methods);
                    } else if (cls.name.equals(clsB)) {
                        methsB = new ArrayList<MethodInfo>();
                        methsB.addAll(cls.methods);
                    }
                }
            }
        }
        if (methsA == null || methsB == null) {
            System.err.println("Une des classes n'existe pas");
            return -1;
        }

        //System.out.println("\nM�thodes appel�es par " + clsA);
        for (MethodInfo meth : methsA) {
            ArrayList<String> varsAToB = new ArrayList<String>();
            varsAToB.addAll(meth.getVarsNameByType(clsB));
            //System.out.println("Variables de " + meth.name + " de type " + clsB);
            //System.out.println(varsAToB);
            //System.out.println("et champs: \n" + fieldsAOfTypeB);
            for (MethodInvocation methInv : meth.calledMethods) {
                // if(methInv.toString().matches(".")){
                // System.out.println(methInv.toString());
                String var = methInv.toString();

                String[] parts = var.split("\\.");
                var = parts[0];
                // System.out.println(meth.name + ": " + methInv.toString() + " -> " + var + " =? " + clsB);
                if (var.equals(clsB)) sub += 1;
                else {
                    for (String varAB : varsAToB) {
                        if (var.equals(varAB)) sub += 1;

                    }
                    for (String fieldAB : fieldsAOfTypeB) {
                        if (var.equals(fieldAB)) sub += 1;
                    }
                }

                //System.out.println(var);
                // }else{
                //System.out.println(methInv.toString());
                // }
            }
        }
        //System.out.println("\nM�thodes appel�es par " + clsB);

        for (MethodInfo meth : methsB) {
            ArrayList<String> varsBToA = new ArrayList<String>();
            varsBToA.addAll(meth.getVarsNameByType(clsA));
            //System.out.println("Variables de " + meth.name + " de type " + clsA);
            //System.out.println(varsBToA);
            //System.out.println("et champs: \n" + fieldsBOfTypeA);
            for (MethodInvocation methInv : meth.calledMethods) {
                String var = methInv.toString();
                String[] parts = var.split("\\.");
                var = parts[0];
                if (var.equals(clsA)) sub += 1;
                else {
                    for (String varBA : varsBToA) {
                        if (var.equals(varBA)) sub += 1;

                    }
                    for (String fieldBA : fieldsBOfTypeA) {
                        if (var.equals(fieldBA)) sub += 1;
                    }
                }

                //System.out.println(var);
                //System.out.println(methInv.toString());
            }
        }

        // System.out.println("SUB = " + sub);

        int div = 0;
        for (PackageInfo pck : appInfo.packages) {
            for (FileInfo file : pck.files) {
                for (ClassInfo cls : file.classes) {
                    if (cls.name.equals(clsA) || cls.name.equals(clsB)) {

                    }
                    for (MethodInfo meth : cls.methods) {
                        //System.out.println(meth);
                        // System.out.println("  " + meth.getCls());

                        ArrayList<String> vars = new ArrayList<String>();

                        for (String var : meth.variablesTypes.keySet()) {
                            for (ClassInfo cl : appInfo.getClasses()) {
                                if (meth.variablesTypes.get(var).equals(cl.name)) {
                                    vars.add(var);

                                }
                            }

                        }
                        for (MethodInvocation calledMeth : meth.calledMethods) {
                            //System.out.println("     " + calledMeth.toString());
                            String call = calledMeth.toString();
                            String[] rec = call.split("\\.");
                            //System.out.println(rec);
                            if (rec.length <= 1 || rec[0].equals("this")) ;
                            else {
                                if (vars.contains(rec[0])) div += 1;
                            }

                        }
                    }
                }

            }
        }

        //System.out.println("DIV = " + div);

        result = (double) sub / (double) div;
        //System.out.println(result);
        return result;
    }

    public static ArrayList<String> getFieldsAToB(String clsA, String clsB) {
        ArrayList<String> fields = new ArrayList<String>();

        AppInfo appInfo = Utils.app;

        for (PackageInfo pck : appInfo.packages) {
            for (FileInfo file : pck.files) {
                for (ClassInfo cls : file.classes) {
                    if (cls.name.equals(clsA)) {
                        fields.addAll(cls.getFieldsNameByType(clsB));

                    }
                }
            }
        }


        return fields;
    }

    public static void drawPonderedGraph() {
        ArrayList<ClassInfo> clss = app.getClasses();
        //System.out.println(clss.size());
        for (int i = 0; i < clss.size() - 1; i++) {
            for (int j = i + 1; j < clss.size(); j++) {
                double met = getMetrik(clss.get(i).name, clss.get(j).name);
                if (met != 0) {
                    System.out.println(clss.get(i).name + " | " + clss.get(j).name + " -> " + met);
                }
            }

        }
    }

    public static ArrayList<Cluster> clusteringHierarchique() {
        ArrayList<Cluster> result = new ArrayList<Cluster>();
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        ArrayList<ClassInfo> clss = app.getClasses();

        for (ClassInfo cls : clss) {
            clusters.add(new Cluster(cls));
        }

        while (clusters.size() > 1) {
            double max = 0;
            Cluster clsA = clusters.get(0);
            Cluster clsB = clusters.get(1);

            for (int i = 0; i < clusters.size() - 1; i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double metrik = getMetrik(clusters.get(i), clusters.get(j));
                    if (metrik > max) {
                        max = metrik;
                        clsA = clusters.get(i);
                        clsB = clusters.get(j);
                    }
                }
            }

            Cluster newCls = new Cluster(clsA, clsB);
            result.add(newCls);
            clusters.remove(clsA);
            clusters.remove(clsB);
            clusters.add(newCls);
        }
        return result;
    }

    public static ArrayList<Cluster> selectionClusters(Cluster cluster) {
        Stack<Cluster> composants = new Stack<>();
        ArrayList<Cluster> result = new ArrayList<>();
        composants.push(cluster);

        while (!(composants.empty())) {
            Cluster pere = composants.pop();
            Cluster filsG = pere.filsG;
            Cluster filsD = pere.filsD;
            if (filsG == null || filsD == null) {
                result.add(pere);
            } else if (pere.metrik > (filsG.metrik + filsD.metrik) / 2.) {
                result.add(pere);
            } else {
                composants.push(filsG);
                composants.push(filsD);
            }
        }
        return result;
    }
}
