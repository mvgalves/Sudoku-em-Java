// package util;

// import javax.swing.*;
// import java.awt.*;
// import java.util.ArrayList;
// import java.util.List;

// import model.Board;
// import model.Space;

// public class SudokuUI extends JFrame {

//     private static final int SIZE = 9;
//     private Board board;

//     public SudokuUI(Board board) {
//         this.board = board;  // Inicializa o board com a instância recebida
//         setTitle("Sudoku");
//         setLayout(new GridLayout(SIZE, SIZE));  // 9x9 grid
//         initializeUI();  // Inicializa a interface gráfica
//     }

//     private void initializeUI() {
//         for (int i = 0; i < SIZE; i++) {
//             for (int j = 0; j < SIZE; j++) {
//                 try {
//                     Space space = board.getSpaces().get(i).get(j);  // Obtém a célula específica do board
//                     JButton button = new JButton(space.getActual() == null ? " " : space.getActual().toString());
//                     button.setFont(new Font("Arial", Font.PLAIN, 20));
//                     button.setBackground(space.isFixed() ? Color.LIGHT_GRAY : Color.WHITE);

//                     // Torna as variáveis i e j finais para serem usadas no ActionListener
//                     final int finalI = i;
//                     final int finalJ = j;
//                     button.addActionListener(e -> onCellClicked(finalI, finalJ));  // Ação para quando a célula for clicada

//                     add(button);  // Adiciona o botão à interface
//                 } catch (Exception e) {
//                     System.err.println("Erro ao inicializar a célula na posição (" + i + ", " + j + "): " + e.getMessage());
//                 }
//             }
//         }
//     }

//     private void onCellClicked(int row, int col) {
//         if (board.getSpaces().get(row).get(col).isFixed()) {
//             JOptionPane.showMessageDialog(this, "Esta célula é fixa, não pode ser alterada!");
//         } else {
//             String input = JOptionPane.showInputDialog(this, "Digite um número (1-9):");
//             try {
//                 int value = Integer.parseInt(input);
//                 if (value >= 1 && value <= 9) {
//                     board.changeValue(row, col, value);
//                     updateBoard();
//                 } else {
//                     JOptionPane.showMessageDialog(this, "Número inválido! Informe um número de 1 a 9.");
//                 }
//             } catch (NumberFormatException ex) {
//                 JOptionPane.showMessageDialog(this, "Entrada inválida!");
//             }
//         }
//     }

//     private void updateBoard() {
//         for (int i = 0; i < SIZE; i++) {
//             for (int j = 0; j < SIZE; j++) {
//                 Space space = board.getSpaces().get(i).get(j);
//                 Component component = getComponent(i * SIZE + j);  // Obtemos o componente

//                 // Verificamos se o componente é um JButton antes de fazer o cast
//                 if (component instanceof JButton) {
//                     JButton button = (JButton) component;
//                     button.setText(space.getActual() == null ? " " : space.getActual().toString());
//                 } else {
//                     System.err.println("Componente não é um JButton: " + component.getClass().getName());
//                 }
//             }
//         }
//     }

//     public static void main(String[] args) {
//         List<List<Space>> spaces = new ArrayList<>();
//         for (int i = 0; i < SIZE; i++) {
//             List<Space> row = new ArrayList<>();
//             for (int j = 0; j < SIZE; j++) {
//                 row.add(new Space(0, false)); // Inicializando com valores padrão
//             }
//             spaces.add(row);
//         }
//         Board board = new Board(spaces);
//         SudokuUI sudokuUI = new SudokuUI(board);
//         sudokuUI.setSize(500, 500);
//         sudokuUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         sudokuUI.setVisible(true);
//     }
// }
