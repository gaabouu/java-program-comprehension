package main;

import appInfo.ClassInfo;

import java.util.ArrayList;

public class Cluster {
    private static int counter = 0;

    public ArrayList<ClassInfo> classes = new ArrayList<ClassInfo>();
    public Cluster filsG;
    public Cluster filsD;
    public String name;
    public double metrik;

    public Cluster(ClassInfo cls) {
        this.classes.add(cls);
        this.filsG = null;
        this.filsD = null;
        this.name = cls.name;
    }

    public Cluster(Cluster cls1, Cluster cls2) {
        for (ClassInfo cls : cls1.classes) {
            this.classes.add(cls);
        }
        for (ClassInfo cls : cls2.classes) {
            this.classes.add(cls);
        }
        this.filsG = cls1;
        this.filsD = cls2;
        this.name = "C" + counter;
        counter++;
        this.metrik = Utils.getMetrik(filsG, filsD);
    }

    public String toString() {
        String str = this.name + "\n";
        str += "Liste des classe: ";
        for (ClassInfo cls : this.classes) {
            str += cls.name + " | ";
        }
        if (this.filsG != null && this.filsD != null) {
            str += "\n";
            str += "Fils Gauche: " + this.filsG.name + "\n";
            str += "Fils Droit: " + this.filsD.name + "\n";
            str += "Metrique: " + this.metrik;
        }
        str += "\n";
        return str;
    }
}
