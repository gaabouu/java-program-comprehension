package main;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import org.w3c.dom.Document;
import visitors.*;
import appInfo.*;
import draw.*;

public class Main {

    public static final String projectPath = "./";
    public static final String projectSourcePath = projectPath + "./src";
    public static final String jrePath = "/usr/lib/jvm/oracle-java8-jre-amd64/lib/rt.jar";

    public static AppInfo app;

    public static void main(String[] args) throws IOException {

        // read java files
        final File root = new File(projectSourcePath);

        //print nbr of class, nbr of line, nbr of methods for the app
        // app = (AppInfo) getInfo(root);

        Utils.root = root;
        Utils.projectSourcePath = projectSourcePath;
        Utils.jrePath = jrePath;

        app = (AppInfo) Utils.getInfo(Utils.root);

        Utils.app = app;

        double met;
        boolean stop = false;
        Scanner sc = new Scanner(System.in);
        System.out.println("################## GRAPHE DE COUPLAGE PONDERE #####################");
        Utils.drawPonderedGraph();
        System.out.println("################### GRAPHE DE CLUSTERING ########################");
        for (Cluster cls : Utils.clusteringHierarchique()) {
            System.out.println(cls);
        }
        //PonderedGraphPanel pondGraph = new PonderedGraphPanel(app);
        //pondGraph.createAndShowGui();
        //while (!stop) {

            /*System.out.println("Choisissez les classes dont vous souhaitez connaitre le couplage: Classe A");
            String classA = sc.nextLine();
            System.out.println("Classe B");
            String classB = sc.nextLine();
*/
        //met = Utils.getMetrik(classA, classB);


        //if(!(met == -1)) stop = true;
        // }

	    
/*
	    //1. Nombre de classes de l’application.
	    System.out.println("Nombre de classes: " + app.getNbClasses());

	    //2. Nombre de lignes de code de l’application.
	    System.out.println("Nombre total de lignes de code: " + app.getLines());

	    //3. Nombre total de méthodes de l’application.
	    System.out.println("Nombre de méthodes: " + app.getNbMethods());

	    //4. Nombre total de packages de l’application.
	    System.out.println("Nombre de packages: " + app.packages.size());

	    //5. Nombre moyen de méthodes par classe.
	    System.out.println("Nombre moyen de méthodes par classe: " + ((float) app.getNbMethods()) / app.getNbClasses());

	    //6. Nombre moyen de lignes de code par méthode.
	    System.out.println("Nombre moyen de lignes de code par méthode: " + ((float) app.getLines()) / app.getNbMethods());

	    //7. Nombre moyen d’attributs par classe.
	    System.out.println("Nombre moyen d'attributs par classe: " + ((float) app.getNbFields()) / app.getNbClasses());

	    //8. Les 10% des classes qui possèdent le plus grand nombre de méthodes.
	    Utils.tenPercentClassGreaterMethodNumber();
	    

	    //9. Les 10% des classes qui possèdent le plus grand nombre d’attributs.
	    Utils.tenPercentClassGreaterAttributeNumber();

	    //10. Les classes qui font partie en même temps des deux catégories précédentes.
	    ArrayList<ClassInfo> winners = new ArrayList<>();
	    for (ClassInfo winner : app.getClasses()) {
	      if (topClassesByFields.contains(winner) && topClassesByMethods.contains(winner)) {
	        winners.add(winner);
	      }
	    }
	    System.out.print("Classes qui appartiennent aux deux catégories: ");
	    for (ClassInfo cls : winners) {
	      System.out.print(cls.name + " | ");
	    }
	    System.out.println();

	    //11. Les classes qui possèdent plus de X méthodes (la valeur de X est donnée).
	    System.out.print("Classes avec plus de 3 méthodes: ");
	    for (ClassInfo cls : app.getClasses()) {
	      System.out.print(cls.methods.size() > 3 ? cls.name + " | " : "");
	    }
	    System.out.println();

	    //12. Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe).
	    i = 0;
	    percent = (double) app.getNbMethods() * 0.1;
	    ArrayList<MethodInfo> methodsOrderedByParameterNumber = Utils.sortMethodsByParameterNumber(app.getMethods());
	    ArrayList<MethodInfo> topMethodsByParameters = new ArrayList<>();
	    while (i < percent - 1) {
	      topMethodsByParameters.add(methodsOrderedByParameterNumber.get(i));
	      i++;
	    }
	    System.out.print("10% de methodes avec le plus de paramètres: ");
	    for (MethodInfo meth : topMethodsByParameters) {
	      System.out.print(meth.name + " | ");
	    }
	    System.out.println();

	    //13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l’application.
	    int max = 0;
	    for (MethodInfo meth : app.getMethods()) {
	      if (meth.nbParameters > max) {
	        max = meth.nbParameters;
	      }
	    }
	    System.out.print("Nombre maximal de paramètres dans une méthode: " + max + "\n\n");

	    while (true) {
	      System.out.println("Tapez le nom d'une classe à analyser");
	      Scanner sc = new Scanner(System.in);
	      String classToSearch = sc.nextLine();
	      for (ClassInfo cls : app.getClasses()) {
	        if (cls.name.equals(classToSearch)) {
	          System.out.println("Methodes dans la class " + classToSearch);
	          for (MethodInfo meth : cls.getMethods()) {
	            System.out.println(meth.name);
	          }
	        }
	      }
	      System.out.println();
	      System.out.println("Tapez le nom d'une méthode à analyser");
	      String methodToSearch = sc.nextLine();
	      for (MethodInfo meth : app.getMethods()) {
	        if (meth.name.equals(methodToSearch)) {
	          System.out.println("Methodes appelées par la méthode " + meth.name);
	          for (String called : meth.calledMethods) {
	            System.out.println(called);
	          }
	        }
	      }
	    }
	  }*/


    }
}
