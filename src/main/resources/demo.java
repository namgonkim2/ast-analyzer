import java.util.List;
import java.sql.Timestamp;

public class SimpleProgram {

    public static void main(String[] args) {
        // HardCodedSecretCheck
        String password = "passwd1234";
        String token = "php_12dc";
        // SimpleClassNameCheck
        java.util.List<String> myList; // Noncompilant
        Timestamp tStamp; // Compilant
    }

    // IfElseIfStatementEndsWithElseCheck
    public static void foo() {
        if (x == 16) {
        } else if (x == 17) {
        } else if (x == 18) {  // Noncompliant
        }
        if (x == 16) {
        } else if (x == 17) {
        } else if (x == 18) {  // Compliant
        } else {
        }
        if (x == 16) {
        } else if (x == 17) {
        } else if (x == 18) {
        } else if (x == 19) {
        }  // Noncompliant
    }
}