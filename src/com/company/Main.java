package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
        String str = "a+(b*c)+(c*(d+g))";

        printTruthTable(str);

    }
    private static void printTruthTable(String str) {
        LexAnalyzator lexAnalyzator = new LexAnalyzator();
        List<Lexeme> lexemes;
        LexemeBuffer lexemeBuffer;
        lexemes = lexAnalyzator.lexAnalyze(str);
        lexemeBuffer = new LexemeBuffer(lexemes);
        Set<Character> characterSet = lexAnalyzator.getSetOfOperand();
        int n= characterSet.size();
        int rows = (int) Math.pow(2,n);
        int count = 0;
        List<Character> list = new ArrayList<>(characterSet);
        for (int j=n-1; j>=0; j--) {
            System.out.print(list.get(n-1-j)+" ");
        }
        System.out.print("f");
        System.out.println();

        for (int i=0; i<rows; i++) {
            for (int j=n-1; j>=0; j--) {
                int boolValue = (i/(int) Math.pow(2, j))%2;
                System.out.print(boolValue + " ");
                if(count<n){
                    String operator = list.get(n-1-j).toString();
                    Lexeme lexeme;
                    while(lexAnalyzator.getLexemeByValue(operator)!=null)
                    {
                        lexeme = lexAnalyzator.getLexemeByValue(operator);
                        lexeme.setValue(String.valueOf(boolValue));
                    }
                    count++;
                }
            }
            count=0;
            System.out.print(lexAnalyzator.Expr(lexemeBuffer)?"1"+" ":"0"+" ");
            lexemes = lexAnalyzator.lexAnalyze(str);
            lexemeBuffer = new LexemeBuffer(lexemes);
            System.out.println();
        }
    }

}
