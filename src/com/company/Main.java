package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        String str = "b+a";

        printTruthTable(str);

    }
    private static void printTruthTable(String str) {
        LexAnalyzator lexAnalyzator = new LexAnalyzator();
        List<Lexeme> lexemes;
        LexemeBuffer lexemeBuffer;
        //Парсим выражение в список лексем(операторы и операнды)
        lexemes = lexAnalyzator.lexAnalyze(str);
        //Создаем буфер для лексем
        lexemeBuffer = new LexemeBuffer(lexemes);
        //Создаем множество(уникальное) операндов
        Set<Character> characterSet = lexAnalyzator.getSetOfOperand();
        int n= characterSet.size();
        //Количество строк
        int rows = (int) Math.pow(2,n);
        int count = 0;
        //Помещаем множество в список
        List<Character> list = new ArrayList<>(characterSet);
        System.out.println("f = "+str+"\n");
        System.out.print("  "+list.get(0));
        for (int j=n-2; j>=0; j--) {
            System.out.print(" "+list.get(n-1-j));
        }
        System.out.println("    f");
        for (int i=0; i<rows; i++) {
            System.out.print("| ");
            for (int j=n-1; j>=0; j--) {
                //Заполняем таблицу
                int boolValue = (i/(int) Math.pow(2, j))%2;
                System.out.print(boolValue + " ");
                //Чтобы не словить NPE...
                if(count<n){
                    //Выбираем операнд из списка(по очереди)
                    String operand = list.get(n-1-j).toString();
                    //Одинаковые операнды, должны принимать одинаковое булево значение
                    while(lexAnalyzator.getLexemeByValue(operand)!=null)
                    {
                        //Получаем лексему с таким-же операндом
                        Lexeme lexeme = lexAnalyzator.getLexemeByValue(operand);
                        //Устанавливаем значение операнда 0 или 1
                        lexeme.setValue(String.valueOf(boolValue));
                    }
                    count++;
                }
            }
            count=0;
            //Вычисляем значение функции для текущей строки булевых значений.
            boolean f = lexAnalyzator.Expr(lexemeBuffer);
            System.out.print(f?"|  1"+"  |  ":"|  0"+"  |  ");
            //Возвращаем список и буфер в первоначальное значение
            lexemes = lexAnalyzator.lexAnalyze(str);
            lexemeBuffer = new LexemeBuffer(lexemes);
            System.out.println();
        }
    }

}
