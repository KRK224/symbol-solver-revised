package org.kryun.symbol.javaparser.symbolsolver.interfaces.referabletyperesolver;

import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.util.Optional;

public interface ReferableTypeResolver<T> {

    public static final TypeSolver reflectionSolver = new ReflectionTypeSolver();

    /**
     * Check if the given full qualified name is JDK class or not [실제 resolve를 통해 검증]
     *
     * @param fqn
     * @return
     */
    static boolean isJdk(String fqn) {
        try {
            reflectionSolver.solveType(fqn);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isJdk(String fqn, String methodName) {
        try {
            // org.kryun.org.kryun.symbol.pkg.symbolsolver.interfaces.referabletyperesolver.ReferableTypeResolver.isJdkPackage()...
            int position = fqn.indexOf(methodName);
            String classFQN = fqn.substring(0, position - 1); // remove dot
            reflectionSolver.solveType(classFQN);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Deprecated
    static boolean isJdkByClassName(String className) {
        try {
            String fqn = "java.lang." + className;
            return isJdk(fqn);
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isJdkPackage(String packageName) {
        return (packageName.startsWith("java.") || packageName.startsWith("javax."));
    }

    public abstract Optional<String> getFullQualifiedName(T resolvedType);

}
