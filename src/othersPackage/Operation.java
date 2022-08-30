package othersPackage;

import org.eclipse.jdt.core.dom.*;
//import org.eclipse.jdt.core.dom.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Operation {

    public GraphNode root;
    Stack<GraphNode> graphNodeStack = new Stack<GraphNode> ();
    Set<GraphNode> assertNodeSet = new HashSet<>();
    Set<GraphNode> tryBodySet = new HashSet<>();
    Map<IVariableBinding,Set<GraphNode>> mapForVariableBinding = new HashMap<>();
    Map<IMethodBinding,Set<GraphNode>> mapForMethodInvocationBinding = new HashMap<>();
    Map<IMethodBinding,GraphNode> mapForMethodDeclarationBinding = new HashMap<>();
    int i = 0;
    int marker = 0;
    int marking = 0;

    int complexity = 1;
    int loc = 0;
    int lloc = 0;
    int comments = 0;

    int wloc = 0;

    IVariableBinding s;
    public Set<IVariableBinding> setOfVariableBinding = new HashSet<>();
    public Set<IMethodBinding> setOfMethodBinding = new HashSet<>();
    ASTNode startingNode;
    Set<ASTNode> nodesForBackwardSlicing = new TreeSet<>(Comparator.comparing(ASTNode::getStartPosition));
    Set<ASTNode> nodesForForwardSlicing = new TreeSet<>(Comparator.comparing(ASTNode::getStartPosition));

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
        //comments = commentList.size();


        for (Comment comment : (List<Comment>) commentList) {

            comment.accept(new ASTVisitor() {

                public boolean visit(LineComment node) {
                    //System.out.println(node.getStartPosition());
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


        root = new GraphNode();
        graphNodeStack.push(root);

        cu.accept(new ASTVisitor() {
            public void preVisit (ASTNode node) {
                //System.out.println(node);
                if (node instanceof Statement && !(node instanceof Block))
                {
                    lloc++;
                }
                /*if (node instanceof Comment)
                {
                    System.out.println(node);
                    comments++;
                }*/
            }

            public boolean visit (IfStatement node) {
                //System.out.println(node.getElseStatement());
                complexity++;
                //System.out.println(node.getExpression());
                //System.out.println(node);
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
                return true;
            }

            public boolean visit (SynchronizedStatement node) {
                //System.out.println(node.getExpression());
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
                return true;
            }

            public boolean visit (ForStatement node) {
                //System.out.println(node.getBody());
                complexity++;
                //System.out.println(node.getExpression());
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
                return true;
            }

            public boolean visit (EnhancedForStatement node) {
                complexity++;
                //System.out.println(node.getExpression());
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
                return true;
            }

            public boolean visit (WhileStatement node) {
                complexity++;
                //System.out.println(node);
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
                return true;
            }

            public boolean visit (DoStatement node) {
                complexity++;
                //System.out.println(node);
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
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

            public boolean visit (SwitchStatement node) {
                //System.out.println(node);
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
                return true;
            }

            public boolean visit (SwitchCase node) {
                if (!node.isDefault())
                {
                    complexity++;
                }
                //System.out.println("ccfc");
                //System.out.println(node);
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                if(marker == 1)
                    tryBodySet.add(temp);
                return true;
            }

            public boolean visit (LabeledStatement node) {
                //System.out.println(node.getExpression());
                GraphNode temp;
                temp = new GraphNode(node);
                temp.parents.add(graphNodeStack.peek());
                graphNodeStack.peek().children.add(temp);
                graphNodeStack.push(temp);
                for(GraphNode g: assertNodeSet)
                {
                    g.children.add(temp);
                    temp.parents.add(g);
                }
                return true;
            }

            public void endVisit (ForStatement node) {
                graphNodeStack.pop();
            }

            public void endVisit (EnhancedForStatement node) {
                graphNodeStack.pop();
            }

            public void endVisit (WhileStatement node) {
                graphNodeStack.pop();
            }

            public void endVisit (DoStatement node) {
                GraphNode temp =  graphNodeStack.pop();
                graphNodeStack.peek().children.addAll(temp.children);
                for(GraphNode g : temp.children) {
                    g.parents.add(graphNodeStack.peek());
                }
            }

            public void endVisit (SwitchStatement node) {
                graphNodeStack.pop();
            }

            public void endVisit (SwitchCase node) {
                graphNodeStack.pop();
            }

            public void endVisit (IfStatement node) {
                graphNodeStack.pop();
            }

            public void endVisit (SynchronizedStatement node) {
                graphNodeStack.pop();
            }
        });

    }

    /*public void halsted (String str)
    {

    }*/

    public void operations () {
        try {
            parse(readFileToString("src/sourcePackage/Saal.java"));

            //System.out.println("Cyclomatic Complexity: " + complexity);

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
