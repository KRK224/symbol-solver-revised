package org.kryun.symbol.javaparser;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import org.kryun.global.enums.symbol.SymbolStatusEnum;
import org.kryun.symbol.model.dto.SymbolStatusDTO;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectParser {

    private final Logger logger = LoggerFactory.getLogger(ProjectParser.class);

    private final String projectPath;
    private final String projectName;
    private final SymbolStatusDTO symbolStatusDTO;
    private final Boolean isDependency;

    public ProjectParser(String projectPath, String projectName, SymbolStatusDTO symbolStatusDTO, Boolean isDependency) {
        this.projectPath = projectPath;
        this.projectName = projectName;
        this.symbolStatusDTO = symbolStatusDTO;
        this.isDependency = isDependency;
    }

    public void parseProject() throws Exception {
        ConvertJavaParserToSymbol convertJavaParserToSymbol = new ConvertJavaParserToSymbol(symbolStatusDTO.getSymbolStatusId(), isDependency);
        try {
            try {
                ProjectRoot projectRoot = getProjectRoot(projectPath + "/" + projectName, 0);

                // 전체 프로젝트 ast로 조회
                logger.info("[4/7] (1) projectRoot: " + projectRoot);
                List<SourceRoot> sourceRootList = projectRoot.getSourceRoots();
                logger.info("#######for 0 ######## : " + sourceRootList);
                for (SourceRoot sourceRoot : sourceRootList) {
                    logger.info("#######for 1 ########");
                    List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
                    for (ParseResult<CompilationUnit> parseResult : parseResults) {
                        Optional<CompilationUnit> optionalCompilationUnit = parseResult.getResult();
                        if (optionalCompilationUnit.isPresent()) {
                            CompilationUnit cu = optionalCompilationUnit.get();

                            // logger.info(cu.toString());

                            String fileName = cu.getStorage().get().getPath().toString();
                            String srcPath = fileName.replace(projectPath + "/", "");

                            logger.info("fileName:" + fileName);

                            // cu를 활용
                            convertJavaParserToSymbol.visit(cu, srcPath);

                            // dotPrinter(fileName, cu);
                        }
                    }
                    // saveSourceCodesInOutputDir(sourceRoot);
                }
            } catch (Exception e) {
                // Parsing 실패 시
                symbolStatusDTO.setStatusEnum(SymbolStatusEnum.UNAVAILAbLE);
                throw e;
            }
        } finally {
            convertJavaParserToSymbol.clear();
        }
    }

    private ProjectRoot getProjectRoot(String projectPath, int depth) throws Exception {
        logger.info("find ProjectRoot recursively, depth: " + depth);
        logger.info("project path: " + projectPath);
        if (depth > 2) {
            throw new Exception("can't find ProjectRoot, please check package and src directory");
        }

        if (!projectPath.endsWith("/")) {
            projectPath += "/";
        }

        Path root = Paths.get(projectPath);
        SymbolSolverCollectionStrategy symbolSolverCollectionStrategy = new SymbolSolverCollectionStrategy();
        ProjectRoot projectRoot = symbolSolverCollectionStrategy.collect(root);
        if (projectRoot.getSourceRoots().size()==0) {
            File rootFile = new File(projectPath);
            String[] subSrcPath = rootFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isDirectory() ? name.startsWith("src"):false;
                }
            });
            if (subSrcPath.length==0) {
                throw new Exception(
                        "Can't find ProjectRoot, please check package and src directory");
            }
            return getProjectRoot(projectPath + subSrcPath[0], ++depth);
        }
        return projectRoot;

    }
}
