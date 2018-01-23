package visitors;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;

public class EnhancedForStatementVisitor extends ASTVisitor {
	private final ArrayList<EnhancedForStatement> fors = new ArrayList<>();

	  public static ArrayList<EnhancedForStatement> perform(ASTNode node) {
	    EnhancedForStatementVisitor finder = new EnhancedForStatementVisitor();
	    node.accept(finder);
	    return finder.getFors();
	  }

	  @Override
	  public boolean visit(final EnhancedForStatement fore) {
	    fors.add(fore);
	    return super.visit(fore);
	  }

	  /**
	   * @return an immutable list view of the methods discovered by this visitor
	   */
	  public ArrayList<EnhancedForStatement> getFors() {
	    return fors;
	  }
}
