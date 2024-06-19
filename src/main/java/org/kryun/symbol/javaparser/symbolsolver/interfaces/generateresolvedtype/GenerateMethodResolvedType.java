package org.kryun.symbol.javaparser.symbolsolver.interfaces.generateresolvedtype;

import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

public interface GenerateMethodResolvedType<T> extends GenerateResolvedType {

    public abstract ResolvedMethodDeclaration generateResolvedType(T resolvableNode)
            throws Exception;
}
