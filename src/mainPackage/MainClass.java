package mainPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;
import othersPackage.*;


public class MainClass {



    public static void main(String args[]) {

        Operation op = new Operation();
        op.operations();
    }
}