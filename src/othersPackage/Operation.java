package othersPackage;

import org.eclipse.jdt.core.dom.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Operation {

    int complexity = 1;
    int loc = 0;
    int lloc = 0;
    int comments = 0;

    int wloc = 0;

    public String readFileToString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();

        return  fileData.toString();
    }

    public void parse(String str) {
        Halstead halstead = new Halstead(str);
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setResolveBindings(true);
        parser.setSource(str.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        //parser.setEnvironment(new String[] {"C:\\Users\\ASUS\\Desktop\\demo\\out\\production\\demo"}, new String[] {"C:\\Users\\ASUS\\Desktop\\demo\\src"}, null, true);
        parser.setEnvironment(null, null, null, true);
        parser.setUnitName("Saal.java");

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        String [] ploc = str.split("\n", -1);

        for (String line: ploc)
        {
            if (line.length()==0||(line.length()==1&&line.contains("\r"))) {
                wloc++;
            }
        }

        loc = ploc.length;

        List commentList = cu.getCommentList();

        for (Comment comment : (List<Comment>) commentList) {

            comment.accept(new ASTVisitor() {

                public boolean visit(LineComment node) {
                    comments++;
                    return true;
                }

                public boolean visit(BlockComment node) {
                    int startLineNumber = cu.getLineNumber(node.getStartPosition());
                    int endLineNumber = cu.getLineNumber(node.getStartPosition() + node.getLength());
                    comments = comments + endLineNumber - startLineNumber + 1;
                    return true;
                }

            });
        }

        cu.accept(new ASTVisitor() {
            public void preVisit (ASTNode node) {
                //System.out.println(node);
                if (node instanceof Statement && !(node instanceof Block))
                {
                    lloc++;
                }
            }

            public boolean visit (IfStatement node) {
                complexity++;
                return true;
            }

            public boolean visit (ForStatement node) {
                complexity++;
                return true;
            }

            public boolean visit (EnhancedForStatement node) {
                complexity++;
                return true;
            }

            public boolean visit (WhileStatement node) {
                complexity++;
                return true;
            }

            public boolean visit (DoStatement node) {
                complexity++;
                return true;
            }

            public boolean visit (MethodDeclaration node)
            {
                System.out.println("Method Name: " + node.getName());
                return true;
            }

            public void endVisit (MethodDeclaration node)
            {
                System.out.println("Cyclomatic Complexity: " + complexity);

                complexity = 1;
            }

            public boolean visit (SwitchCase node) {
                if (!node.isDefault())
                {
                    complexity++;
                }
                return true;
            }
        });

    }

    public void operations () {
        try {
            parse(readFileToString("src/sourcePackage/Saal.java"));

            System.out.println();

            System.out.println("****** LOC ******");

            System.out.println("WLOC: " + wloc);

            System.out.println("PLOC: " + (loc - comments - wloc));

            System.out.println("LOC: " + (loc));

            System.out.println("LLOC: " + lloc);

            System.out.println("Comments: " + comments);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
