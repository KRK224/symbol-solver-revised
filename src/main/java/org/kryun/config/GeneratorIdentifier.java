package org.kryun.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeneratorIdentifier {
    public final Map<String, Long> symbolIds = new LinkedHashMap<>();
    public GeneratorIdentifier() {
        symbolIds.put("symbol_reference", 0L);
        symbolIds.put("block", 0L);
        symbolIds.put("package", 0L);
        symbolIds.put("import", 0L);
        symbolIds.put("class", 0L);
        symbolIds.put("member_var_decl", 0L);
        symbolIds.put("stmt_var_decl", 0L);
        symbolIds.put("method_decl", 0L);
        symbolIds.put("parameter", 0L);
        symbolIds.put("argument", 0L);
        symbolIds.put("return_mapper", 0L);
        symbolIds.put("method_call_expr", 0L);
        symbolIds.put("assign_expr", 0L);
        symbolIds.put("full_qualified_name", 0L);
    }
    public Map<String, Long> getSymbolIds() {
        return symbolIds;
    }
}
