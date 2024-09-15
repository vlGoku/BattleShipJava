import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BattleshipGame extends JFrame {
    private Player player;
    private Player computer;
    private JButton[][] playerButtons;
    private JButton[][] computerButtons;
    private int boardSize = 10;
    private boolean placingShips = true;
    private List<Ship> shipsToPlacePlayer = List.of(new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2));
    private List<Ship> shipsToPlaceComputer = List.of(new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2));
    private int currentShipIndexPlayer = 0;
    private int currentShipIndexComputer = 0;
    private boolean horizontal = true;
    private JTextArea infoTextArea;

    public BattleshipGame() {
        GameBoard gameboard = new GameBoard(boardSize);
        player = new Player(gameboard);
        computer = new Player(gameboard);

        setTitle("Schiffe versenken");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        Border blackline = BorderFactory.createLineBorder(Color.black);

        JPanel playerPanel = new JPanel(new GridLayout(boardSize, boardSize));
        JPanel computerPanel = new JPanel(new GridLayout(boardSize, boardSize));
        JPanel controlPanel = new JPanel(new GridLayout(1, 2));
        JLabel overviewLabel = new JLabel("Vertical", JLabel.CENTER);
        controlPanel.setSize(200,200);
        overviewLabel.setBorder(blackline);

        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoTextArea);

        JPanel bottomRightPanel = new JPanel(new BorderLayout());
        bottomRightPanel.add(scrollPane, BorderLayout.CENTER);


        playerButtons = new JButton[boardSize][boardSize];
        computerButtons = new JButton[boardSize][boardSize];

        placeComputerShip();
        Color lightBlue = new Color(173,216,230);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                playerButtons[i][j] = new JButton();
                playerButtons[i][j].setBackground(lightBlue);
                int finalI = i;
                int finalJ = j;
                playerButtons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (placingShips) {
                            placePlayerShip(finalI, finalJ);
                        }
                    }
                });
                playerPanel.add(playerButtons[i][j]);

                computerButtons[i][j] = new JButton();
                computerButtons[i][j].setBackground(lightBlue);
                computerButtons[i][j].setEnabled(false);
                computerPanel.add(computerButtons[i][j]);

                final int x = i;
                final int y = j;

                computerButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!placingShips) {
                            if (computerButtons[x][y].getBackground() == Color.RED || computerButtons[x][y].getBackground() == Color.BLUE) {
                                return;
                            }
                            if (player.takeShot(x, y, false)) {
                                computerButtons[x][y].setBackground(Color.RED);
                            } else {
                                computerButtons[x][y].setBackground(Color.BLUE);
                            }

                            int[] computerShot = computer.getRandomShot();

                            while (playerButtons[computerShot[0]][computerShot[1]].getBackground() == Color.RED || playerButtons[computerShot[0]][computerShot[1]].getBackground() == Color.BLUE) {
                                computerShot = computer.getRandomShot();
                            }

                            if (computer.takeShot(computerShot[0], computerShot[1], true)) {
                                playerButtons[computerShot[0]][computerShot[1]].setBackground(Color.RED);
                            } else {
                                playerButtons[computerShot[0]][computerShot[1]].setBackground(Color.BLUE);
                            }
                            checkEnd();
                        }
                    }
                });
            }
        }

        add(playerPanel);
        add(computerPanel);
        add(controlPanel);
        add(scrollPane, BorderLayout.SOUTH);

        JButton orientationButton = new JButton("Rotate");
        orientationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                horizontal = !horizontal;
                System.out.println(horizontal);
                overviewLabel.setText(horizontal ? "Vertical" : "Horizontal");
            }
        });
        controlPanel.add(orientationButton);
        controlPanel.add(overviewLabel);
    }

    private void placePlayerShip(int x, int y) {
        if (currentShipIndexPlayer < shipsToPlacePlayer.size()) {
            Ship currentShip = shipsToPlacePlayer.get(currentShipIndexPlayer);
            if (isPlacementValid(x, y, currentShip, horizontal, true)) {
                if (player.getGameboard().placeShip(currentShip, x, y, horizontal, true)) {
                    for (int i = 0; i < currentShip.getLength(); i++) {
                        if (horizontal) {
                            playerButtons[x + i][y].setBackground(Color.GREEN);
                            currentShip.addPositionPlaced((x + i), y);
                        } else {
                            playerButtons[x][y + i].setBackground(Color.GREEN);
                            currentShip.addPositionPlaced(x, (y + i));
                        }
                    }
                    currentShipIndexPlayer++;
                    if (currentShipIndexPlayer == shipsToPlacePlayer.size()) {
                        placingShips = false;
                        for (JButton[] row : computerButtons) {
                            for (JButton button : row) {
                                button.setEnabled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeComputerShip(){
        Random rand = new Random();
        int randRotation;
        boolean rotation;

        for(Ship ship : shipsToPlaceComputer){
            int x, y, z;
            randRotation = rand.nextInt(1, 3);
            if(randRotation == 1){
                rotation = true;
            } else {
                rotation = false;
            }

            do {
                x = rand.nextInt(1, 10);
                y = rand.nextInt(1, 10);
                z = 1;
            } while (!isPlacementValid(x, y, ship, rotation, false));

            for(int i = 0; i < ship.getLength(); i++){
                if(rotation) {
                    if (x + i > 9) {
                        computer.getGameboard().placeShip(ship, (x - z), y, rotation, false);
                        ship.addPositionPlaced((x - z), y);
                        z++;
                    } else {
                        computer.getGameboard().placeShip(ship, (x + i), y, rotation, false);
                        ship.addPositionPlaced((x + i), y);
                    }
                } else {
                    if (y + i > 9) {
                        computer.getGameboard().placeShip(ship, x, (y - z), rotation, false);
                        ship.addPositionPlaced(x, (y - z));
                        z++;
                    } else {
                        computer.getGameboard().placeShip(ship, x, (y + i), rotation, false);
                        ship.addPositionPlaced(x, (y + i));
                    }
                }
            }
        }
    }

    public boolean isPlacementValid(int x, int y, Ship ship, boolean horizontal, boolean placementOnPlayer) {
        char[][] grid = player.getGameboard().getGrid(placementOnPlayer);
        for (int i = 0; i < ship.getLength(); i++) {
            if (horizontal) {
                if (x + i >= grid.length || grid[x + i][y] != ' ') {
                    return false;
                }
            } else {
                if (y + i >= grid[0].length || grid[x][y + i] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkEnd(){
        boolean allShipsPlayerSunk = true;
        boolean allShipsComputerSunk = true;
        for (Ship ship : player.getGameboard().getShipsPlayer()) {
            if (!ship.isSunk()) {
                allShipsPlayerSunk = false;
                //break;
            } else if (!ship.isSunkNotified()) {
                infoTextArea.append("Oh nein! Dein Schiff wurde vom Computer versenkt.\n");
                ship.setSunkNotified(true);
            }
        }
        for (Ship ship : computer.getGameboard().getShipsComputer()) {
            if (!ship.isSunk()) {
                allShipsComputerSunk = false;
                //break;
            } else if (!ship.isSunkNotified()) {
                infoTextArea.append("Super! Du hast ein Schiff des Computers versenkt.\n");
                ship.setSunkNotified(true);
            }
        }

        if (allShipsPlayerSunk) {
            JOptionPane.showMessageDialog(this, "Du hast verloren! Alle deine Schiffe wurden versenkt.");
            System.exit(0);
        }

        if (allShipsComputerSunk) {
            JOptionPane.showMessageDialog(this, "Herzlichen Glückwunsch! Du hast alle Schiffe des Computers versenkt.");
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BattleshipGame().setVisible(true);
            }
        });
    }
}

