package org.kryun.symbol.javaparser.symbolsolver.interfaces.referabletyperesolver;

import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import java.util.Optional;

/**
 * method 추상 골격 클래스
 */
public class AbstractMethodReferableTypeResolver
        implements ReferableTypeResolver<ResolvedMethodDeclaration> {

    @Override
    public Optional<String> getFullQualifiedName(ResolvedMethodDeclaration resolvedType) {
        try {
            String mdQualifiedSignature = resolvedType.getQualifiedSignature();
            return Optional.of(mdQualifiedSignature);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

}
