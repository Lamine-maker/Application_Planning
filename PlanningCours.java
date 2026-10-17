import java.util.Arrays;
import java.util.Scanner;

/**
 * Application de programmation des cours (Java, C, Python) du lundi au samedi, le matin.
 * Règle : un même cours (donc un même professeur) ne peut pas avoir lieu deux jours de suite.
 *
 * Style procédural : la classe sert uniquement de conteneur (exigence du langage Java).
 * Elle ne contient que des constantes et des méthodes statiques : aucun objet métier.
 *
 * Représentation des données :
 *  - un cours est identifié par son indice dans COURS (0 = Java, 1 = C, 2 = Python)
 *  - PROFS[i] est le professeur du cours COURS[i] (tableaux parallèles)
 *  - le planning est un tableau de 6 entiers : planning[jour] = indice du cours, ou VIDE
 */
public class PlanningCours {

    // =====================================================================
    // CONSTANTES
    // =====================================================================

    static final String[] JOURS = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
    static final String[] COURS = {"Java", "C", "Python"};
    static final String[] PROFS = {"M. X", "M. Y", "M. A"};

    static final int NB_JOURS = JOURS.length;   // 6
    static final int NB_COURS = COURS.length;   // 3
    static final int VIDE = -1;                 // jour sans cours programmé

    // =====================================================================
    // RÈGLE MÉTIER
    // =====================================================================

    /** Vrai si ce cours peut être placé ce jour-là : il ne doit être ni la veille ni le lendemain. */
    static boolean estPlacementValide(int[] planning, int jour, int cours) {
        if (jour > 0 && planning[jour - 1] == cours) {
            return false;
        }
        if (jour < NB_JOURS - 1 && planning[jour + 1] == cours) {
            return false;
        }
        return true;
    }

    // =====================================================================
    // DONNÉES
    // =====================================================================

    static int[] creerPlanningVide() {
        int[] planning = new int[NB_JOURS];
        Arrays.fill(planning, VIDE);
        return planning;
    }

    // =====================================================================
    // AFFICHAGE
    // =====================================================================

    static void afficherMenu() {
        System.out.println();
        System.out.println("===== PLANNING DES COURS =====");
        System.out.println("1. Choisir un planning");
        System.out.println("2. Afficher le planning");
        System.out.println("3. Afficher les professeurs");
        System.out.println("4. Modifier un jour");
        System.out.println("5. Quitter");
    }

    static void afficherListeCours() {
        for (int i = 0; i < NB_COURS; i++) {
            System.out.println("  " + (i + 1) + ". " + COURS[i] + " (" + PROFS[i] + ")");
        }
    }

    static void afficherListeJours() {
        for (int i = 0; i < NB_JOURS; i++) {
            System.out.println("  " + (i + 1) + ". " + JOURS[i]);
        }
    }

    static void afficherPlanning(int[] planning) {
        System.out.println();
        System.out.printf("%-10s | %-8s | %s%n", "Jour", "Cours", "Professeur");
        System.out.println("-----------+----------+-----------");
        for (int jour = 0; jour < NB_JOURS; jour++) {
            if (planning[jour] == VIDE) {
                System.out.printf("%-10s | %-8s | %s%n", JOURS[jour], "(libre)", "-");
            } else {
                System.out.printf("%-10s | %-8s | %s%n",
                        JOURS[jour], COURS[planning[jour]], PROFS[planning[jour]]);
            }
        }
    }

    /** Affiche chaque professeur, son cours et les jours où il intervient dans le planning actuel. */
    static void afficherProfesseurs(int[] planning) {
        System.out.println();
        System.out.printf("%-8s | %-8s | %s%n", "Prof", "Cours", "Jours");
        System.out.println("---------+----------+------------------------");
        for (int prof = 0; prof < NB_COURS; prof++) {   // le professeur i enseigne le cours i
            String jours = "";
            for (int jour = 0; jour < NB_JOURS; jour++) {
                if (planning[jour] == prof) {
                    if (!jours.isEmpty()) {
                        jours += ", ";
                    }
                    jours += JOURS[jour];
                }
            }
            if (jours.isEmpty()) {
                jours = "-";
            }
            System.out.printf("%-8s | %-8s | %s%n", PROFS[prof], COURS[prof], jours);
        }
    }

    // =====================================================================
    // SAISIE
    // =====================================================================

    /** Redemande la saisie tant que ce n'est pas un entier compris entre min et max. */
    static int lireEntier(Scanner clavier, String message, int min, int max) {
        while (true) {
            System.out.print(message);
            String ligne = clavier.nextLine().trim();
            try {
                int valeur = Integer.parseInt(ligne);
                if (valeur >= min && valeur <= max) {
                    return valeur;
                }
            } catch (NumberFormatException e) {
                // saisie non numérique : on passe au message d'erreur ci-dessous
            }
            System.out.println("Saisie invalide : entrez un nombre entre " + min + " et " + max + ".");
        }
    }

    // =====================================================================
    // ACTIONS DU MENU
    // =====================================================================

    /** Option 1 : l'utilisateur choisit le cours de chacun des 6 jours, en respectant la règle. */
    static void choisirPlanning(Scanner clavier, int[] planning) {
        Arrays.fill(planning, VIDE);
        System.out.println();
        System.out.println("Choisissez le cours de chaque jour (le même cours ne peut pas être donné deux jours de suite) :");
        afficherListeCours();
        for (int jour = 0; jour < NB_JOURS; jour++) {
            boolean accepte = false;
            while (!accepte) {
                int cours = lireEntier(clavier, JOURS[jour] + " : ", 1, NB_COURS) - 1;
                if (estPlacementValide(planning, jour, cours)) {
                    planning[jour] = cours;
                    accepte = true;
                } else {
                    System.out.println("Refusé : " + COURS[cours]
                            + " ne peut pas être donné deux jours de suite. Choisissez un autre cours.");
                }
            }
        }
        System.out.println("Planning enregistré.");
        afficherPlanning(planning);
    }

    /** Option 4 : change le cours d'un seul jour ; refusé si la règle n'est plus respectée. */
    static void modifierJour(Scanner clavier, int[] planning) {
        System.out.println();
        afficherListeJours();
        int jour = lireEntier(clavier, "Jour à modifier : ", 1, NB_JOURS) - 1;
        afficherListeCours();
        int cours = lireEntier(clavier, "Nouveau cours : ", 1, NB_COURS) - 1;
        if (estPlacementValide(planning, jour, cours)) {
            planning[jour] = cours;
            System.out.println("Modification effectuée.");
            afficherPlanning(planning);
        } else {
            System.out.println("Refusé : " + COURS[cours]
                    + " serait donné deux jours de suite. Le planning n'a pas changé.");
        }
    }

    // =====================================================================
    // PROGRAMME PRINCIPAL
    // =====================================================================

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        int[] planning = creerPlanningVide();
        boolean continuer = true;

        while (continuer) {
            afficherMenu();
            int choix = lireEntier(clavier, "Votre choix : ", 1, 5);

            switch (choix) {
                case 1:
                    choisirPlanning(clavier, planning);
                    break;
                case 2:
                    afficherPlanning(planning);
                    break;
                case 3:
                    afficherProfesseurs(planning);
                    break;
                case 4:
                    modifierJour(clavier, planning);
                    break;
                case 5:
                    continuer = false;
                    System.out.println("Au revoir.");
                    break;
                default:
                    break;
            }
        }
        clavier.close();
    }
}
