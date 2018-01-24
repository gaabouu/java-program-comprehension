package main;

import appInfo.ClassInfo;

import java.util.ArrayList;

public class Cluster {
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
        this.name = cls1.name + " | " + cls2.name;
        this.metrik = Utils.getMetrik(filsG, filsD);
    }

    public String toString() {
        return this.name + " < " + this.filsG.name + " > " + filsD.name + " ====> Metrique = " + this.metrik;
    }
}
