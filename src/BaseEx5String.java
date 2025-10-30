import java.util.Scanner;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class BaseEx5String {
    // shared scanner
    private static final Scanner sc = new Scanner((System.in));

    public static void main(String[] args) {
        // launch the interactive menu loop & close scanner on exit
        runMenu();
        sc.close();
    }

    //////////////////////// MENU ////////////////////////

    private static void runMenu() {
        // Loop until the user chooses quit
        boolean keepRunning = true;
        while (keepRunning) {
            System.out.println("\n=== Ex 5 : Strings ===");
            System.out.println("1) [5.1] Concaténer deux chaînes (plusieurs façons)");
            System.out.println("2) [5.2] Rechercher un mot dans une sentence (case-insensitive)");
            System.out.println("3) [5.3] Remplacer un mot trouvé par un autre (case-insensitive)");
            System.out.println("4) [5.4] Tester si une chaîne est un palindrome");
            System.out.println("5) [5.5] Tester avec \"Elu par cette crapule\"");
            System.out.println("0) Quitter");
            System.out.print("Choix : ");

            // Read user choice & route to the corresponding exercise
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> ex51ConcatWays(); // multiple concatenation
                case "2" -> ex52Search(); // find a word in a sentence (case/accent-insensitive)
                case "3" -> ex53Replace(); // replace a word with another
                case "4" -> ex54Palindrome(); // check if a string is a palindrome
                case "5" -> ex55SentencePal(); // test palindrome
                case "0" -> {
                    System.out.println("Bye...!!!");
                    keepRunning = false;
                    return; // Exit loop & end
                }
                default -> System.out.println("Choix invalide."); // safety for invalid inputs
            }
        }
    }

    //////////////////////// 5.1 CONCATENATION ////////////////////////

    /**
     * 5 ways to join strings in Java
     * 1) '+' : simple, 'null' becomes "null"
     * 2) String.concat : direct, but fails if 'null'
     * 3) StringBuilder : good for many appends (loops), 'null' becomes "null"
     * 4) String.format : readable templates, %s accepts 'null'
     * 5) String.join : handy with a separator fails if 'null'
     */
    // show original inputs with accents and visually decompose each concatenation
    // method
    private static void ex51ConcatWays() {
        System.out.print("Phrase 1: ");
        String a = sc.nextLine();
        System.out.print("Phrase 2: ");
        String b = sc.nextLine();

        System.out.println(" - phrase1 = \"" + a + "\"");
        System.out.println(" - phrase2 = \"" + b + "\"");

        // 1) '+' operator
        System.out.println("\n1) Avec l'opérateur '+' :");
        StringBuilder tmpPlus = new StringBuilder(); // step 1: create builder
        System.out.println("   step 1: new StringBuilder() -> \"" + tmpPlus.toString() + "\"");
        tmpPlus.append(a); // step 2: append a
        System.out.println("   step 2: append(a) -> \"" + tmpPlus.toString() + "\"");
        tmpPlus.append(b); // step 3: append b
        System.out.println("   step 3: append(b) -> \"" + tmpPlus.toString() + "\"");
        String plusResult = a + b; // final result
        System.out.println("   Resultat (a + b) -> \"" + plusResult + "\"");

        // 2) String.concat
        System.out.println("\n2) Avec String.concat :");

        String concatResult = a.concat(b);
        System.out.println("   a.concat(b) -> \"" + concatResult + "\"");
        System.out.println("   Lève une exception si un argument est 'null'.");

        // 3) StringBuilder
        System.out.println("\n3) Avec StringBuilder :");
        StringBuilder sb = new StringBuilder();
        System.out.println("   step 1: new StringBuilder() -> \"" + sb.toString() + "\"");
        sb.append(a);
        System.out.println("   step 2: append(a) -> \"" + sb.toString() + "\"");
        sb.append(b);
        System.out.println("   step 3: append(b) -> \"" + sb.toString() + "\"");
        String sbResult = sb.toString();
        System.out.println("   Resultat -> \"" + sbResult + "\"");

        // 4) String.format
        System.out.println("\n4) Avec String.format :");
        System.out.println("   Template: \"%s%s\"");
        System.out.println("   \"'a''b'\"");
        String formatResult = String.format("%s%s", a, b);
        System.out.println("   Resultat -> \"" + formatResult + "\"");

        // 5) String.join
        System.out.println("\n5) Avec String.join :");
        String joinResult = String.join("", a, b);
        System.out.println("   sans délimiteur -> \"" + joinResult + "\"");
        String joinVisible = String.join(" | ", a, b);
        System.out.println("   avec ' | ' pour délimiter -> \"" + joinVisible + "\"");
    }

    //////////////////////// 5.2 SEARCH ////////////////////////

    /**
     * Search a word in a sentence (case & accent insensitive)
     * - remove accents
     * - convert to lowercase
     * - match whole words only
     */
    private static void ex52Search() {
        System.out.print("Phrase : ");
        String sentence = sc.nextLine();
        System.out.print("Word to search: ");
        String word = sc.nextLine();

        String normalizedSentence = normalizeString(sentence);
        String normalizedWord = normalizeString(word);
        boolean found = Pattern.compile("\\b" + Pattern.quote(normalizedWord) + "\\b")
                .matcher(normalizedSentence)
                .find();

        System.out.println(found ? "Trouvé! : " + word : "Pas trouvé! : " + word);
    }

    ////////////////////////// 5.3 REPLACE WORD ////////////////////////

    /**
     * Replace a word in a sentence (case-insensitive, keep accents)
     * - split the sentence into words by space
     * - for each word :
     * - separate trailing punctuation if any
     * - compare word lowercased to target
     * - if it matches, replace with new word & keep punctuation
     * - join words
     */
    private static void ex53Replace() {
        System.out.print("Phrase : ");
        String sentence = sc.nextLine();
        System.out.print("Mot à remplacer : ");
        String target = sc.nextLine();
        System.out.print("Remplacer par : ");
        String replacement = sc.nextLine();

        // Split sentence into words by spaces
        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            // separate trailing punctuation
            int end = word.length();
            while (end > 0 && !Character.isLetterOrDigit(word.charAt(end - 1))) {
                end--;
            }
            String wordOnly = word.substring(0, end);
            String punct = word.substring(end);

            // compare lowercase to match case-insensitively
            if (wordOnly.equalsIgnoreCase(target)) {
                words[i] = replacement + punct; // replace & keep punctuation
            }

            // Join back into a single string
            String result = String.join(" ", words);
            System.out.println("→ " + result);
        }
    }

    ////////////////////////// 5.4 PALINDROME ////////////////////////
    /**
     * Check if a string is a palindrome (ignore case, accents, spaces,
     * punctuation)
     * - normalize the input :
     * - remove accents
     * - remove spaces
     * - remove punctuation
     * - convert to lowercase
     * - use two pointers: one from start, one from end
     * - compare characters while moving towards the middle
     * - if all pairs match → palindrome, else → not
     */
    private static void ex54Palindrome() {
        System.out.print("Chaîne à tester : ");
        String s = sc.nextLine();
        boolean pal = isPalindrome(s);

        System.out.println(pal ? "→ palindrome" : "→ pas palindrome");
    }

    ////////////////////////// 5.5 PALINDROME AGAIN ////////////////////////
    /**
     * Test palindrome : "Elu par cette crapule"
     *
     */
    private static void ex55SentencePal() {
        String s = "Elu par cette crapule";
        System.out.println("Test sur : \"" + s + "\"");
        boolean pal = isPalindrome(s);

        System.out.println(pal ? " C'est un palindrome" : "Ce n'est pas un palindrome");
    }

    ////////////////////////// Helpers ////////////////////////

    /**
     * Normalize string for case-insensitive search
     * Steps:
     * 1) NFD splits letters & diacritics :
     * "é" →Normalizer.Form.NFD → 'e' + 'accent'
     *
     * 2) Remove marks
     * 3) Lowercase for case-insensitive comparisons
     */
    private static String normalizeString(String s) {
        if (s == null)
            return "";
        // NFD decomposition: letters + diacritics
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        StringBuilder sb = new StringBuilder();
        for (char c : normalized.toCharArray()) {
            // keep only non-diacritic characters
            if (Character.getType(c) != Character.NON_SPACING_MARK) {
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase(); // lowercase for case-insensitive
    }

    /**
     * Normalize for palindrome :
     * - Drop accents + lowercase
     * - Keep only letters & digits (ignore spaces/punctuation)
     */
    private static String normalizeForPalindrome(String s) {
        String t = normalizeString(s); // lowercase + no accents
        StringBuilder sb = new StringBuilder();
        for (char c : t.toCharArray()) {
            if (Character.isLetterOrDigit(c)) { // keep letters & digits only
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Two-pointer palindrome :
     * - i starts at the beginning, j at the end
     * - move pointers inward while characters match
     * - if a mismatch occurs, it's not a palindrome
     */
    private static boolean isPalindrome(String s) {
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
