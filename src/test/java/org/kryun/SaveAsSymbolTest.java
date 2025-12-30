package org.kryun;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kryun.symbol.pkg.ProjectParser;
import org.kryun.symbol.service.SaveAsSymbol;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SaveAsSymbolTest {

    @InjectMocks
    private SaveAsSymbol saveAsSymbol;

    @Mock
    private ProjectParser projectParser;

    @Test
    public void whenSaveAsSymbol_thenProjectParserIsCalled() throws Exception {
        // given
        String projName = "testProject";
        String projectPath = "/path/to/project";
        String resultPath = null;
        String fileType = "csv";

        // when
        saveAsSymbol.saveAsSymbol(projName, projectPath, resultPath, fileType);

        // then
        verify(projectParser).parseProject();
    }
}
