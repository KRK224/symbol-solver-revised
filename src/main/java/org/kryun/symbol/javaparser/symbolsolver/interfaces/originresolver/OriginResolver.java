package org.kryun.symbol.javaparser.symbolsolver.interfaces.originresolver;

import java.util.Optional;

/**
 * T: Javaparser의 Class or MethodDecl Node
 */
public interface OriginResolver<T> {

    /**
     * TypeMapperManager에 등록하기 위한 Key값을 생성해주는 함수
     *
     * @param originNode
     * @return
     * @throws Exception
     */
    public abstract Optional<String> getFullQualifiedName(T originNode) throws Exception;
}
