package org.kryun.symbol.javaparser;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import java.util.Comparator;
import org.kryun.global.enums.symbol.SymbolStatusEnum;
import org.kryun.global.utils.FileUtil;
import org.kryun.symbol.javaparser.model.exception.SaveSymbolException;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.kryun.symbol.pkg.builder.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymbolBuilderWithJavaParser implements SymbolBuilder {
    private final Logger logger = LoggerFactory.getLogger(SymbolBuilderWithJavaParser.class.getName());

    private final Long symbolStatusId;
    private final String projectPath;
    private final String projectName;
    private final Boolean isDependency;
    private final ConvertJavaParserToSymbol convertJavaParserToSymbol;

    public SymbolBuilderWithJavaParser(Long symbolStatusId, String projectPath, String projectName, Boolean isDependency) {
        this.symbolStatusId = symbolStatusId;
        this.projectPath = projectPath;
        this.projectName = projectName;
        this.isDependency = isDependency;
        this.convertJavaParserToSymbol = new ConvertJavaParserToSymbol(symbolStatusId, isDependency);
    }

    @Override
    public SymbolContainer build() throws Exception {
        try {
            logger.info("Start parsing project with JavaParser");
            ProjectRoot projectRoot = getProjectRoot(projectPath + "/" + projectName, 0);
            logger.info("projectRoot: " + projectRoot);
            List<SourceRoot> sourceRootList = projectRoot.getSourceRoots();
            logger.info("#######for 0 ######## : " + sourceRootList);

            for (SourceRoot sourceRoot : sourceRootList) {
                logger.info("#######for 1 ########");
                List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
                for (ParseResult<CompilationUnit> parseResult : parseResults) {
                    Optional<CompilationUnit> optionalCompilationUnit = parseResult.getResult();
                    if (optionalCompilationUnit.isPresent()) {
                        CompilationUnit cu = optionalCompilationUnit.get();
                        String fileName = cu.getStorage().get().getPath().toString();
                        String srcPath = fileName.replace(projectPath + "/", "");
                        logger.info("fileName:" + fileName);
                        if (srcPath.endsWith("Testcode.java")) {
                            System.out.println("fileName is Testcode");
                        }
                        convertJavaParserToSymbol.visit(cu, srcPath);

                    }
                }
            }

            return convertJavaParserToSymbol;
        } catch (Exception e) {
            logger.error("Error in SymbolBuilderWithJavaParser.build()", e);
            throw new SaveSymbolException(SymbolStatusEnum.UNAVAILAbLE, e.getMessage(), "Error in SymbolBuilderWithJavaParser.build()");
        } finally {
            convertJavaParserToSymbol.printLastSymbol();
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
