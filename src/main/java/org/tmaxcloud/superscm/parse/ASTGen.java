package org.tmaxcloud.superscm.parse;

import org.eclipse.jdt.core.dom.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ASTGen {

    public CompilationUnit getCompilationUnit(String javaCode) throws Exception {
        ASTParser astParser = ASTParser.newParser(AST.JLS17);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setSource(javaCode.toCharArray());
        astParser.setResolveBindings(true);

        CompilationUnit compilationUnit;
        try {
            compilationUnit = (CompilationUnit) astParser.createAST(null);
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("ECJ: Unable to parse file.");
        }
        return compilationUnit;
    }

    public String getSourceCode(String javaFilePath) {
        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFilePath));
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String javaCode = new String(input);
        return javaCode;
    }
}
