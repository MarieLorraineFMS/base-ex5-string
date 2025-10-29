import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class BaseEx5String {

    private static final Scanner sc = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();
    private static boolean helpShownOnce = false; // flag for intro

    // --- CLI colors ---
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        runMenu();
        sc.close();
    }

    /////////////////////////////// MENU //////////////////////////////////////

    private static void runMenu() {
        if (!helpShownOnce) {
            printIntroHelp(); // show how to use once at start
            helpShownOnce = true;
        }

        while (true) {
            System.out.println("\n=== Ex 4 : Menu principal ===");
            System.out.println(
                    "1) [4.1] Démo : Parcourir un tableau de notes \n                Afficher la plus grande, la plus petite et la moyenne");
            System.out.println("2) [4.2] Saisir des élèves et leurs notes");
            System.out.println(students.isEmpty()
                    ? "3) [4.3] Rechercher un élève  (indisponible - Nécessite des élèves)"
                    : "3) [4.3] Rechercher un élève");
            System.out.println(students.isEmpty()
                    ? "4) [4.4] Trier les prénoms (desc puis asc)  (indisponible - Nécessite des élèves)"
                    : "4) [4.4] Trier les prénoms (desc puis asc)");
            System.out.println("5) [4.5] Visualiser le tri");
            System.out.println("9) Aide");
            System.out.println("0) Quitter");
            System.out.print("Choix : ");

            int choice = readIntAndCheckBounds(0, 9);// validate choice

            switch (choice) {
                case 1 -> demoNotes(); // 4.1
                case 2 -> addStudent(); // 4.2 (unlocks 4.3 & 4.4)
                case 3 -> {
                    if (students.isEmpty()) {
                        System.out.println("Saisir des élèves via l'option 2.");
                        break;
                    }
                    searchStudent(); // 4.3
                }
                case 4 -> {
                    if (students.isEmpty()) {
                        System.out.println("Saisir des élèves via l'option 2.");
                        break;
                    }
                    sortByFirstname(); // 4.4
                }
                case 5 -> runVisualBubbleSort(); // 4.5 (visual bubble sort)
                case 9 -> printIntroHelp(); // help
                case 0 -> {
                    System.out.println("Bye!!!");
                    return; // exit loop & close scanner
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    ////////////////////////////// HELP MESSAGE //////////////////////////////////
    private static void printIntroHelp() {
        System.out.println("""
                Aide :
                - 1) Lancer la démo 4.1 (min/max/moyenne) avec un tableau d'exemples.
                - 2) Saisir au moins un élève et ses notes pour débloquer 3) et 4).
                - 3) Recherche un élève par prénom + nom (si au moins 1 élève).
                - 4) Trie les prénoms en décroissant puis croissant (si au moins 1 élève).
                - 5) Explication step by step du tri à bulles.
                - 9) Aide. 0) Quitter.
                """);
    }

    /////////////////////////////// 4.1 DEMO //////////////////////////////////////

    private static void demoNotes() {
        System.out.println("\n[4.1] Démo sur tableau de notes {12, 9, 15, 7, 18}");
        int[] grades = { 12, 9, 15, 7, 18 }; // example array
        System.out.println("min = " + GradeStats.min(grades)); // smallest
        System.out.println("max = " + GradeStats.max(grades)); // largest
        System.out.println("avg = " + GradeStats.avg(grades)); // average
    }

    //////////////////////////////// 4.2 INPUT /////////////////////////////////////

    private static void addStudent() {
        System.out.println("\n[4.2] Saisie de l'identité des élèves et de leurs notes.");
        while (true) {
            System.out.print("Ajouter un élève ? (o/n) : ");
            String yesNo = sc.next().trim().toLowerCase();
            if (yesNo.equals("n"))
                break;
            if (!yesNo.equals("o")) {
                System.out.println("Réponse invalide.");
                continue;
            }
            System.out.print("Prénom : ");
            String firstname = sc.next();
            System.out.print("Nom : ");
            String lastname = sc.next();
            Student newStudent = new Student(firstname, lastname);
            System.out.println("Saisir les notes de l'élève (de 0 à 20), 'q' pour terminer :");
            while (true) {
                String userInput = sc.next().trim();
                if (userInput.equalsIgnoreCase("q"))
                    break; // this is the end

                Double parsedInput = parseDouble(userInput); // return Double | null if invalid
                if (parsedInput == null) {
                    System.out.println("Saisie invalide ignorée : " + userInput);
                    continue;
                }

                double grade = parsedInput;

                if (grade < 0.0 || grade > 20.0) {
                    System.out.println("Note invalide : 0.0 à 20.0.");
                    continue;
                }

                // store as double
                newStudent.grades.add(grade);
            }
            // show student summary
            System.out.printf("→ %s, moyenne = %.2f%n", newStudent.displayName(), newStudent.average());
            students.add(newStudent);
        }
    }

    //////////////////////////////// 4.3 SEARCH ///////////////////////////////

    private static void searchStudent() {
        System.out.println("\n[4.3] Recherche d’un élève.");
        System.out.print("Prénom à chercher : ");
        String firstnameQuery = sc.next();
        System.out.print("Nom à chercher : ");
        String lastnameQuery = sc.next().toUpperCase();

        Student found = null;
        for (Student student : students) {
            if (student.firstName.equalsIgnoreCase(firstnameQuery)
                    && student.lastName.equalsIgnoreCase(lastnameQuery)) {
                found = student;
                break;
            }
        }

        if (found == null) {
            System.out.println("Élève introuvable.");
        } else {
            System.out.println("Élève trouvé : " + found.displayName());
            System.out.println("Notes : " + found.grades);
            System.out.printf("Moyenne : %.2f%n", found.average());
        }
    }

    ////////////////////////////////// 4.4 SORT ///////////////////////////////////

    private static void sortByFirstname() {
        System.out.println("\n[4.4] Tri des prénoms (desc puis asc).");
        List<String> firstNames = new ArrayList<>();
        for (Student s : students)
            firstNames.add(s.firstName);

        // descending
        firstNames.sort(Comparator.reverseOrder());
        System.out.println("Prénoms (décroissant) : " + firstNames);

        // ascending
        firstNames.sort(Comparator.naturalOrder());
        System.out.println("Prénoms (croissant) : " + firstNames);
    }

    ///////////////////////////////// 4.5 VISUAL //////////////////////////////////

    private static void runVisualBubbleSort() {
        System.out.println("\n[4.5] Visualisation du tri à bulles (tableau d'exemple)");
        int[] table = { -5, 2, -8, 31, 15, 4 };

        System.out.print("Avant tri : ");
        displayTableInline(table);

        // delay between step
        System.out.print("Delai entre les étapes en ms : ");
        int delay = readIntAndCheckBounds(0, 10000);

        // visual bubble sort
        fonctionVisualSort(table, delay);

        System.out.print("Après tri  : ");
        displayTableInline(table);
    }

    static void fonctionVisualSort(int[] table, int delayMs) {
        int pass = 1;

        for (int i = 0; i < table.length; i++) {
            for (int j = 1; j < table.length - i; j++) {
                // print comparison before potential swap
                printStep(table, j - 1, j, false, pass);

                if (table[j - 1] > table[j]) {
                    // swap
                    int tmp = table[j - 1];
                    table[j - 1] = table[j];
                    table[j] = tmp;

                    // show result after swap
                    printStep(table, j - 1, j, true, pass);
                }

                pause(delayMs); // small delay to make steps readable by normally constitued humans
            }
            System.out.println(); // blank line between passes
            pass++;
        }
    }

    private static void printStep(int[] table, int a, int b, boolean swapped, int pass) {
        // highlight compared indices [a] and [b] in yellow
        StringBuilder stringToBuild = new StringBuilder();
        stringToBuild.append("Passe ").append(pass).append(" : ");

        for (int i = 0; i < table.length; i++) {
            if (i == a || i == b) {
                stringToBuild.append(YELLOW).append("[").append(table[i]).append("]").append(RESET).append(" ");
            } else {
                stringToBuild.append(table[i]).append(" ");
            }
        }
        if (swapped) {
            stringToBuild.append(RED).append("<-> SWAP").append(RESET);
        } else {
            stringToBuild.append(CYAN).append(" - ").append(RESET);
        }
        System.out.println(stringToBuild);
    }

    ///////////////////////////////// HELPERS //////////////////////////////////

    private static int readIntAndCheckBounds(int min, int max) {
        while (true) {
            String line = sc.nextLine().trim(); // remove spaces
            try {
                int parsedLine = Integer.parseInt(line);
                if (parsedLine < min || parsedLine > max) {
                    System.out.print("Saisir un nombre entre " + min + " et " + max + " : ");
                    continue;
                }
                return parsedLine;
            } catch (NumberFormatException err) {
                System.out.print("Saisir un nombre valide. ");
            }
        }
    }

    private static void displayTableInline(int[] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(tab[i] + " ");
        }
        System.out.println();
    }

    private static void pause(int ms) {
        if (ms <= 0)
            return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    private static Double parseWithLocale(String stringToParse, Locale locale) {
        // try-with-ressources to auto-close temporary scan
        try (Scanner tmp = new Scanner(stringToParse).useLocale(locale)) {
            if (!tmp.hasNextDouble())
                return null;

            double value = tmp.nextDouble();

            if (tmp.hasNext())
                return null;

            return value;
        }
    }

    // parse input as double, accepting comma or dot & returns null if not a number
    private static Double parseDouble(String userInput) {
        if (userInput == null)
            return null;
        String trimmedString = userInput.trim();

        if (trimmedString.isEmpty())
            return null;

        // try French locale
        Double fr = parseWithLocale(trimmedString, Locale.FRANCE);
        if (fr != null)
            return fr;

        // fallback : US locale
        return parseWithLocale(trimmedString, Locale.US);
    }

}
