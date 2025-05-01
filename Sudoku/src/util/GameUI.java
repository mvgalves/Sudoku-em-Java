package util;

import model.Board;
import model.Space;
import model.GameStatusEnum;
import util.SudokuBoards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class GameUI extends JFrame {

    private Board board;
    private final JTextField[][] cells = new JTextField[9][9];
    private final JLabel statusLabel = new JLabel("Status: Não iniciado");
    private String dificuldadeAtual = "Nenhuma";
    private boolean jogoIniciado = false;

    public GameUI() {
        setTitle("Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(600, 700);
        setLocationRelativeTo(null);

        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        boardPanel.setBackground(Color.BLACK);

        // Criando a grade de campos de texto
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                cells[i][j] = cell;
                boardPanel.add(cell);

                final int row = i;
                final int col = j;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        highlightConflicts(row, col);
                    }
                });
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("Novo Jogo");
        JButton checkButton = new JButton("Verificar");
        JButton clearButton = new JButton("Limpar");
        JButton finishButton = new JButton("Finalizar");

        controlPanel.add(newGameButton);
        controlPanel.add(checkButton);
        controlPanel.add(clearButton);
        controlPanel.add(finishButton);

        add(controlPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        // Ações dos botões
        newGameButton.addActionListener(e -> startGame());
        checkButton.addActionListener(e -> checkGame());
        clearButton.addActionListener(e -> clearGame());
        finishButton.addActionListener(e -> finishGame());
    }

    private void startGame() {
        int option = JOptionPane.showOptionDialog(this,
            "Escolha a dificuldade:",
            "Dificuldade",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Fácil", "Médio", "Difícil"},
            "Fácil"
        );

        if (option == -1) return; // Usuário cancelou

        int[][] template;
        if (option == 0) {
            template = SudokuBoards.EASY_BOARD;
            dificuldadeAtual = "Fácil";
        } else if (option == 1) {
            template = SudokuBoards.MEDIUM_BOARD;
            dificuldadeAtual = "Médio";
        } else {
            template = SudokuBoards.HARD_BOARD;
            dificuldadeAtual = "Difícil";
        }

        board = new Board(new ArrayList<>());

        for (int i = 0; i < 9; i++) {
            ArrayList<Space> row = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                int value = template[i][j];
                boolean fixed = value != 0;
                row.add(new Space(value, fixed));
            }
            board.getSpaces().add(row);
        }

        refreshBoard();
        jogoIniciado = true;
        atualizarStatus();
    }

    private void atualizarStatus() {
        if (jogoIniciado) {
            String cor;
            if ("Fácil".equals(dificuldadeAtual)) {
                cor = "#008000"; // Verde
            } else if ("Médio".equals(dificuldadeAtual)) {
                cor = "#FFFF00"; // Amarelo
            } else {
                cor = "#FF0000"; // Vermelho
            }

            String texto = String.format(
                "<html>Status: Jogo iniciado - Dificuldade: <span style='color:%s;'>%s</span></html>",
                cor,
                dificuldadeAtual
            );
            statusLabel.setText(texto);
            statusLabel.setForeground(Color.BLACK);
        } else {
            statusLabel.setText("Status: Não iniciado");
            statusLabel.setForeground(Color.RED);
        }
    }

    private void refreshBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Space space = board.getSpaces().get(i).get(j);
                JTextField cell = cells[i][j];
                if (space.isFixed()) {
                    cell.setText(String.valueOf(space.getExpected()));
                    cell.setEditable(false);
                    cell.setBackground(Color.LIGHT_GRAY);
                } else {
                    cell.setText(space.getActual() != null ? String.valueOf(space.getActual()) : "");
                    cell.setEditable(true);
                    cell.setBackground(Color.WHITE);
                }
            }
        }
    }

    private void updateBoardFromUI() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Space space = board.getSpaces().get(i).get(j);
                if (!space.isFixed()) {
                    String text = cells[i][j].getText();
                    if (text.isEmpty()) {
                        space.clearSpace();
                    } else {
                        try {
                            int value = Integer.parseInt(text);
                            if (value >= 1 && value <= 9) {
                                space.setActual(value);
                            } else {
                                space.clearSpace();
                            }
                        } catch (NumberFormatException e) {
                            space.clearSpace();
                        }
                    }
                }
            }
        }
    }

    private void checkGame() {
        if (board == null) {
            JOptionPane.showMessageDialog(this, "Nenhum jogo iniciado!");
            return;
        }
        updateBoardFromUI();
        if (board.hasErrors()) {
            JOptionPane.showMessageDialog(this, "Existem erros no tabuleiro.");
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum erro detectado.");
        }
    }

    private void clearGame() {
        if (board == null) return;
        for (List<Space> row : board.getSpaces()) {
            for (Space space : row) {
                if (!space.isFixed()) {
                    space.clearSpace();
                }
            }
        }
        refreshBoard();
        statusLabel.setText("Status: Jogo limpo - Dificuldade: " + dificuldadeAtual);
        statusLabel.setForeground(new Color(255, 165, 0)); // Laranja
    }

    private void finishGame() {
        if (board == null) return;
        updateBoardFromUI();
        if (board.gameIsFinished()) {
            JOptionPane.showMessageDialog(this, "Parabéns! Você finalizou o Sudoku!");
            board = null;
            refreshBoard();
            statusLabel.setText("Status: Jogo finalizado");
            statusLabel.setForeground(new Color(0, 0, 255)); // Azul
        } else {
            JOptionPane.showMessageDialog(this, "O jogo ainda não foi finalizado corretamente.");
        }
    }

    // Novo método para destacar linha, coluna e quadrante
    private void highlightConflicts(int selectedRow, int selectedCol) {
        if (board == null) return;
    
        // Resetar cores
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Space space = board.getSpaces().get(i).get(j);
                JTextField cell = cells[i][j];
                if (space.isFixed()) {
                    cell.setBackground(Color.LIGHT_GRAY);
                } else {
                    cell.setBackground(Color.WHITE);
                }
            }
        }
    
        // Cor para destacar
        Color highlightColor = new Color(245, 245, 220); // Bege clarinho
    
        // Destacar linha e coluna
        for (int i = 0; i < 9; i++) {
            if (!board.getSpaces().get(selectedRow).get(i).isFixed()) {
                cells[selectedRow][i].setBackground(highlightColor);
            }
            if (!board.getSpaces().get(i).get(selectedCol).isFixed()) {
                cells[i][selectedCol].setBackground(highlightColor);
            }
        }
    
        // Destacar quadrante
        int startRow = (selectedRow / 3) * 3;
        int startCol = (selectedCol / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (!board.getSpaces().get(i).get(j).isFixed()) {
                    cells[i][j].setBackground(highlightColor);
                }
            }
        }
    
        // Não precisamos mais mudar a célula clicada para branco!
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameUI ui = new GameUI();
            ui.setVisible(true);
        });
    }
}
