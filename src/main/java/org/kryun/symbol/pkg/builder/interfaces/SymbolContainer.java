package org.kryun.symbol.pkg.builder.interfaces;

import java.util.List;
import java.util.Map;
import org.kryun.symbol.model.dto.ArgumentDTO;
import org.kryun.symbol.model.dto.BlockDTO;
import org.kryun.symbol.model.dto.ClassDTO;
import org.kryun.symbol.model.dto.ExpressionDTO;
import org.kryun.symbol.model.dto.ForStmtDTO;
import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import org.kryun.symbol.model.dto.IfStmtDTO;
import org.kryun.symbol.model.dto.ImportDTO;
import org.kryun.symbol.model.dto.MemberVariableDeclarationDTO;
import org.kryun.symbol.model.dto.MethodCallExprDTO;
import org.kryun.symbol.model.dto.MethodDeclarationDTO;
import org.kryun.symbol.model.dto.PackageDTO;
import org.kryun.symbol.model.dto.ParameterDTO;
import org.kryun.symbol.model.dto.ReturnMapperDTO;
import org.kryun.symbol.model.dto.StmtVariableDeclarationDTO;
import org.kryun.symbol.model.dto.SwitchStmtDTO;
import org.kryun.symbol.model.dto.SymbolReferenceDTO;
import org.kryun.symbol.model.dto.WhileStmtDTO;

public interface SymbolContainer {

    Map<String, Long> getSymbolIds();

    List<SymbolReferenceDTO> getSymbolReferenceDTOList();

    List<FullQualifiedNameDTO> getFullQualifiedNameDTOList();

    List<? extends BlockDTO> getBlockDTOList();

    List<? extends PackageDTO> getPackageDTOList();

    List<? extends ClassDTO> getClassDTOList();

    List<? extends ImportDTO> getImportDTOList();

    List<? extends ExpressionDTO> getExpressionDTOList();

    List<? extends MemberVariableDeclarationDTO> getMemberVariableDeclarationDTOList();

    List<? extends MethodDeclarationDTO> getMethodDeclarationDTOList();

    List<? extends MethodCallExprDTO> getMethodCallExprDTOList();

    List<? extends StmtVariableDeclarationDTO> getStmtVariableDeclarationDTOList();

    List<? extends ArgumentDTO> getArgumentDTOList();

    List<? extends ParameterDTO> getParameterDTOList();

    List<? extends ReturnMapperDTO> getReturnMapperDTOList();

    List<? extends IfStmtDTO> getIfStmtDTOList();

    List<? extends ForStmtDTO> getForStmtDTOList();

    List<? extends WhileStmtDTO> getWhileStmtDTOList();

    List<? extends SwitchStmtDTO> getSwitchStmtDTOList();

    void clear();
}
