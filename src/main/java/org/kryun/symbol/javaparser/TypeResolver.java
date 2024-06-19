package org.kryun.symbol.javaparser;


import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.kryun.symbol.javaparser.management.FullQualifiedNameManager;
import org.kryun.symbol.javaparser.model.dto.JavaParserImportDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMemberVariableDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodCallExprDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserParameterDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserReturnMapperDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserStmtVariableDeclarationDTO;
import org.kryun.symbol.javaparser.symbolsolver.interfaces.referabletyperesolver.ReferableTypeResolver;
import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import org.kryun.symbol.model.dto.interfaces.FQNReferable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;


/**
 * ConvertJavaParserToSymbol에서 호출하는 Class 각각의 노드 타입에 맞게 FQN DTO 생성 및 업데이트
 */
public class TypeResolver {
    private final Long symbolStatusId;
    private TypeResolverManager typeResolverManager = new TypeResolverManager();

    private final FullQualifiedNameManager fullQualifiedNameManager;


    protected TypeResolver(Long symbolStatusId, FullQualifiedNameManager fullQualifiedNameManager) {
        this.symbolStatusId = symbolStatusId;
        this.fullQualifiedNameManager = fullQualifiedNameManager;
    }

    protected TypeResolver(Long symbolStatusId, FullQualifiedNameManager fullQualifiedNameManager, Connection conn) {
        this.symbolStatusId = symbolStatusId;
        this.fullQualifiedNameManager = fullQualifiedNameManager;
        this.typeResolverManager = new TypeResolverManager(conn);
    }

    public void resolveSymbolAndUpdateFQNDTO(Node node, String nodeType, FQNReferable fqnReferable, Map<String, JavaParserImportDTO> typeImportMapper) {
        if (nodeType.equals("ClassOrInterfaceDeclaration")) {
            typeResolverManager.generateClassFullQualifiedName((ClassOrInterfaceDeclaration) node)
                    .ifPresent(fqn -> updateClassFQNReferableDTO(fqn, fqnReferable));
        } else if (nodeType.equals("EnumDeclaration")) {
            typeResolverManager.generateEnumFullQualifiedName((EnumDeclaration) node)
                    .ifPresent(fqn -> updateClassFQNReferableDTO(fqn, fqnReferable));
        } else if (nodeType.equals("MethodDeclaration")) {
            JavaParserMethodDeclarationDTO mdDto = (JavaParserMethodDeclarationDTO) fqnReferable;
            JavaParserReturnMapperDTO rmDto = mdDto.getReturnMapper();
            List<JavaParserParameterDTO> javaParserParameterDTOList = mdDto.getParameters();

            typeResolverManager.generateMethodDeclFullQualifiedName((MethodDeclaration) node)
                    .ifPresent(fqn -> updateMethodFQNReferableDTO(fqn, mdDto,
                            mdDto.getName()));

            typeResolverManager.generateReturnFullQualifiedName((MethodDeclaration) node)
                    .ifPresentOrElse(fqn -> updateClassFQNReferableDTO(fqn, rmDto), () -> {
                        String type = rmDto.getType();
                        JavaParserImportDTO javaParserImportDTO = typeImportMapper.get(normalizeType(type));
                        if (javaParserImportDTO!=null) {
                            updateClassFQNReferableDTO(javaParserImportDTO.getName(), rmDto);
                        }
                    });

            javaParserParameterDTOList.forEach(paramDto -> {
                Parameter parameterNode = (Parameter) paramDto.getNode();
                typeResolverManager.generateParameterFullQualifiedName(parameterNode)
                        .ifPresentOrElse(fqn -> updateClassFQNReferableDTO(fqn, paramDto),
                                () -> {
                                    String type = paramDto.getType();
                                    JavaParserImportDTO javaParserImportDTO = typeImportMapper.get(normalizeType(type));
                                    if (javaParserImportDTO!=null) {
                                        updateClassFQNReferableDTO(javaParserImportDTO.getName(), paramDto);
                                    }
                                });
            });
        } else if (nodeType.equals("ConstructorDeclaration")) {
            JavaParserMethodDeclarationDTO mdDto = (JavaParserMethodDeclarationDTO) fqnReferable;
            List<JavaParserParameterDTO> javaParserParameterDTOList = mdDto.getParameters();
            typeResolverManager.generateConstructorDeclFullQualifiedName((ConstructorDeclaration) node)
                    .ifPresent(fqn -> updateMethodFQNReferableDTO(fqn, mdDto, mdDto.getName()));
            javaParserParameterDTOList.forEach(paramDto -> {
                Parameter parameterNode = (Parameter) paramDto.getNode();
                typeResolverManager.generateParameterFullQualifiedName(parameterNode)
                        .ifPresentOrElse(fqn -> updateClassFQNReferableDTO(fqn, paramDto),
                                () -> {
                                    String type = paramDto.getType();
                                    JavaParserImportDTO javaParserImportDTO = typeImportMapper.get(normalizeType(type));
                                    if (javaParserImportDTO!=null) {
                                        updateClassFQNReferableDTO(javaParserImportDTO.getName(), paramDto);
                                    }
                                });
            });
        } else if (nodeType.equals("FieldDeclaration")) {
            JavaParserMemberVariableDeclarationDTO mvd = (JavaParserMemberVariableDeclarationDTO) fqnReferable;
            typeResolverManager.generateFieldDeclFullQualifiedName((FieldDeclaration) node)
                    .ifPresentOrElse(fqn -> updateClassFQNReferableDTO(fqn, mvd), () -> {
                        String type = mvd.getType();
                        JavaParserImportDTO javaParserImportDTO = typeImportMapper.get(normalizeType(type));
                        if (javaParserImportDTO!=null) {
                            updateClassFQNReferableDTO(javaParserImportDTO.getName(), mvd);
                        }
                    });
        } else if (nodeType.equals("VariableDeclarationExpr")) {
            JavaParserStmtVariableDeclarationDTO svd = (JavaParserStmtVariableDeclarationDTO) fqnReferable;
            typeResolverManager.generateVariableDeclFullQualifiedName((VariableDeclarationExpr) node)
                    .ifPresentOrElse(fqn -> updateClassFQNReferableDTO(fqn, svd), () -> {
                        String type = svd.getType();
                        JavaParserImportDTO javaParserImportDTO = typeImportMapper.get(normalizeType(type));
                        if (javaParserImportDTO!=null) {
                            updateClassFQNReferableDTO(javaParserImportDTO.getName(), svd);
                        }
                    });
        } else if (nodeType.equals("MethodCallExpr")) {
            JavaParserMethodCallExprDTO mceDto = (JavaParserMethodCallExprDTO) fqnReferable;
            typeResolverManager.generateMethodCallExprFullQualifiedName((MethodCallExpr) node)
                    .ifPresent(fqn -> updateMethodFQNReferableDTO(fqn, mceDto, mceDto.getName()));
        }
    }

    private void updateClassFQNReferableDTO(String fqn, FQNReferable fqnReferable) {
        if (fqn.equals("primitive")) {
            fqnReferable.setFullQualifiedNameId(-1L);
        } else {
            FullQualifiedNameDTO fullQualifiedNameDTO = typeResolverManager
                    .getFullQualifiedNameDTOFromTypeMapper(fqn, symbolStatusId)
                    .orElseGet(() -> {
                        Boolean isJDK = ReferableTypeResolver.isJdk(fqn);
                        FullQualifiedNameDTO newFullQualifiedNameDTO = fullQualifiedNameManager
                                .buildFullQualifiedName(symbolStatusId, fqn, isJDK);
                        typeResolverManager.registerFullQualifiedNameDTO(fqn, newFullQualifiedNameDTO);
                        return newFullQualifiedNameDTO;
                    });
            fqnReferable.setFullQualifiedNameId(fullQualifiedNameDTO.getFullQualifiedNameId());
        }

    }

    private void updateMethodFQNReferableDTO(String fqn, FQNReferable fqnReferable, String methodName) {
        if (fqn.equals("primitive")) {
            fqnReferable.setFullQualifiedNameId(-1L);
        } else {
            FullQualifiedNameDTO fullQualifiedNameDTO = typeResolverManager
                    .getFullQualifiedNameDTOFromTypeMapper(fqn, symbolStatusId)
                    .orElseGet(() -> {
                        Boolean isJDK = ReferableTypeResolver.isJdk(fqn, methodName);
                        FullQualifiedNameDTO newFullQualifiedNameDTO = fullQualifiedNameManager
                                .buildFullQualifiedName(symbolStatusId, fqn, isJDK);
                        typeResolverManager.registerFullQualifiedNameDTO(fqn, newFullQualifiedNameDTO);
                        return newFullQualifiedNameDTO;
                    });
            fqnReferable.setFullQualifiedNameId(fullQualifiedNameDTO.getFullQualifiedNameId());
        }
    }


    /**
     * 실제 사용한 타입에 Generic이 존재하거나 import class의 InnerClass인 경우에 대한 처리.
     *
     * @param type
     * @return
     */
    private String normalizeType(String type) {
        String normalizedType = type;
        if (normalizedType.contains("<")) { // Generic을 가지고 있는 type
            normalizedType = normalizedType.substring(0, normalizedType.indexOf("<"));
        }
        if (normalizedType.contains(".")) {
            String[] splits = normalizedType.split("\\.");
            normalizedType = splits[splits.length - 1];
        }
        return normalizedType;
    }

    public void clear() {
        typeResolverManager.clear();
    }
}
