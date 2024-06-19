package org.kryun.symbol.javaparser.symbolsolver;

import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import java.util.Optional;

public final class TypeMapperManager {

    private final FullQualifiedNameMapper fullQualifiedNameMapper = new FullQualifiedNameMapper();

    public TypeMapperManager() {
    }

    // static으로 물고 있는 typeMapper clear;
    public boolean clearTypeMapper() {
        try {
            fullQualifiedNameMapper.clear();
            return true;
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return false;
        }
    }

    public Optional<FullQualifiedNameDTO> getFullQualifiedNameDTOFromTypeMapper(
            String fullQualifiedName) {
        return fullQualifiedNameMapper.getFullQualifiedNameDTO(fullQualifiedName);
    }

    public boolean registerFullQualifiedNameDTO(String fullQualifiedName,
                                                FullQualifiedNameDTO fullQualifiedNameDTO)
            throws Exception {
        return fullQualifiedNameMapper.registerFullQualifiedNameDTO(fullQualifiedName,
                fullQualifiedNameDTO);
    }

}
