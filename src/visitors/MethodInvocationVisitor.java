package visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class MethodInvocationVisitor extends ASTVisitor {
	ArrayList<MethodInvocation> methods = new ArrayList<MethodInvocation>();
	ArrayList<SuperMethodInvocation> superMethods = new ArrayList<SuperMethodInvocation>();
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethods.add(node);
		return super.visit(node);
	}

	public static ArrayList<MethodInvocation> perform(ASTNode node) {
		MethodInvocationVisitor finder = new MethodInvocationVisitor();
		node.accept(finder);
		return finder.getMethods();
	}

	public ArrayList<MethodInvocation> getMethods() {
		return methods;
	}
	
	public ArrayList<SuperMethodInvocation> getSuperMethod() {
		return superMethods;
	}
}
