package org.kryun.symbol.javaparser.symbolsolver;


import com.github.javaparser.ast.body.EnumDeclaration;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.originresolver.OriginResolver;
import java.util.Optional;

public class EnumOriginRegister implements OriginResolver<EnumDeclaration> {

    private static class LazyHolder {

        private static final EnumOriginRegister INSTANCE = new EnumOriginRegister();
    }

    public static EnumOriginRegister getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Optional<String> getFullQualifiedName(EnumDeclaration originNode)
            throws Exception {
        try {
            String fullQualifiedName = originNode.resolve().getQualifiedName();
            return Optional.of(fullQualifiedName);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
