package org.tmaxcloud.superscm.service;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Statement;
import org.tmaxcloud.superscm.provider.ConnectionProvider;
import org.tmaxcloud.superscm.visit.ASTVisitor;

import java.sql.*;
import java.util.List;

public class InsertASTService {
    public static Long stmtId = 10L;

    public static Long thenStmtId;
    public static Long elseStmtId;

    public Long insertSrc(Long projectId, String path) throws SQLException, ClassNotFoundException {
        Connection conn = ConnectionProvider.getConnection();

        String sql = "insert into src(project_id, path) values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, projectId);
        pstmt.setString(2, path);

        pstmt.execute();

        sql = "select src_id from src where project_id=? and path=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1,projectId);
        pstmt.setString(2,path);

        ResultSet rs = pstmt.executeQuery();

        Long result = 0L;
        while(rs.next()) {
            result = rs.getLong("src_id");
        }
        return result;
    }
    public int insertAST(Long srcId, ASTNode astNode) throws SQLException, ClassNotFoundException {
        Connection conn = ConnectionProvider.getConnection();

        List<ASTNode> children = ASTVisitor.getChildren(astNode);
        String nodeType = "";
        String sql = "";
        PreparedStatement pstmt = null;

        if (children.size() > 0) {
            // compilationUnit insert
            sql = "insert into compilation_unit(src_id) values(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,srcId);
            pstmt.execute();

            for(ASTNode node : children) {
                nodeType = ASTNode.nodeClassForType(node.getNodeType()).getSimpleName();
                // PackageDeclaration
                if(nodeType.compareTo("PackageDeclaration") == 0) {

                }
                // ImportDeclaration
                else if(nodeType.compareTo("ImportDeclaration") == 0) {

                }
                // TypeDeclaration
                else if(nodeType.compareTo("TypeDeclaration") == 0) {

                    TypeDeclaration typeDeclaration = (TypeDeclaration) node;
                    // TypeDeclaration insert
                    Long typeDeclId = System.currentTimeMillis();
                    String typeName = typeDeclaration.getName().getFullyQualifiedName();
                    String modifiers = typeDeclaration.modifiers().get(0).toString();
                    sql = "insert into type_declaration(type_decl_id,type_name,modifiers,parent) values(?,?,?,?)";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setLong(1,typeDeclId);
                    pstmt.setString(2,typeName);
                    pstmt.setString(3,modifiers);
                    pstmt.setLong(4,1L);
                    pstmt.execute();

                    List<ASTNode> bodyDeclarations = typeDeclaration.bodyDeclarations();
                    for (ASTNode body : bodyDeclarations) {
                        insertBodyDeclarations(body, typeDeclId, pstmt);
                    }

                }
            }
        }
        return 0;
    }
    private void insertBodyDeclarations(ASTNode node, Long parent, PreparedStatement pstmt) throws SQLException, ClassNotFoundException {
        Connection conn = ConnectionProvider.getConnection();
        String nodeType = ASTNode.nodeClassForType(node.getNodeType()).getSimpleName();
        String sql = "";
        if(nodeType.compareTo("MethodDeclaration") == 0) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) node;
            // MethodDeclaration insert
            Long methodDeclId = System.currentTimeMillis();
            String methodName = "";
            List<Modifier> modifierList = methodDeclaration.modifiers();
            for(Modifier modifier : modifierList) {
                methodName = methodName + modifier.getKeyword().toString() + " ";
            }
            methodName = methodName + methodDeclaration.getName();
            sql = "insert into method_declaration(method_decl_id,method_name,parent) values(?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,methodDeclId);
            pstmt.setString(2,methodName);
            pstmt.setLong(3,parent);
            pstmt.execute();

            // Block insert
            Block block = methodDeclaration.getBody();
            insertBlock(block, methodDeclId, nodeType, pstmt);

        }
        else if(nodeType.compareTo("FieldDeclaration") == 0) {

        }
    }

    private void insertBlock(Block block, Long parentId, String parentNode, PreparedStatement pstmt) throws SQLException, ClassNotFoundException {
        Connection conn = ConnectionProvider.getConnection();
        String sql = "";

        Long blockId = System.currentTimeMillis();
        sql = "insert into block(block_id,parent_id,parent_node) values(?,?,?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, blockId);
        pstmt.setLong(2,parentId);
        pstmt.setString(3,parentNode);
        pstmt.execute();

        List<Statement> statementList = block.statements();
        for(Statement stmt : statementList) {
            insertStatement(stmt, blockId, "Block", pstmt);
        }
    }
    private void insertStatement(Statement stmt, Long parentId, String parentNode, PreparedStatement pstmt) throws SQLException, ClassNotFoundException {
        Connection conn = ConnectionProvider.getConnection();
        String nodeType;
        String sql = "";

        nodeType = ASTNode.nodeClassForType(stmt.getNodeType()).getSimpleName();
        if(nodeType.compareTo("IfStatement") == 0) {
            IfStatement ifStatement = (IfStatement) stmt;

            int nodeIntType = ifStatement.getNodeType();
            String expression = ifStatement.getExpression().toString();
            // thenStmt 가 있으면 먼저 처리한다.
            Block thenStmtBlock = (Block) ifStatement.getThenStatement();
            List<ASTNode> listt = ASTVisitor.getChildren(stmt);

            String thenStatement = ifStatement.getThenStatement().toString();
            Statement elseStatement = ifStatement.getElseStatement();

            sql = "insert into statement(stmt_id, node_type, expression, then_statement, optional_else_statement, parent_id, parent_node) values(?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,stmtId);
            pstmt.setInt(2,nodeIntType);
            pstmt.setString(3,expression);
            pstmt.setString(4,thenStatement);
            if(elseStatement != null) {
                pstmt.setLong(5, stmtId+1);
            } else {
                pstmt.setNull(5, Types.BIGINT);
            }
            pstmt.setLong(6, parentId);
            pstmt.setString(7,parentNode);
            pstmt.execute();

            if(elseStatement != null) {
                insertStatement(elseStatement, stmtId++, nodeType ,pstmt);
            } else {
                stmtId++;
            }

//            if(thenStmtBlock != null) {
//                insertBlock(thenStmtBlock, globalStmtId++, nodeType, pstmt);
//            }

        }
        else if(nodeType.compareTo("VariableDeclarationStatement") == 0) {

        }
    }
}
