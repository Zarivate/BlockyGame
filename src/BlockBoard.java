import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Project Description:
 * This program creates a board of multicolored tiles and allows the user to alter
 * the board in certain ways. It achieves the board through the use of the Java Gui methods
 * while the position altering is done through the use of nodes and arrays alongside various
 * other methods
 *
 *@Author Ivan Zarate
 *CSCI 340
 *Section 001*
 *Assignment 1 Blocky Game
 *Known Bugs: None
 */

public class BlockBoard extends JFrame {

    int maxDepth = 5; // Class variable to set how many layers of blocks there can be
    Node root;        // Root node is created as the base of the board

    /**
     * Constructor used to create the main object
     */

    BlockBoard () {
        root = new Node(0);
        add(root);
    }

    /**
     * Class that creates the nodes used in the class. Also assigns their color, position
     * and whether or not they are made into 4 smaller squares. JPanel is extended here
     * as we are utilizing it's methods to create the board.
     */

    public class Node extends JPanel {

        Node [] [] child; // Double node array created called "child" to be altered later

        Color color; // Create a color variable
        Color [] setColors = new Color [] {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW}; // Create and array of Colors

        int depth; // Global variable for the depth of the board

        /**
         * Inner class that decides what color to set a tile to and whether or not it should be smashed
         * @param nodeDepth is the depth of the node sent in
         */

        Node (int nodeDepth) {

            Random rand = new Random();

            this.depth = nodeDepth; // Set the depth equal to the parameter sent in
            color = setColors[(int) (Math.random() * 4)]; // Assign the color
            setBackground(this.color); // Set the background as the assigned color above
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            double randDouble = rand.nextDouble();

            if (Math.exp(-.25 * nodeDepth) > randDouble) { // If statement that randomly runs to decide when a
                smash();                                   // tile should be smashed
            }
        }

        /**
         * Method that smashes a tile into 4 smaller tiles. Checks to see if it is already smashed or if
         * it is already at the max possible depth before doing so. If so then no smashing is done and
         * instead a message is printed saying section is already smashed.
         */

        public void smash() {

            this.depth++; // Update the depth

            this.child = new Node[2][2]; // Sets the child node at the top equal to a new node we create here

            // Loop to check if the node already has children, if so prints statement else continues
            if (child [0] [0] != null) {
                System.out.println("Error, it's already smashed my man.");
                return;
            }

            // Checks to see if the max depth had already been reached or not
            if (this.depth < maxDepth)
                for (int row = 0; row < 2; row++) { // Double for loop to be used when assigned correct array position
                    for (int column = 0; column < 2; column++) {
                        Node inner = new Node (depth); // New node is created and sent to node method with updated depth
                        this.child[row] [column] = inner; // Sets the child node position equal to the inner node
                        add(child[row] [column]); // Adds it to the child array
                        setBackground(Color.darkGray);
                    }
                }

            setLayout(new GridLayout(2,2));
            revalidate(); // Repaints the board
        }

        /**
         * Class to rotate the board clockwise. A temporary double array node
         * is created in the class then filled in accordingly with the corresponding positions
         * to get the right movement.
         */

        public void rotateClockwise() {

            if ((child == null)) {
                System.out.println("This move is not possible, please choose another move");
                return;
            }

            else {
                Node[][] temp = new Node[2][2];

                temp[0][0] = this.child[1][0];
                temp[1][0] = this.child[1][1];
                temp[1][1] = this.child[0][1];
                temp[0][1] = this.child[0][0];

                this.child = temp;

                updateTime();
            }
        }

        /**
         * Class to rotate board counter clockwise. A temporary double array node
         * is created in the class then filled in accordingly with the corresponding positions
         * to get the right movement.
         */

        public void rotateCounterclockwise() {

            if ((child == null)) {
                System.out.println("This move is not possible, please choose another move");
                return;
            }

            else {
                Node[][] temp = new Node[2][2];

                temp[0][0] = this.child[0][1];
                temp[0][1] = this.child[1][1];
                temp[1][1] = this.child[1][0];
                temp[1][0] = this.child[0][0];

                this.child = temp;

                updateTime();
            }
        }
        /**
         * Class to flip the left and right sides of the board. A temporary double array node
         * is created in the class then filled in accordingly with the corresponding positions
         * to get the right movement.
         */

        public void flipLeftAndRight () {

            if ((child == null)) {
                System.out.println("This move is not possible, please choose another move");
                return;
            }

            else {
                Node[][] temp = new Node[2][2];

                temp[0][0] = this.child[0][1];
                temp[0][1] = this.child[0][0];
                temp[1][0] = this.child[1][1];
                temp[1][1] = this.child[1][0];

                this.child = temp;

                updateTime();
            }
        }
        /**
         * Method that flips the top and bottom positions of the board. A temporary double array node
         * is created in the class then filled in accordingly with the corresponding positions
         * to get the right movement.
         */

        public void flipTopAndBottom() {

            // Checks to see if there is anything in the position first, if not then no change is done

            if (child == null) {
                System.out.println("This move is not possible, please choose another move");
                return;
            }
            else {
                Node[][] temp = new Node[2][2]; // Create a temporary double node array

                // Sets the corresponding positions to make proper change equal to one another
                temp[0][0] = this.child[1][0];
                temp[0][1] = this.child[1][1];
                temp[1][0] = this.child[0][0];
                temp[1][1] = this.child[0][1];

                this.child = temp; // Sets the child node array equal to the one we created in the method

                updateTime(); // Calls the update method
            }
        }

        /**
         * Method that updates the board. Is called at the end of every altering method
         * to save time and space.
         */

        public void updateTime() {
            removeAll();    // First removes all the subpanels from the board

            // adds them back
            add(this.child [0] [0]);
            add(this.child [0] [1]);
            add(this.child [1] [0]);
            add(this.child [1] [1]);

            revalidate(); // Repaints the board
        }
    }

    /**
     * Main that loops and checks to see if what the user put in is valid. If so continues to ask them
     * for more input until they type in the command to end the game. A try catch block is also utilized
     * to catch any other possible exceptions/invalid node inputs from the user. Else if it's valid it checks to
     * see which method it corresponds to and subsequently sends it to it.
     * @param args
     */

        public static void main(String[] args) {

            // Create a visible blockboard object to display
            BlockBoard blockGame = new BlockBoard();
            blockGame.setSize(500, 500);
            blockGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            blockGame.setVisible(true);

            // Print out a text menu for the user
            System.out.println("The following commands available to alter the board are" +
                    "\nrc = rotate clockwise" +
                    "\nrcc = rotate counter clockwise" +
                    "\nflr = flip left right" +
                    "\nftb = flip top and bottom" +
                    "\ns = smashes into 4 more blocks" +
                    "\nfollowed by coordinates if desired such as" +
                    "\nrc 0 0 0 1 which rotates clockwise the lower right quadrant of the upper left one" +
                    "\nend = ends the game");

            System.out.println("Enter your move");
            Scanner in = new Scanner(System.in);
            String cmdLine = in.nextLine().toUpperCase();

            Scanner cmdScanner = new Scanner(cmdLine);
            String cmd = cmdScanner.next();

            while (!cmd.equals("END")) {
                Node block = blockGame.root;
                try {
                    while (cmdScanner.hasNextInt()) {
                        int r = cmdScanner.nextInt();
                        int c = cmdScanner.nextInt();
                        block = block.child[r][c];
                    }

                    if (cmd.equals("RC")) {
                        block.rotateClockwise();
                    } else if (cmd.equals("RCC")) {
                        block.rotateCounterclockwise();
                    } else if (cmd.equals("FLR")) {
                        block.flipLeftAndRight();
                    } else if (cmd.equals("FTB")) {
                        block.flipTopAndBottom();
                    } else if (cmd.equals("S")) {
                        block.smash();
                    } else
                        System.out.println("Invalid try typing something else ");
                } catch (Exception ie) {
                    System.out.println("No Good");
                }
                System.out.println("Enter a command:");
                cmdLine = in.nextLine().toUpperCase();
                cmdScanner = new Scanner(cmdLine);
                cmd = cmdScanner.next();
            }
        }
    }