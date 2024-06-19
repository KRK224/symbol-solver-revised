package org.kryun.symbol.javaparser.symbolsolver;


import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.types.ResolvedType;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.generateresolvedtype.GenerateClassResolvedType;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.referabletyperesolver.AbstractClassReferableTypeResolver;
import java.util.Optional;


public class ForwardNameExprTypeResolver implements GenerateClassResolvedType<Expression> {

    private static class LazyHolder {

        private static final ForwardNameExprTypeResolver INSTANCE = new ForwardNameExprTypeResolver();
    }

    public static ForwardNameExprTypeResolver getInstance() {
        return LazyHolder.INSTANCE;
    }

    private final AbstractClassReferableTypeResolver nameExprTypeResolverImpl = new AbstractClassReferableTypeResolver();


    @Override
    public ResolvedType generateResolvedType(Expression nameExpr) throws Exception {
        return nameExpr.calculateResolvedType();
    }

    public Optional<String> getFullQualifiedName(Expression nameExpr) {
        try {
            return nameExprTypeResolverImpl.getFullQualifiedName(
                    generateResolvedType(nameExpr));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
