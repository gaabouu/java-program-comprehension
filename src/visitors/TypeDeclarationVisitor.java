package visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationVisitor extends ASTVisitor {
  ArrayList<TypeDeclaration> types = new ArrayList<TypeDeclaration>();

  public static ArrayList<TypeDeclaration> perform(ASTNode node) {
    TypeDeclarationVisitor finder = new TypeDeclarationVisitor();
    node.accept(finder);
    return finder.getTypes();
  }

  @Override
  public boolean visit(final TypeDeclaration method) {
    types.add(method);
    return super.visit(method);
  }

  /**
   * @return an immutable list view of the methods discovered by this visitor
   */
  public ArrayList<TypeDeclaration> getTypes() {
    return types;
  }
}
