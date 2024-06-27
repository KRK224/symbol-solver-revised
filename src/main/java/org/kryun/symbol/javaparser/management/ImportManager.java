package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import org.kryun.symbol.javaparser.model.dto.JavaParserImportDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.kryun.symbol.pkg.IdentifierGenerator;

public class ImportManager {
    private final List<JavaParserImportDTO> javaParserImportDTOList;
    private final IdentifierGenerator importIdGenerator = new IdentifierGenerator("import");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new HashMap<>();
        identifierMap.put(importIdGenerator.getSymbolType(), importIdGenerator.getLastId());
        return identifierMap;
    }

    public ImportManager() {
        this.javaParserImportDTOList = new ArrayList<>();
    }

    public List<JavaParserImportDTO> getImportDTOList() {
        return this.javaParserImportDTOList;
    }

    public void clear() {
        this.javaParserImportDTOList.clear();
    }

    public JavaParserImportDTO buildImport(Long blockId, Node node) {
        JavaParserImportDTO javaParserImportDTO = new JavaParserImportDTO();

        String name = "";
        String packageName = "";
        String className = "";
        String memberName = "";

        ImportDeclaration importNode = (ImportDeclaration) node;
        Boolean isStatic = importNode.isStatic();
        Boolean isAsterisk = importNode.isAsterisk();
        Name nameNode = importNode.getName();
        name = nameNode.toString();

        if (isStatic && isAsterisk) {
            memberName = "*";
            Optional<Name> packageQualifier = nameNode.getQualifier();
            if (packageQualifier.isPresent()) {
                packageName = packageQualifier.get().toString();
            }
            // 여기서 name은 ClassFQN이다.
            className = name.replace(packageName + ".", "");
            name += ".*";
        } else if (isAsterisk) {
            packageName = name;
            className = "*";
            name += ".*";
        } else if (isStatic) {
            Optional<Name> classFQNQuailfier = nameNode.getQualifier();
            if (classFQNQuailfier.isPresent()) {
                String classFQN = classFQNQuailfier.get().toString();
                Optional<Name> packageQuailfier = classFQNQuailfier.get().getQualifier();
                if (packageQuailfier.isPresent()) {
                    // 여기서 name은 memberFQN이다.
                    packageName = packageQuailfier.get().toString();
                    className = classFQN.replace(packageName + ".", "").trim();
                    memberName = name.replace(classFQN + ".", "").trim();
                }
            }
        } else {
            // 일반 import문
            Optional<Name> packageQualifier = nameNode.getQualifier();
            if (packageQualifier.isPresent()) {
                packageName = packageQualifier.get().toString();
                // 여기서 name은 classFQN이다.
                className = name.replace(packageName + ".", "").trim();
            }
        }

        javaParserImportDTO.setImportId(importIdGenerator.nextId());
        javaParserImportDTO.setBlockId(blockId);
        javaParserImportDTO.setName(name);
        javaParserImportDTO.setPackageName(packageName);
        javaParserImportDTO.setClassName(className);
        javaParserImportDTO.setMemberName(memberName);
        javaParserImportDTO.setIsAsterisk(isAsterisk);
        javaParserImportDTO.setIsStatic(isStatic);
        javaParserImportDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column));

        javaParserImportDTOList.add(javaParserImportDTO);

        return javaParserImportDTO;
    }
}
