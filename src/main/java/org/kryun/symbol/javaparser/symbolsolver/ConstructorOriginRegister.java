package org.kryun.symbol.javaparser.symbolsolver;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.originresolver.OriginResolver;
import java.util.Optional;

public class ConstructorOriginRegister implements OriginResolver<ConstructorDeclaration> {

    private static class LazyHolder {

        private static final ConstructorOriginRegister INSTANCE = new ConstructorOriginRegister();
    }

    public static ConstructorOriginRegister getInstance() {
        return LazyHolder.INSTANCE;
    }

    // 상속 방지
    private ConstructorOriginRegister() {
    }


    @Override
    public Optional<String> getFullQualifiedName(ConstructorDeclaration originNode)
            throws Exception {
        try {
            String fullQualifiedSignature = originNode.resolve().getQualifiedSignature();
            return Optional.of(fullQualifiedSignature);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
