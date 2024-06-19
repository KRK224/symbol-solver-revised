package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.javaparser.model.dto.JavaParserPackageDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageManager {

    private final List<JavaParserPackageDTO> javaParserPackageDTOList;
    private final IdentifierGenerator packageIdGenerator = new IdentifierGenerator("package");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new HashMap<>();
        identifierMap.put(packageIdGenerator.getSymbolType(), packageIdGenerator.getLastId());
        return identifierMap;
    }

    public Long getLastId() {
        return packageIdGenerator.getLastId();
    }

    public PackageManager() {
        this.javaParserPackageDTOList = new ArrayList<>();
    }

    public List<JavaParserPackageDTO> getPackageDTOList() {
        return this.javaParserPackageDTOList;
    }

    public void clear() {
        this.javaParserPackageDTOList.clear();
    }

    public JavaParserPackageDTO buildPackage(Long blockId, Node node) {
        JavaParserPackageDTO javaParserPackageDTO = new JavaParserPackageDTO();

        String packageName = null;
        String nodeValue = node.toString();
        String pattern = "[a-z0-9\\.]+\\;"; // 마지막에 $ 붙이면 왜 안되지
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(nodeValue);
        while (m.find()) {
            packageName = m.group();
        }
        if (packageName!=null) {
            packageName = packageName.replace(";", "");
        }
        // nodeValue = nodeValue.replace("package", "");
        // nodeValue = nodeValue.trim();

        javaParserPackageDTO.setPackageId(packageIdGenerator.nextId());
        javaParserPackageDTO.setBlockId(blockId);
        javaParserPackageDTO.setName(packageName);

        javaParserPackageDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column));

        javaParserPackageDTOList.add(javaParserPackageDTO);

        return javaParserPackageDTO;
    }
}
