package visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public final class MethodDeclarationVisitor extends ASTVisitor {
  private final ArrayList<MethodDeclaration> methods = new ArrayList<>();

  public static ArrayList<MethodDeclaration> perform(ASTNode node) {
    MethodDeclarationVisitor finder = new MethodDeclarationVisitor();
    node.accept(finder);
    return finder.getMethods();
  }

  @Override
  public boolean visit(final MethodDeclaration method) {
    methods.add(method);
    return super.visit(method);
  }

  /**
   * @return an immutable list view of the methods discovered by this visitor
   */
  public ArrayList<MethodDeclaration> getMethods() {
    return methods;
  }
}