package othersPackage;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Halstead {

    String [] operators = {"=", "+", "-", "*", "\\", "^", "\"", "\'", ".", "~", "|", "[", "]", "(", ")", ";", ":", "%", ",", "!", "<",
            ">", "&", "{", "}", "abstract", "continue", "for", "new", "switch", "assert", "default", "goto",
            "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double",
            "implements", "protected", "throw", "byte", "else",	"import", "public", "throws", "case",
            "enum", "instanceof", "return",	"transient", "catch", "extends", "int", "String", "short", "try", "char",
            "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while", "println", "print", "nextLine", "nextBoolean", "nextByte",
            "nextDouble", "nextFloat", "nextInt", "nextLong", "nextShort"};

    List<String> operands = new ArrayList<>();

    public HashMap<String, Integer> mapOfOperands = new HashMap<String, Integer>();
    public HashMap<String, Integer> mapOfOperators = new HashMap<String, Integer>();

    private String sourceCode;
    private List<String> commentFilteredLines = new ArrayList<>();

    public void HalsteadCalculation(int totalOperators,int totalOperands,int uniqueOperators, int uniqueOperands) {
        double Vocabulary = uniqueOperators + uniqueOperands;
        double log = (int)(Math.log(Vocabulary)/Math.log(2));
        double Volume = (totalOperators+totalOperands)* log;
        double Difficulty = ((uniqueOperators/ 2) * (totalOperands / uniqueOperands));
        double Effort = Difficulty * Volume;

        System.out.println();
        System.out.println("**** HALSTEAD ****");
        System.out.println("Vocabulary: "+Vocabulary);
        System.out.println("Volume: "+ Volume);
        System.out.println("Difficulty: "+Difficulty);
        System.out.println("Effort: "+ Effort);
        System.out.println();
        System.out.println("***************");
    }

    public Halstead(String sourceCode) {
        this.sourceCode = sourceCode;
        filterComments();
        for (String line: commentFilteredLines)
        {
            //System.out.println(line);
            String quote = "";
            if (line.contains("\""))
            {
                int quoteStart = line.indexOf("\"");
                int quoteLength = line.substring(quoteStart+1).indexOf("\"") + 1;
                int quoteEnd = quoteStart + quoteLength;

                quote = line.substring(quoteStart+1,quoteEnd);
                filterTokens(quote);

                line = line.substring(0,quoteStart+1).concat(line.substring(quoteEnd));
            }

            String [] tokens = line.strip().split(" ");
            String conToken = "";
            for(String token:tokens){
                conToken = conToken + token;
            }
            filterTokens(conToken);
        }

        System.out.println("*** Operators ***");
        for(String key:mapOfOperators.keySet())
        {
            System.out.println(key + " = "+mapOfOperators.get(key));
        }

        System.out.println();
        System.out.println("*** Operands ***");
        for(String key:mapOfOperands.keySet())
        {
            System.out.println(key + " = "+mapOfOperands.get(key));
        }
        int totalOperators = 0;
        for(String key:mapOfOperators.keySet())
        {
            totalOperators = totalOperators+mapOfOperators.get(key);
        }
        int totalOperands = 0;
        for(String key:mapOfOperands.keySet())
        {
            totalOperands = totalOperands+mapOfOperands.get(key);
        }
        HalsteadCalculation(totalOperators,totalOperands,mapOfOperators.size(),mapOfOperands.size());


    }

    public void filterTokens (String token)
    {
        while (!token.isEmpty())
        {
            token = sliceToken (token);
        }
    }

    public String sliceToken (String token)
    {
        //System.out.println(token);
        int operatorPosition = token.length();

        for (String operator: operators)
        {
            if (token.contains(operator))
            {
                if (token.startsWith(operator))
                {
                    if (!mapOfOperators.containsKey(operator))
                    {
                        mapOfOperators.put(operator, 1);
                    }
                    else
                    {
                        mapOfOperators.put(operator, mapOfOperators.get(operator)+1);
                    }

                    return token.substring(operator.length());
                }

                operatorPosition = Math.min(operatorPosition, token.indexOf(operator));
            }
        }

        String remainingToken = token.substring(0,operatorPosition);

        /*for (String operand: operands)
        {
            if (remainingToken.equals(operand))
            {
                if (!mapOfOperands.containsKey(operand))
                {
                    mapOfOperands.put(operand, 1);
                }
                else
                {
                    mapOfOperands.put(operand, mapOfOperands.get(operand)+1);
                }
            }

            if (!mapOfOperands.containsKey(remainingToken))
            {
                mapOfOperands.put(remainingToken, 1);
            }
            else
            {
                mapOfOperands.put(remainingToken, mapOfOperands.get(remainingToken)+1);
            }
        }*/

        //System.out.println(token);

        if (operatorPosition<token.length())
        {
            if (token.charAt(operatorPosition)=='(')
            {
                if (!mapOfOperators.containsKey(remainingToken))
                {
                    mapOfOperators.put(remainingToken, 1);
                }
                else
                {
                    mapOfOperators.put(remainingToken, mapOfOperators.get(remainingToken)+1);
                }
            }
            else
            {
                if (!mapOfOperands.containsKey(remainingToken))
                {
                    mapOfOperands.put(remainingToken, 1);
                }
                else
                {
                    mapOfOperands.put(remainingToken, mapOfOperands.get(remainingToken)+1);
                }
            }
        }
        else
        {
            if (!mapOfOperands.containsKey(remainingToken))
            {
                mapOfOperands.put(remainingToken, 1);
            }
            else
            {
                mapOfOperands.put(remainingToken, mapOfOperands.get(remainingToken)+1);
            }
        }

        //System.out.println(mapOfOperators);
        //System.out.println(mapOfOperands);

        return token.substring(operatorPosition);
    }

    public void filterComments ()
    {
        String [] lines = sourceCode.split("\n");
        int lineCommentPosition = -1;
        int multiCommentStartPosition = -1;
        int multiCommentEndPosition = -1;
        boolean insideComment = false;

        for (String line: lines)
        {
           if (line.strip().isEmpty())
           {
               continue;
           }
           if (line.contains("//"))
           {
               lineCommentPosition = line.indexOf("//");
           }
           if (line.contains("/*"))
           {
               multiCommentStartPosition = line.indexOf("/*");
           }
           if (line.contains("*/"))
           {
               multiCommentEndPosition = line.indexOf("*/");
           }

           if (multiCommentStartPosition!=-1&&multiCommentEndPosition!=-1)
           {
               insideComment = false;
           }
           else if (!insideComment&&lineCommentPosition!=-1)
           {
               if (line.substring(0,lineCommentPosition).length()>0)
               {
                   commentFilteredLines.add(line.substring(0,lineCommentPosition));
               }
           }
           else if (insideComment&&multiCommentEndPosition!=-1)
           {
               insideComment = false;
           }
           else if (multiCommentStartPosition!=-1)
           {
                insideComment = true;
           }
           else {
               commentFilteredLines.add(line);
           }

           lineCommentPosition = -1;
           multiCommentStartPosition = -1;
           multiCommentEndPosition = -1;
        }
    }
}
