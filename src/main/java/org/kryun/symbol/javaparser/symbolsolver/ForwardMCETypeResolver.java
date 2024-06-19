package org.kryun.symbol.javaparser.symbolsolver;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.generateresolvedtype.GenerateMethodResolvedType;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.referabletyperesolver.AbstractMethodReferableTypeResolver;
import java.util.Optional;

/**
 * MethodCallExpr 처리에 대한
 */
public class ForwardMCETypeResolver
        implements GenerateMethodResolvedType<MethodCallExpr> {

    private static class LazyHolder {

        private static final ForwardMCETypeResolver INSTANCE = new ForwardMCETypeResolver();
    }

    // singleton
    public static ForwardMCETypeResolver getInstance() {
        return LazyHolder.INSTANCE;
    }

    private AbstractMethodReferableTypeResolver methodTypeResolverImpl;

    private ForwardMCETypeResolver() {
        this.methodTypeResolverImpl = new AbstractMethodReferableTypeResolver();
    }

    @Override
    public final ResolvedMethodDeclaration generateResolvedType(MethodCallExpr mce)
            throws Exception {
        try {
            return mce.resolve();
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<String> getFullQualifiedName(MethodCallExpr mce) {
        try {
            return methodTypeResolverImpl.getFullQualifiedName(generateResolvedType(mce));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
