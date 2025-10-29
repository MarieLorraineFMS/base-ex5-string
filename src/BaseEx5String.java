import java.util.Scanner;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseEx5String {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        runMenu();
        sc.close();
    }

    //////////////////////// MENU ////////////////////////

    private static void runMenu() {
        while (true) {
            System.out.println("\n=== Ex 5 : Les Strings ===");
            System.out.println("1) [5.1] Concaténer deux chaînes (plusieurs façons)");
            System.out.println("2) [5.2] Rechercher un mot dans une phrase (case-insensitive)");
            System.out.println("3) [5.3] Remplacer un mot trouvé par un autre (case-insensitive)");
            System.out.println("4) [5.4] Tester si une chaîne est un palindrome");
            System.out.println("5) [5.5] Tester avec \"Elu par cette crapule\"");
            System.out.println("0) Quitter");
            System.out.print("Choix : ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> ex51ConcatWays();
                case "2" -> ex52Search();
                case "3" -> ex53Replace();
                case "4" -> ex54Palindrome();
                case "5" -> ex55PhrasePal();
                case "0" -> {
                    System.out.println("Bye...!!!");
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    //////////////////////// 5.1 /////////////////////////////////////

    // Show different ways to concatenate strings
    private static void ex51ConcatWays() {
        System.out.print("Chaîne 1 : ");
        String a = sc.nextLine();
        System.out.print("Chaîne 2 : ");
        String b = sc.nextLine();

        // + operator
        String plus = a + b; // simplest

        // String.concat
        String concat = a.concat(b); // null-safe only if 'a' non-null

        // StringBuilder (best for loops / many appends)
        String builder = new StringBuilder().append(a).append(b).toString();

        // String.format
        String formatted = String.format("%s%s", a, b);

        // String.join
        String joined = String.join("", a, b);

        System.out.println("\n==> Résultats (mêmes contenus) :");
        System.out.println("1) a + b           = " + plus);
        System.out.println("2) a.concat(b)     = " + concat);
        System.out.println("3) StringBuilder   = " + builder);
        System.out.println("4) String.format   = " + formatted);
        System.out.println("5) String.join     = " + joined);
    }

    ////////////////////////// 5.2 ////////////////////////
    // Search a word in a sentence (case-insensitive, accent-insensitive)
    private static void ex52Search() {
        System.out.print("Phrase : ");
        String phrase = sc.nextLine();
        System.out.print("Mot à chercher : ");
        String word = sc.nextLine();

        // normalize both sides, remove accents, lowercase
        String normPhrase = normalizeForSearch(phrase);
        String normWord = normalizeForSearch(word);

        // use word boundary to avoid partial matches
        Pattern p = Pattern.compile("\\b" + Pattern.quote(normWord) + "\\b");
        boolean found = p.matcher(normPhrase).find();

        System.out.println(found ? "→ trouvé !" : "→ non trouvé.");
    }

    ////////////////////////// 5.3 ////////////////////////
    // Replace a word with another if found (case-insensitive & accents kept from
    // original text)
    private static void ex53Replace() {
        System.out.print("Phrase : ");
        String phrase = sc.nextLine();
        System.out.print("Mot à remplacer : ");
        String target = sc.nextLine();
        System.out.print("Remplacer par : ");
        String replacement = sc.nextLine();

        // build a case-insensitive regex with word boundaries
        // note: does not handle accent-insensitive replacement perfectly; keeps it
        String regex = "\\b(?i)" + Pattern.quote(target) + "\\b";
        Pattern pat = Pattern.compile(regex);
        Matcher m = pat.matcher(phrase);
        String result = m.replaceAll(replacement);

        System.out.println("→ " + result);
    }

    ////////////////////////// 5.4 ////////////////////////
    // Palindrome check ignoring case, spaces, punctuation & accents
    private static void ex54Palindrome() {
        System.out.print("Chaîne à tester : ");
        String s = sc.nextLine();
        boolean pal = isPalindromeFlexible(s);
        System.out.println(pal ? "→ palindrome" : "→ pas palindrome");
    }

    ////////////////////////// 5.5 ////////////////////////
    // Test with "Elu par cette crapule"
    private static void ex55PhrasePal() {
        String s = "Elu par cette crapule";
        System.out.println("Test sur : \"" + s + "\"");
        boolean pal = isPalindromeFlexible(s);
        System.out.println(pal ? "→ palindrome (oui)" : "→ pas palindrome");
    }

    ////////////////////////// Helpers ////////////////////////

    // normalize string for accent/case-insensitive search
    private static String normalizeForSearch(String s) {
        if (s == null)
            return "";
        String noAccent = Normalizer
                .normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // remove diacritics
        return noAccent.toLowerCase();
    }

    // remove accents + non-alphanumerics
    // + lowercase
    private static String normalizeForPalindrome(String s) {
        String t = normalizeForSearch(s); // lowercase + no accents
        return t.replaceAll("[^a-z0-9]", ""); // keep only letters/digits
    }

    // two-pointer palindrome on normalized content
    private static boolean isPalindromeFlexible(String s) {
        String t = normalizeForPalindrome(s);
        int i = 0, j = t.length() - 1;
        while (i < j) {
            if (t.charAt(i) != t.charAt(j))
                return false;
            i++;
            j--;
        }
        return true;
    }
}
