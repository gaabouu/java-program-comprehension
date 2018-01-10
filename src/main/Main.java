package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import org.w3c.dom.Document;
import visitors.*;
import appInfo.*;

public class Main {
	
	public static final String projectPath = "./";
	public static final String projectSourcePath = projectPath + "./src";
	public static final String jrePath = "/usr/lib/jvm/oracle-java8-jre-amd64/lib/rt.jar";
	
	public static AppInfo app;


	public static void main(String[] args){
		System.out.println("test");
	}
}
