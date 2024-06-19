package org.kryun.symbol.javaparser.symbolsolver;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.resolution.types.ResolvedType;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.generateresolvedtype.GenerateClassResolvedType;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.referabletyperesolver.AbstractClassReferableTypeResolver;
import java.util.Optional;

public class ForwardParamTypeResolver implements GenerateClassResolvedType<Parameter> {

    private static class LazyHolder {

        private static final ForwardParamTypeResolver INSTANCE = new ForwardParamTypeResolver();
    }

    public static ForwardParamTypeResolver getInstance() {
        return LazyHolder.INSTANCE;
    }

    private AbstractClassReferableTypeResolver paramTypeResolverImpl;

    private ForwardParamTypeResolver() {
        this.paramTypeResolverImpl = new AbstractClassReferableTypeResolver();
    }

    @Override
    public ResolvedType generateResolvedType(Parameter parameter) throws Exception {
        try {
            return parameter.getType().resolve();
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<String> getFullQualifiedName(Parameter parameter) {
        try {
            return paramTypeResolverImpl.getFullQualifiedName(generateResolvedType(parameter));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
