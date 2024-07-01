package org.kryun.symbol.pkg.save.interfaces;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface SymbolSaverToFile extends SymbolSaver {
    static String getGetterMethodName(Field field) throws Exception {
        String fieldName = field.getName();
        String capitalizedFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        if (field.getType()==boolean.class || field.getType()==Boolean.class) {
            return capitalizedFieldName.startsWith("Is")? "get" + capitalizedFieldName : "is" + capitalizedFieldName;
        } else {
            return "get" + capitalizedFieldName;
        }
    }

    static List<String> createHeader(Class<?> classType) throws Exception {
        List<String> columnList = new ArrayList<>();
        Class<?> clazz = classType;
        while (clazz!=null) {
            for (var field : clazz.getDeclaredFields()) {
                columnList.add(field.getName());
            }
            clazz = clazz.getSuperclass();
            System.out.println("super clazz = " + clazz);
            System.out.println("super clazz.getPackage() = " + clazz.getPackage());
            if (!clazz.getPackage().equals(classType.getPackage()))
                clazz = null;
        }
        return columnList;
    }


}
