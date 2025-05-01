package util;

import model.Board;
import model.Space;
import java.util.*; 
import util.BoardTemplate;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Board board;
    private static final int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        board = null;

        while (true) {
            if (nonNull(board)) {
                System.out.println(getBoardFormattedTemplate());
            }

            System.out.println("Selecione uma das opções a seguir:");
            System.out.println("1- Iniciar um novo Jogo");
            System.out.println("2- Colocar um novo número");
            System.out.println("3- Remover um número");
            System.out.println("4- Visualizar jogo atual");
            System.out.println("5- Verificar status do jogo");
            System.out.println("6- Limpar jogo");
            System.out.println("7- Finalizar jogo");
            System.out.println("8- Sair");

            int option = runUntilGetValidNumber(1, 8);

            switch (option) {
                case 1 -> startGame();
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
            }
        }
    }

    private static String getBoardFormattedTemplate() {
        List<String> values = new ArrayList<>();

        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (int j = 0; j < BOARD_LIMIT; j++) {
                Space space = board.getSpaces().get(i).get(j);
                Integer actual = space.getActual();
                values.add(actual != null ? actual.toString() : " ");
            }
        }

        return String.format(BoardTemplate.BOARD_TEMPLATE, values.toArray());
    }

    private static void startGame() {
        List<List<Space>> spaces = new ArrayList<>();
        int[][] template = SudokuBoards.EASY_BOARD; // Usa o novo Sudoku padrão

        for (int i = 0; i < BOARD_LIMIT; i++) {
            List<Space> row = new ArrayList<>();
            for (int j = 0; j < BOARD_LIMIT; j++) {
                int value = template[i][j];
                boolean fixed = value != 0;
                row.add(new Space(value, fixed));
            }
            spaces.add(row);
        }

        board = new Board(spaces);
        System.out.println("O jogo foi iniciado com o tabuleiro padrão!");
    }

    private static void inputNumber() {
        if (isBoardNull()) return;

        System.out.println("Informe a linha (1 a 9):");
        int row = runUntilGetValidNumber(1, 9) - 1;

        System.out.println("Informe a coluna (1 a 9):");
        int col = runUntilGetValidNumber(1, 9) - 1;

        System.out.printf("Informe o número para a posição [%d, %d]:\n", row + 1, col + 1);
        int value = runUntilGetValidNumber(1, 9);

        if (!board.changeValue(row, col, value)) {
            System.out.printf("A posição [%d, %d] tem um valor fixo.\n", row + 1, col + 1);
        }
    }

    private static void removeNumber() {
        if (isBoardNull()) return;

        System.out.println("Informe a linha (1 a 9):");
        int row = runUntilGetValidNumber(1, 9) - 1;

        System.out.println("Informe a coluna (1 a 9):");
        int col = runUntilGetValidNumber(1, 9) - 1;

        if (!board.clearValue(row, col)) {
            System.out.printf("A posição [%d, %d] tem um valor fixo.\n", row + 1, col + 1);
        }
    }

    private static void showCurrentGame() {
        if (isBoardNull()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Seu jogo se encontra da seguinte forma:\n");

        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (int j = 0; j < BOARD_LIMIT; j++) {
                Space space = board.getSpaces().get(i).get(j);
                Integer actual = space.getActual();
                sb.append(actual != null ? actual : ".");
                sb.append(" ");
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }

    private static void showGameStatus() {
        if (isBoardNull()) return;

        if (board.isComplete()) {
            if (board.hasErrors()) {
                System.out.println("O jogo está completo, mas contém erros.");
            } else {
                System.out.println("Parabéns! Você completou o jogo corretamente.");
            }
        } else {
            System.out.println("O jogo atualmente se encontra no status Incompleto");
            if (board.hasErrors()) {
                System.out.println("O jogo contém erros.");
            } else {
                System.out.println("O jogo não contém erros.");
            }
        }
    }

    private static void clearGame() {
        if (isBoardNull()) return;

        System.out.println("Tem certeza que deseja limpar o jogo e perder todo seu progresso? (sim/não)");
        String confirm = scanner.next().trim().toLowerCase();

        confirm = java.text.Normalizer.normalize(confirm, java.text.Normalizer.Form.NFD)
                                      .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        while (!confirm.equals("sim") && !confirm.equals("nao")) {
            System.out.println("Informe 'sim' ou 'não'");
            confirm = scanner.next().trim().toLowerCase();
            confirm = java.text.Normalizer.normalize(confirm, java.text.Normalizer.Form.NFD)
                                          .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        }

        if (confirm.equals("sim")) {
            board.reset();
            System.out.println("O jogo foi limpo!");
        }
    }

    private static void finishGame() {
        if (isBoardNull()) return;

        if (board.gameIsFinished()) {
            System.out.println("Parabéns, você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros. Verifique e ajuste.");
        } else {
            System.out.println("Você ainda precisa preencher algum espaço.");
        }
    }

    private static boolean isBoardNull() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado.");
            return true;
        }
        return false;
    }

    private static int runUntilGetValidNumber(int min, int max) {
        while (true) {
            try {
                int current = scanner.nextInt();
                if (current >= min && current <= max) {
                    return current;
                } else {
                    System.out.printf("Informe um número entre %d e %d:\n", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número.");
                scanner.next();
            }
        }
    }
}
