package org.kryun.symbol.javaparser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.kryun.symbol.javaparser.symbolsolver.ClassOriginRegister;
import org.kryun.symbol.javaparser.symbolsolver.ConstructorOriginRegister;
import org.kryun.symbol.javaparser.symbolsolver.EnumOriginRegister;
import org.kryun.symbol.javaparser.symbolsolver.ForwardFDTypeResolver;
import org.kryun.symbol.javaparser.symbolsolver.ForwardMCETypeResolver;
import org.kryun.symbol.javaparser.symbolsolver.ForwardNameExprTypeResolver;
import org.kryun.symbol.javaparser.symbolsolver.ForwardParamTypeResolver;
import org.kryun.symbol.javaparser.symbolsolver.ForwardRMTypeResolver;
import org.kryun.symbol.javaparser.symbolsolver.ForwardVDTypeResolver;
import org.kryun.symbol.javaparser.symbolsolver.MethodOriginRegister;
import org.kryun.symbol.javaparser.symbolsolver.TypeMapperManager;
import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import java.sql.Connection;
import java.util.Optional;


class TypeResolverManager {

    private Connection conn;
    private final TypeMapperManager typeMapperManager = new TypeMapperManager();
    private final ForwardRMTypeResolver returnMapperTypeResolver = ForwardRMTypeResolver.getInstance();
    private final ForwardMCETypeResolver methodCallExprTypeResolver = ForwardMCETypeResolver.getInstance();
    private final ForwardFDTypeResolver fieldDeclTypeResolver = ForwardFDTypeResolver.getInstance();
    private final ForwardVDTypeResolver variableDeclTypeResolver = ForwardVDTypeResolver.getInstance();
    private final ForwardParamTypeResolver parameterTypeResolver = ForwardParamTypeResolver.getInstance();
    private final ForwardNameExprTypeResolver nameExprTypeResolver = ForwardNameExprTypeResolver.getInstance();
    private final ClassOriginRegister classOriginRegister = ClassOriginRegister.getInstance();
    private final MethodOriginRegister methodOriginRegister = MethodOriginRegister.getInstance();
    private final EnumOriginRegister enumOriginRegister = EnumOriginRegister.getInstance();
    private final ConstructorOriginRegister constructorOriginRegister = ConstructorOriginRegister.getInstance();


    protected TypeResolverManager() {
    }

    protected TypeResolverManager(Connection conn) {
        this.conn = conn;
    }

    Optional<String> generateClassFullQualifiedName(
            ClassOrInterfaceDeclaration ClassOrInterfaceDeclaration) {
        try {
            return classOriginRegister.getFullQualifiedName(ClassOrInterfaceDeclaration);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<String> generateEnumFullQualifiedName(EnumDeclaration enumNode) {
        try {
            return enumOriginRegister.getFullQualifiedName(enumNode);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    Optional<String> generateMethodDeclFullQualifiedName(MethodDeclaration md) {
        try {
            return methodOriginRegister.getFullQualifiedName(md);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<String> generateConstructorDeclFullQualifiedName(ConstructorDeclaration cd) {
        try {
            return constructorOriginRegister.getFullQualifiedName(cd);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }


    Optional<String> generateReturnFullQualifiedName(MethodDeclaration md) {
        try {
            return returnMapperTypeResolver.getFullQualifiedName(md);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<String> generateParameterFullQualifiedName(Parameter pm) {
        try {
            return parameterTypeResolver.getFullQualifiedName(pm);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<String> generateMethodCallExprFullQualifiedName(MethodCallExpr mce) {
        try {
            return methodCallExprTypeResolver.getFullQualifiedName(mce);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<String> generateFieldDeclFullQualifiedName(FieldDeclaration fd) {
        try {
            return fieldDeclTypeResolver.getFullQualifiedName(fd);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<String> generateVariableDeclFullQualifiedName(VariableDeclarationExpr vdExpr) {
        try {
            return variableDeclTypeResolver.getFullQualifiedName(vdExpr);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<String> generateNameExprFullQualifiedName(Expression nameExpr) {
        try {
            return nameExprTypeResolver.getFullQualifiedName(nameExpr);
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    Optional<FullQualifiedNameDTO> getFullQualifiedNameDTOFromTypeMapper(String fullQualifiedName,
                                                                         Long symbolStatusId) {
        try {
            return typeMapperManager.getFullQualifiedNameDTOFromTypeMapper(fullQualifiedName);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    boolean registerFullQualifiedNameDTO(String fullQualifiedName,
                                         FullQualifiedNameDTO fullQualifiedNameDTO) {
        try {
            return typeMapperManager.registerFullQualifiedNameDTO(fullQualifiedName,
                    fullQualifiedNameDTO);
        } catch (Exception e) {
            return false;
        }
    }

    void clear() {
        try {
            typeMapperManager.clearTypeMapper();
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }

    }
}
