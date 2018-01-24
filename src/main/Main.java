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

        Scanner sc = new Scanner(System.in);
        System.out.println("\n################## GRAPHE DE COUPLAGE PONDERE #####################");
        Utils.drawPonderedGraph();

        System.out.println("\n################### GRAPHE DE CLUSTERING ########################");
        ArrayList<Cluster> clusters = Utils.clusteringHierarchique();
        for (Cluster cls : clusters) {
            System.out.println(cls);
        }

        System.out.println("\n################### COMPOSANTS EXTRAITS ########################");
        for (Cluster cls : Utils.selectionClusters(clusters.get(clusters.size() - 1))) {
            System.out.println(cls);
        }
    }
}
