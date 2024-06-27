package org.kryun.symbol.javaparser.management;

import org.kryun.symbol.model.dto.SymbolReferenceDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kryun.symbol.pkg.IdentifierGenerator;

public class SymbolReferenceManager {

    private final List<SymbolReferenceDTO> symbolReferencdDTOList;
    private final IdentifierGenerator symbolReferenceIdGenerator = new IdentifierGenerator("symbol_reference");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new HashMap<>();
        identifierMap.put(symbolReferenceIdGenerator.getSymbolType(), symbolReferenceIdGenerator.getLastId());
        return identifierMap;
    }

    public SymbolReferenceManager() {
        this.symbolReferencdDTOList = new ArrayList<>();
    }

    public List<SymbolReferenceDTO> getSymbolReferenceDTOList() {
        return this.symbolReferencdDTOList;
    }

    public void clear() {
        this.symbolReferencdDTOList.clear();
    }

    public SymbolReferenceDTO buildSymbolReference(Long symbolStatusId, String src_path) {
        SymbolReferenceDTO symbolReferenceDTO = new SymbolReferenceDTO();

        symbolReferenceDTO.setSymbolReferenceId(symbolReferenceIdGenerator.nextId());
        symbolReferenceDTO.setSymbolStatusId(symbolStatusId);
        symbolReferenceDTO.setSrcPath(src_path);

        symbolReferencdDTOList.add(symbolReferenceDTO);

        return symbolReferenceDTO;
    }

}
