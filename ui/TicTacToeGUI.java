package ui;

import game.GameManager;
import exceptions.InvalidPlayerNameException;
import interfaces.GameInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeGUI extends JFrame implements ActionListener, GameInterface {
    private GameManager manager;
    private JButton[][] buttons = new JButton[3][3];
    private boolean playerXTurn = true;
    private int totalRounds, currentRound = 1, xScore = 0, oScore = 0;
    private JLabel statusLabel = new JLabel("", JLabel.CENTER);
    private JLabel scoreLabel = new JLabel("", JLabel.CENTER);

    public TicTacToeGUI() {
        try {
            setupGame();
        } catch (InvalidPlayerNameException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void setupGame() throws InvalidPlayerNameException {
        JTextField xField = new JTextField();
        JTextField oField = new JTextField();
        JSpinner roundSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Player X Name:"));
        panel.add(xField);
        panel.add(new JLabel("Player O Name:"));
        panel.add(oField);
        panel.add(new JLabel("Number of Rounds:"));
        panel.add(roundSpinner);

        int result = JOptionPane.showConfirmDialog(null, panel, "Game Setup",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) throw new InvalidPlayerNameException("Game setup was cancelled.");

        manager = new GameManager(xField.getText(), oField.getText(), (Integer) roundSpinner.getValue());
        totalRounds = manager.getTotalRounds();

        startGame();
    }

    @Override
    public void startGame() {
        setTitle("Tic Tac Toe - " + manager.getPlayerXName() + " vs " + manager.getPlayerOName());
        setLayout(new BorderLayout());

        JPanel gamePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gamePanel.setBackground(Color.GRAY);

        Font buttonFont = new Font("Arial", Font.BOLD, 60);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(buttonFont);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].addActionListener(this);
                gamePanel.add(buttons[i][j]);
            }
        }

        statusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        updateScoreLabel();

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());

        add(topPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(resetButton, BorderLayout.SOUTH);

        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        updateStatusLabel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        if (!buttonClicked.getText().equals("")) return;

        buttonClicked.setText(playerXTurn ? "X" : "O");

        if (checkWin()) {
            if (playerXTurn) xScore++; else oScore++;
            JOptionPane.showMessageDialog(this, (playerXTurn ? manager.getPlayerXName() : manager.getPlayerOName()) + " wins this round!");
            checkEndCondition();
        } else if (isDraw()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            checkEndCondition();
        } else {
            playerXTurn = !playerXTurn;
            updateStatusLabel();
        }
    }

    private void updateStatusLabel() {
        statusLabel.setText((playerXTurn ? manager.getPlayerXName() : manager.getPlayerOName()) + "'s Turn (" +
                (playerXTurn ? "X)" : "O)"));
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Round " + currentRound + "/" + totalRounds + " | " +
                manager.getPlayerXName() + ": " + xScore + " | " + manager.getPlayerOName() + ": " + oScore);
    }

    private boolean checkWin() {
        String symbol = playerXTurn ? "X" : "O";
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) &&
                buttons[i][1].getText().equals(symbol) &&
                buttons[i][2].getText().equals(symbol)) return true;

            if (buttons[0][i].getText().equals(symbol) &&
                buttons[1][i].getText().equals(symbol) &&
                buttons[2][i].getText().equals(symbol)) return true;
        }

        return (buttons[0][0].getText().equals(symbol) && buttons[1][1].getText().equals(symbol) && buttons[2][2].getText().equals(symbol)) ||
               (buttons[0][2].getText().equals(symbol) && buttons[1][1].getText().equals(symbol) && buttons[2][0].getText().equals(symbol));
    }

    private boolean isDraw() {
        for (JButton[] row : buttons)
            for (JButton b : row)
                if (b.getText().equals("")) return false;
        return true;
    }

    private void clearBoard() {
        for (JButton[] row : buttons)
            for (JButton b : row) {
                b.setText("");
                b.setEnabled(true);
            }
        playerXTurn = true;
    }

    private void checkEndCondition() {
        currentRound++;
        updateScoreLabel();
        if (currentRound > totalRounds) {
            if (xScore == oScore) {
                int choice = JOptionPane.showConfirmDialog(this, "It's a tie! Play one more round?", "Tie", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    totalRounds++;
                } else {
                    JOptionPane.showMessageDialog(this, "The match is a draw!");
                    resetGame();
                    return;
                }
            } else {
                String winner = xScore > oScore ? manager.getPlayerXName() : manager.getPlayerOName();
                JOptionPane.showMessageDialog(this, winner + " wins the match!");
                resetGame();
                return;
            }
        }
        clearBoard();
        updateStatusLabel();
    }

    @Override
    public void resetGame() {
        this.dispose();
        new TicTacToeGUI();
    }
}
