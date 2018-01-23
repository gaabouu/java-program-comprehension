package visitors;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ForStatementVisitor extends ASTVisitor {
	private final ArrayList<ForStatement> fors = new ArrayList<>();

	  public static ArrayList<ForStatement> perform(ASTNode node) {
	    ForStatementVisitor finder = new ForStatementVisitor();
	    node.accept(finder);
	    return finder.getFors();
	  }

	  @Override
	  public boolean visit(final ForStatement fore) {
	    fors.add(fore);
	    return super.visit(fore);
	  }

	  /**
	   * @return an immutable list view of the methods discovered by this visitor
	   */
	  public ArrayList<ForStatement> getFors() {
	    return fors;
	  }

}
