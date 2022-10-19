package tmaxcloud;

import java.util.List;
import java.sql.Timestamp;

public class SimpleProgram {

    final static int x = 1;
    final static int y = 2;

    // IfElseIfStatementEndsWithElseCheck
    public static void ifElseIfStatementEndsWithElseCheck() {
        // Noncompliant
        if (x == 16) {
            String s = "thenStatement-String";
        } else if (x == 18) {
            String s = "elseifStatement-String";
        } else if (x = 17) {
            String s = "elseifStatement-String2";
        }
        // compliant
        if (x == 16) {
            String s = "thenStatement-String";
        } else if (x == 17) {
            String s = "elseifStatement-String";
        } else {
            String s = "elseStatement-String";
        }
//        // Noncompilant
//        if(y == 2) {
//            String t = "y is 2";
//        } else if(y == 3) {
//            String t = "y is 3";
//        }
    }

//    public static void main(String[] args) {
//        // HardCodedSecretCheck
//        String password = "passwd1234";
//        String token = "php_12dc";
//        // SimpleClassNameCheck
//        java.util.List<String> myList; // Noncompilant
//        Timestamp tStamp; // Compilant
//    }
//    // SwitchCaseWithoutBreakCheck
//    public static void switchCaseWithoutBreakCheck() {
//        int myVariable = 0;
//        switch (myVariable) {
//            case 0:
//            case 1:
//                System.out.println("case 1");
//            case 2:
//                System.out.println("case 1");
//                break;
//            case 3:
//                return;
//            case 4:
//                throw new IllegalStateException();
//            default:
//                System.out.println("default");
//        }
//    }
}
//
//public class Tests extends SimpleProgram {
//
//}