package visitors;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class VariableDeclarationFragmentVisitor extends ASTVisitor {
	private ArrayList<VariableDeclarationFragment> variables = new ArrayList<VariableDeclarationFragment>();
	
	public static ArrayList<VariableDeclarationFragment> perform(ASTNode node) {
	    VariableDeclarationFragmentVisitor finder = new VariableDeclarationFragmentVisitor();
	    node.accept(finder);
	    return finder.getVariables();
	  }
	
	public boolean visit(VariableDeclarationFragment node) {
		variables.add(node);
		return super.visit(node);
	}
	
	public ArrayList<VariableDeclarationFragment> getVariables() {
		return variables;
	}
}
