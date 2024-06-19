package org.kryun.symbol.javaparser.management;

import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullQualifiedNameManager {

    private final List<FullQualifiedNameDTO> fullQualifiedNameDTOList;
    private final IdentifierGenerator fullQualifiedNameIdGenerator = new IdentifierGenerator("full_qualified_name");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new HashMap<>();
        identifierMap.put(fullQualifiedNameIdGenerator.getSymbolType(), fullQualifiedNameIdGenerator.getLastId());
        return identifierMap;
    }

    public FullQualifiedNameManager() {
        this.fullQualifiedNameDTOList = new ArrayList<>();
    }

    public List<FullQualifiedNameDTO> getFullQualifiedNameDTOList() {
        return fullQualifiedNameDTOList;
    }

    public void clear() {
        this.fullQualifiedNameDTOList.clear();
    }

    // TreeSitter에서 사용.
    public FullQualifiedNameDTO buildFullQualifiedName(Long fullQualifiedNameId,
                                                       Long symbolStatusId, String fullQualifiedName, Boolean isJdk) {
        FullQualifiedNameDTO fullQualifiedNameDTO = new FullQualifiedNameDTO();

        fullQualifiedNameDTO.setFullQualifiedNameId(fullQualifiedNameId);
        fullQualifiedNameDTO.setSymbolStatusId(symbolStatusId);
        fullQualifiedNameDTO.setFullQualifiedName(fullQualifiedName);
        fullQualifiedNameDTO.setIsJDK(isJdk);

        fullQualifiedNameDTOList.add(fullQualifiedNameDTO);
        return fullQualifiedNameDTO;
    }

    public FullQualifiedNameDTO buildFullQualifiedName(Long symbolStatusId, String fullQualifiedName, Boolean isJdk) {
        FullQualifiedNameDTO fullQualifiedNameDTO = new FullQualifiedNameDTO();

        fullQualifiedNameDTO.setFullQualifiedNameId(fullQualifiedNameIdGenerator.nextId());
        fullQualifiedNameDTO.setSymbolStatusId(symbolStatusId);
        fullQualifiedNameDTO.setFullQualifiedName(fullQualifiedName);
        fullQualifiedNameDTO.setIsJDK(isJdk);

        fullQualifiedNameDTOList.add(fullQualifiedNameDTO);
        return fullQualifiedNameDTO;
    }

}
