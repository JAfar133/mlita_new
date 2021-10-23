package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String pkg1 = "!p+q+t";
        String pkg2 = "!p+q+h";
        String out = "!q+h";

        List<String> packageList = new ArrayList<>();
        packageList.add(pkg1); packageList.add(pkg2);

        //Считаем количество уникальных операндов
        for (String pkg:packageList)
            sb.append(pkg);
        sb.append(out);
        LexAnalyzator lexAnalyzator = new LexAnalyzator();
        List<Lexeme> lexemes = lexAnalyzator.lexAnalyze(sb.toString());
        Set<Character> characterSet = lexAnalyzator.getSetOfOperand();
        List<Character> operandList = new ArrayList<>(characterSet);
        int n = characterSet.size();

        List<List<String>> tableRowsList = getTableRowsList(n);

        printTruthTable(tableRowsList,packageList,out,operandList);

    }
    private static List<List<String>> getTableRowsList(int n) {
        List<List<String>> tableRowsList = new ArrayList<>();
        int rows = (int) Math.pow(2,n);
        int num = 0;
        for (int i=0; i<rows; i++) {
            List<String> row = new ArrayList<>();
            for (int j=n-1; j>=0; j--) {
                //Заполняем таблицу
                int boolValue = (i/(int) Math.pow(2, j))%2;
                row.add(String.valueOf(boolValue));
                }
            tableRowsList.add(row);
            num++;
            }
        return tableRowsList;
        }
        public static void printTruthTable(List<List<String>> tableRowsList,
                                           List<String> packageList,
                                           String out,
                                           List<Character> operandList){
            List<Boolean> pkgsResult = new ArrayList<>();
            boolean outResult;
            boolean valid = true;
            for (Character OP: operandList) {
                System.out.print(OP+" ");
            }
            System.out.print(" ");
            for (int i = 1;i<= packageList.size();i++) {
                System.out.print("C"+i+" ");
            }
            System.out.print("F ");
            System.out.println();
            for (List<String> opBoolList:tableRowsList) {
                for (String op:opBoolList) {
                    System.out.print(op+" ");
                }
                System.out.print(" ");
                for (String pkg:packageList) {
                    pkgsResult.add(getPkgOrOutResult(opBoolList,pkg,operandList) == "0" ? false:true);
                    System.out.print(getPkgOrOutResult(opBoolList,pkg,operandList)+"  ");
                }
                    outResult = getPkgOrOutResult(opBoolList,out,operandList) == "0" ? false:true;
                    System.out.print(getPkgOrOutResult(opBoolList,out,operandList)+"  ");

                    if(!isValid(pkgsResult,outResult)){
                        valid = false;
                        System.out.print("<-- не валидное выражение");
                    }
                System.out.println();
                }
            if(!valid)
                System.out.println("Вывод не валидный");
            else System.out.println("Вывод валидный");
            }

        public static boolean isValid(List<Boolean>pkgs,boolean out){
        int count =0;
            for (Boolean b:pkgs) {
                if(b) count++;
            }
            if(count== pkgs.size()&&!out){
                return false;
            }
            else return true;
        }
        public static String getPkgOrOutResult(List<String> valueList,String pkg,List<Character> operandList){
            LexAnalyzator lexAnalyzator = new LexAnalyzator();
            //Парсим выражение в список лексем(операторы и операнды)
            List<Lexeme> lexemes = lexAnalyzator.lexAnalyze(pkg);;
            //Создаем буфер для лексем
            LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
            //Создаем множество(уникальное) операндов
            Set<Character> characterSet = lexAnalyzator.getSetOfOperand();
            //Помещаем множество в список
            List<Character> list = new ArrayList<>(characterSet);
            //Проходимся по списку всех операндов
            for (Character operand:operandList) {
                //Если в списке операндов нашего выражения, содержится данный операнд
                if(list.contains(operand)){
                    //Текущий операнд
                    String currenOp = String.valueOf(operand);
                    //Одинаковые операнды, должны принимать одинаковое булево значение
                    while(lexAnalyzator.getLexemeByValue(currenOp)!=null)
                    {
                        //Получаем лексему с таким-же операндом
                        Lexeme lexeme = lexAnalyzator.getLexemeByValue(currenOp);
                        //Устанавливаем значение операнда 0 или 1
                        int a = operandList.indexOf(currenOp.toCharArray()[0]);
                        lexeme.setValue(valueList.get(a));
                    }
                }
            }
            boolean pkgBool = lexAnalyzator.Expr(lexemeBuffer);
            return pkgBool?"1":"0";
        }
    }

