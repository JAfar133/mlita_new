package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String pkg1 = "A->B";
        String pkg2 = "B->C";
        String pkg3 = "C->D";
        String pkg4 = "E->!D";
        String out1 = "A->!E";
        String out2 = "A*E";

        List<String> packageList = new ArrayList<>();
        List<String> outList = new ArrayList<>();
        packageList.add(pkg1); packageList.add(pkg2);packageList.add(pkg3);packageList.add(pkg4);
        outList.add(out1);outList.add(out2);
        //Считаем количество уникальных операндов
        for (String pkg:packageList)
            sb.append(pkg);
        for (String out:outList)
            sb.append(out);
        LexAnalyzator lexAnalyzator = new LexAnalyzator();
        lexAnalyzator.lexAnalyze(sb.toString());
        Set<Character> characterSet = lexAnalyzator.getSetOfOperand();
        List<Character> operandList = new ArrayList<>(characterSet);
        int n = characterSet.size();

        List<List<String>> tableRowsList = getTableRowsList(n);

        printTruthTable(tableRowsList,packageList,outList,operandList);

    }
    private static List<List<String>> getTableRowsList(int n) {
        List<List<String>> tableRowsList = new ArrayList<>();
        int rows = (int) Math.pow(2,n);
        for (int i=0; i<rows; i++) {
            List<String> row = new ArrayList<>();
            for (int j=n-1; j>=0; j--) {
                //Заполняем таблицу
                int boolValue = (i/(int) Math.pow(2, j))%2;
                row.add(String.valueOf(boolValue));
                }
            tableRowsList.add(row);
            }
        return tableRowsList;
        }
        public static void printTruthTable(List<List<String>> tableRowsList,
                                           List<String> packageList,
                                           List<String> outList,
                                           List<Character> operandList){
            List<Boolean> pkgsResult = new ArrayList<>();
            List<Boolean> outsResult = new ArrayList<>();
            Set<String> not_valid_outs = new TreeSet<>();
            Map<String,String> nameOfOperandList = new HashMap<>();
            boolean valid = true;
            System.out.print("Посылки:  ");
            for (int i = 1;i<= packageList.size();i++) {
                System.out.print("P"+i+"="+packageList.get(i-1)+", ");
            }
            System.out.println();
            System.out.print("Выводы:  ");
            for (int i = 1;i<= outList.size();i++) {
                System.out.print("С"+i+"="+outList.get(i-1)+", ");
            }
            System.out.println();

            System.out.print("n    ");
            for (Character OP: operandList) {
                System.out.print(OP+" ");
            }
            System.out.print("  ");
            for (int i = 1;i<= packageList.size();i++) {
                nameOfOperandList.put(packageList.get(i-1),"P"+i);
                System.out.print("P"+i+"  ");
            }
            System.out.print("P   ");
            for (int i = 1;i<= outList.size();i++) {
                nameOfOperandList.put(outList.get(i-1),"C"+i);
                System.out.print("C"+i+"  ");
            }

            System.out.println();
            int count=0;
            for (List<String> opBoolList:tableRowsList) {
                if(count<10)
                    System.out.print(count+"  | ");
                else System.out.print(count+" | ");
                count++;
                for (String op:opBoolList) {
                    System.out.print(op+" ");
                }
                System.out.print("| ");
                for (String pkg:packageList) {
                    pkgsResult.add(getPkgOrOutResult(opBoolList,pkg,operandList) == "0" ? false:true);
                    System.out.print(getPkgOrOutResult(opBoolList,pkg,operandList)+" | ");
                }
                System.out.print(getСonjunctionP(pkgsResult)?"1"+" | ":"0"+" | ");
                List<String> notValidOut = new ArrayList<>();
                for(String out:outList) {
                    outsResult.add(getPkgOrOutResult(opBoolList, out, operandList) == "0" ? false : true);
                    System.out.print(getPkgOrOutResult(opBoolList, out, operandList) + " | ");
                    if(!isValid(getСonjunctionP(pkgsResult),getPkgOrOutResult(opBoolList, out, operandList).equals("1")?true:false)){
                        valid = false;
                        notValidOut.add(out);
                    }
                }
                if(!valid){
                    System.out.print("<--Вывод(ы): ");

                    for (String s:notValidOut) {
                        not_valid_outs.add(nameOfOperandList.get(s));
                        System.out.print(nameOfOperandList.get(s)+"=");
                        System.out.print(s+" ");
                    }
                    System.out.print("не валидный(е)");
                }
                valid=true;
                    outsResult = new ArrayList<>();
                    pkgsResult = new ArrayList<>();
                System.out.println();
                }
            if(not_valid_outs!=null)
                System.out.println("Выводы: "+not_valid_outs+" не валидные");
            }
        public static boolean getСonjunctionP(List<Boolean>pkgs){
            boolean pkges=true;
            for (Boolean b:pkgs) {
                pkges = pkges&&b;
            }
            return pkges;
        }
        public static boolean isValid(boolean conP, boolean out){
            if(conP&!out){
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

