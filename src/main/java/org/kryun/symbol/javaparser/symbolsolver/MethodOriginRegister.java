package org.kryun.symbol.javaparser.symbolsolver;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.originresolver.OriginResolver;
import java.util.Optional;

public class MethodOriginRegister implements OriginResolver<MethodDeclaration> {

    private static class LazyHolder {

        private static final MethodOriginRegister INSTANCE = new MethodOriginRegister();
    }

    public static MethodOriginRegister getInstance() {
        return LazyHolder.INSTANCE;
    }

    // 상속 방지
    private MethodOriginRegister() {
    }

    @Override
    public Optional<String> getFullQualifiedName(MethodDeclaration originNode) throws Exception {
        try {
            String fullQualifiedSignature = originNode.resolve().getQualifiedSignature();
            return Optional.of(fullQualifiedSignature);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
