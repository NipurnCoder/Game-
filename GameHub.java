import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class GameHub extends JFrame {
    public GameHub() {
        setTitle("Advanced Java Game Hub (10 Games)");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(createButton("1) Number Guessing", e -> new NumberGuessingGame()));
        add(createButton("2) Bird Flying", e -> new BirdFlyingGame()));
        add(createButton("3) TicTacToe", e -> new TicTacToeGame()));
        add(createButton("4) Pong", e -> new PongGame()));
        add(createButton("5) Snake", e -> new SnakeGame()));
        add(createButton("6) Brick Breaker", e -> new BrickBreakerGame()));
        add(createButton("7) Minesweeper", e -> new MinesweeperGame()));
        add(createButton("8) Space Quiz", e -> new QuizGame()));
        add(createButton("9) Memory Match", e -> new MemoryGame()));
        add(createButton("10) Reaction Test", e -> new ReactionGame()));

        setVisible(true);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.addActionListener(action);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameHub::new);
    }
}

class TicTacToeGame extends JFrame implements ActionListener {
    private JButton[] cells = new JButton[9];
    private char currentPlayer = 'X';
    private JLabel status = new JLabel("Turn: X");

    public TicTacToeGame() {
        setTitle("Tic Tac Toe");
        setSize(350, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel board = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            cells[i] = new JButton(" ");
            cells[i].setFont(new Font("Arial", Font.BOLD, 40));
            cells[i].addActionListener(this);
            board.add(cells[i]);
        }

        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> resetBoard());

        add(status, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(restart, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void resetBoard() {
        for (JButton cell : cells) {
            cell.setText(" ");
            cell.setEnabled(true);
        }
        currentPlayer = 'X';
        status.setText("Turn: " + currentPlayer);
    }

    private boolean isWinner(char p) {
        int[][] wins = { {0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6} };
        for (int[] line : wins) {
            if (cells[line[0]].getText().charAt(0) == p &&
                cells[line[1]].getText().charAt(0) == p &&
                cells[line[2]].getText().charAt(0) == p) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        b.setText(String.valueOf(currentPlayer));
        b.setEnabled(false);

        if (isWinner(currentPlayer)) {
            status.setText("Winner: " + currentPlayer);
            for (JButton c : cells) c.setEnabled(false);
            return;
        }

        boolean draw = true;
        for (JButton c : cells) if (c.getText().equals(" ")) draw = false;

        if (draw) {
            status.setText("Draw!");
            return;
        }

        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        status.setText("Turn: " + currentPlayer);
    }
}

class PongGame extends JFrame {
    public PongGame() {
        setTitle("Pong");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new PongPanel());
        setVisible(true);
    }

    class PongPanel extends JPanel implements ActionListener, KeyListener {
        int paddleHeight = 80;
        int paddleWidth = 12;
        int leftY = 200;
        int rightY = 200;
        int ballX = 350, ballY = 250, ballDX = 4, ballDY = 4;
        int leftScore = 0, rightScore = 0;
        Timer timer = new Timer(15, this);

        PongPanel() {
            setBackground(Color.BLACK);
            setFocusable(true);
            addKeyListener(this);
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, leftY, paddleWidth, paddleHeight);
            g.fillRect(getWidth()-paddleWidth, rightY, paddleWidth, paddleHeight);
            g.fillOval(ballX-10, ballY-10, 20, 20);
            g.drawString("Score: " + leftScore + " - " + rightScore, getWidth()/2-30, 20);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ballX += ballDX;
            ballY += ballDY;
            if (ballY < 0 || ballY > getHeight()) ballDY = -ballDY;

            if (ballX < paddleWidth && ballY > leftY && ballY < leftY + paddleHeight) ballDX = -ballDX;
            if (ballX > getWidth()-paddleWidth && ballY > rightY && ballY < rightY + paddleHeight) ballDX = -ballDX;

            if (ballX < 0) { rightScore++; resetBall(); }
            if (ballX > getWidth()) { leftScore++; resetBall(); }
            repaint();
        }

        void resetBall() {
            ballX = getWidth()/2;
            ballY = getHeight()/2;
            ballDX = (new Random().nextBoolean() ? 4 : -4);
            ballDY = (new Random().nextBoolean() ? 4 : -4);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_W && leftY > 0) leftY -= 20;
            if (e.getKeyCode() == KeyEvent.VK_S && leftY < getHeight()-paddleHeight) leftY += 20;
            if (e.getKeyCode() == KeyEvent.VK_UP && rightY > 0) rightY -= 20;
            if (e.getKeyCode() == KeyEvent.VK_DOWN && rightY < getHeight()-paddleHeight) rightY += 20;
        }

        @Override public void keyReleased(KeyEvent e) {}
        @Override public void keyTyped(KeyEvent e) {}
    }
}

class SnakeGame extends JFrame {
    public SnakeGame() {
        setTitle("Snake");
        setSize(400, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new SnakePanel());
        setVisible(true);
    }

    class SnakePanel extends JPanel implements ActionListener, KeyListener {
        final int TILE_SIZE = 20;
        final int WIDTH = 18;
        final int HEIGHT = 18;
        final int DELAY = 120;

        LinkedList<Point> snake = new LinkedList<>();
        Point food;
        char dir = 'R';
        javax.swing.Timer timer = new javax.swing.Timer(DELAY, this);
        boolean running = true;

        SnakePanel() {
            setPreferredSize(new Dimension(WIDTH*TILE_SIZE, HEIGHT*TILE_SIZE));
            setBackground(Color.BLACK);
            setFocusable(true);
            addKeyListener(this);
            initGame();
            timer.start();
        }

        void initGame() {
            snake.clear();
            snake.add(new Point(5, 5));
            snake.add(new Point(4, 5));
            snake.add(new Point(3, 5));
            placeFood();
            running = true;
        }

        void placeFood() {
            Random r = new Random();
            do {
                food = new Point(r.nextInt(WIDTH), r.nextInt(HEIGHT));
            } while (snake.contains(food));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (running) {
                g.setColor(Color.RED);
                g.fillOval(food.x*TILE_SIZE, food.y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.GREEN);
                for (Point p : snake) g.fillRect(p.x*TILE_SIZE, p.y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
            } else {
                g.setColor(Color.WHITE);
                g.drawString("Game Over - Press ENTER to restart", 20, getHeight()/2);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) move();
            repaint();
        }

        void move() {
            Point head = new Point(snake.getFirst());
            switch (dir) {
                case 'L' -> head.x--;
                case 'R' -> head.x++;
                case 'U' -> head.y--;
                case 'D' -> head.y++;
            }

            if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT || snake.contains(head)) {
                running = false; return;
            }

            snake.addFirst(head);
            if (head.equals(food)) placeFood(); else snake.removeLast();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int kc = e.getKeyCode();
            if (kc == KeyEvent.VK_LEFT && dir != 'R') dir = 'L';
            if (kc == KeyEvent.VK_RIGHT && dir != 'L') dir = 'R';
            if (kc == KeyEvent.VK_UP && dir != 'D') dir = 'U';
            if (kc == KeyEvent.VK_DOWN && dir != 'U') dir = 'D';
            if (kc == KeyEvent.VK_ENTER && !running) initGame();
        }

        @Override public void keyReleased(KeyEvent e) {}
        @Override public void keyTyped(KeyEvent e) {}
    }
}

class BrickBreakerGame extends JFrame {
    public BrickBreakerGame() {
        setTitle("Brick Breaker");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new BreakerPanel());
        setVisible(true);
    }

    class BreakerPanel extends JPanel implements KeyListener, ActionListener {
        int paddleX = 200;
        int ballX = 240, ballY = 350, ballDX = -3, ballDY = -4;
        javax.swing.Timer timer = new javax.swing.Timer(10, this);
        boolean play = true;
        Brick[] bricks;

        BreakerPanel() {
            setFocusable(true);
            addKeyListener(this);
            buildBricks();
            timer.start();
        }

        void buildBricks() {
            bricks = new Brick[35];
            int idx = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    bricks[idx++] = new Brick(j*65+20, i*25+40);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);
            g.setColor(Color.GREEN);
            g.fillRect(paddleX, 520, 100, 10);
            g.setColor(Color.WHITE);
            g.fillOval(ballX, ballY, 15, 15);
            for (Brick brick : bricks) if (!brick.broken) {
                g.setColor(Color.ORANGE);
                g.fillRect(brick.x, brick.y, brick.width, brick.height);
            }
            if (!play) {
                g.setColor(Color.RED);
                g.drawString("Game Over! Press R", 180, 300);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (play) {
                ballX += ballDX; ballY += ballDY;

                if (ballX < 0 || ballX > getWidth()-15) ballDX = -ballDX;
                if (ballY < 0) ballDY = -ballDY;

                if (new Rectangle(ballX, ballY, 15, 15).intersects(new Rectangle(paddleX, 520, 100, 10))) ballDY = -ballDY;

                for (Brick b : bricks) {
                    if (!b.broken && new Rectangle(ballX, ballY, 15, 15).intersects(new Rectangle(b.x, b.y, b.width, b.height))) {
                        b.broken = true; ballDY = -ballDY; break;
                    }
                }

                if (ballY > 570) play = false;
                boolean allBroken = true;
                for (Brick b: bricks) if (!b.broken) { allBroken = false; break; }
                if (allBroken) { play = false; }
            }
            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && paddleX > 0) paddleX -= 25;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddleX < getWidth()-100) paddleX += 25;
            if (e.getKeyCode() == KeyEvent.VK_R) {
                play = true; ballX = 240; ballY = 350; ballDX = -3; ballDY = -4; buildBricks();
            }
        }

        @Override public void keyReleased(KeyEvent e) {}
        @Override public void keyTyped(KeyEvent e) {}

        class Brick { int x, y, width = 60, height = 20; boolean broken = false;
            Brick(int x, int y) { this.x = x; this.y = y; }
        }
    }
}

class MinesweeperGame extends JFrame {
    public MinesweeperGame() {
        setTitle("Minesweeper");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new MinesweeperPanel());
        setVisible(true);
    }

    class MinesweeperPanel extends JPanel {
        private final int rows = 8, cols = 8, mines = 10;
        private Cell[][] grid;
        private boolean started = false;
        private boolean gameover = false;

        MinesweeperPanel() {
            setLayout(new GridLayout(rows, cols));
            setup();
        }

        void setup() {
            removeAll();
            gameover = false;
            grid = new Cell[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    grid[r][c] = new Cell(r, c);
                    add(grid[r][c]);
                }
            }
            revalidate();
            repaint();
        }

        void placeMines(int startR, int startC) {
            Random r = new Random();
            int placed = 0;
            while (placed < mines) {
                int rr = r.nextInt(rows), cc = r.nextInt(cols);
                if ((rr==startR && cc==startC) || grid[rr][cc].isMine) continue;
                grid[rr][cc].isMine = true;
                placed++;
            }
            for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) grid[i][j].count = countNeighbours(i,j);
        }

        int countNeighbours(int r, int c) {
            int cnt = 0;
            for (int dr=-1; dr<=1; dr++) for (int dc=-1; dc<=1; dc++) {
                int nr=r+dr,nc=c+dc;
                if (nr>=0 && nr<rows && nc>=0 && nc<cols && grid[nr][nc].isMine) cnt++;
            }
            return cnt;
        }

        void revealCell(Cell cell) {
            if (cell.revealed || cell.flagged || gameover) return;
            cell.reveal();
            if (cell.isMine) { gameover=true; revealAll(); JOptionPane.showMessageDialog(this,"Boom! Game over."); return; }
            if (cell.count==0) for (int dr=-1; dr<=1; dr++) for (int dc=-1; dc<=1; dc++) {
                int nr=cell.r+dr,nc=cell.c+dc;
                if (nr>=0 && nr<rows && nc>=0 && nc<cols) revealCell(grid[nr][nc]);
            }
            if(checkWin()) { gameover=true; JOptionPane.showMessageDialog(this, "You won!"); }
        }

        void revealAll(){ for(int r=0;r<rows;r++)for(int c=0;c<cols;c++)revealCell(grid[r][c]); }

        boolean checkWin(){
            for(int r=0;r<rows;r++) for(int c=0;c<cols;c++) {
                if (!grid[r][c].isMine && !grid[r][c].revealed) return false;
            }
            return true;
        }

        class Cell extends JButton {
            int r,c,count;
            boolean isMine=false, revealed=false, flagged=false;
            Cell(int r,int c){ this.r=r; this.c=c; setPreferredSize(new Dimension(45,45));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameover) return;
                        if (!started) { placeMines(r,c); started=true; }
                        if (SwingUtilities.isRightMouseButton(e)) { toggleFlag(); }
                        else { revealCell(Cell.this); }
                    }
                });
            }

            void toggleFlag(){ if (revealed) return; flagged=!flagged; setText(flagged?"F":" "); }

            void reveal(){
                if (revealed) return;
                revealed=true;
                setEnabled(false);
                if (isMine) setText("*");
                else if (count>0) setText(String.valueOf(count));
            }
        }
    }
}

class QuizGame extends JFrame {
    public QuizGame() {
        setTitle("Space Quiz");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new QuizPanel());
        setVisible(true);
    }

    class QuizPanel extends JPanel {
        String[] questions = {
            "What planet is known as the Red Planet?",
            "What is the speed of light (approx km/s)?",
            "Which galaxy contains the Earth?"
        };
        String[][] options = {
            {"Mars","Venus","Jupiter","Saturn"},
            {"300000","150000","100000","1000"},
            {"Andromeda","Milky Way","Triangulum","Whirlpool"}
        };
        int[] answers = {0,0,1};
        int current = 0, score = 0;
        JLabel questionLabel = new JLabel();
        JButton[] optButtons = new JButton[4];

        QuizPanel() {
            setLayout(new BorderLayout());
            questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            add(questionLabel, BorderLayout.NORTH);

            JPanel optionsPanel = new JPanel(new GridLayout(2,2,10,10));
            for (int i = 0; i < 4; i++) {
                optButtons[i] = new JButton();
                int idx = i;
                optButtons[i].addActionListener(e -> evaluate(idx));
                optionsPanel.add(optButtons[i]);
            }
            add(optionsPanel, BorderLayout.CENTER);
            loadQuestion();
        }

        void loadQuestion() {
            if (current >= questions.length) {
                JOptionPane.showMessageDialog(this, "Quiz over! Score: " + score + " / " + questions.length);
                dispose();
                return;
            }
            questionLabel.setText(questions[current]);
            for (int i = 0; i < 4; i++) optButtons[i].setText(options[current][i]);
        }

        void evaluate(int chosen) {
            if (chosen == answers[current]) score++;
            current++;
            loadQuestion();
        }
    }
}

class MemoryGame extends JFrame {
    public MemoryGame() {
        setTitle("Memory Match");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new MemoryPanel());
        setVisible(true);
    }

    class MemoryPanel extends JPanel {
        final int PAIRS = 8;
        CardButton first, second;
        int matches = 0;

        MemoryPanel() {
            setLayout(new GridLayout(4,4,5,5));
            java.util.List<Integer> values = new ArrayList<>();
            for (int i=0; i<PAIRS; i++) { values.add(i); values.add(i); }
            Collections.shuffle(values);
            for (int v : values) {
                CardButton btn = new CardButton(v);
                btn.addActionListener(e -> cardClicked(btn));
                add(btn);
            }
        }

        void cardClicked(CardButton btn) {
            if (btn.revealed || second != null) return;
            btn.reveal();
            if (first == null) first = btn;
            else {
                second = btn;
                if (first.value == second.value) {
                    first = null; second = null; matches++;
                    if (matches == PAIRS) JOptionPane.showMessageDialog(this, "You matched all!");
                } else {
                    javax.swing.Timer t = new javax.swing.Timer(500, e -> {
                        first.conceal(); second.conceal();
                        first = null; second = null;
                    });
                    t.setRepeats(false);
                    t.start();
                }
            }
        }

        class CardButton extends JButton {
            int value;
            boolean revealed;

            CardButton(int value) {
                this.value=value;
                setText("?");
                setFont(new Font("Arial", Font.BOLD, 24));
            }

            void reveal() { revealed=true; setText(String.valueOf(value)); }
            void conceal() { revealed=false; setText("?"); }
        }
    }
}

class ReactionGame extends JFrame {
    public ReactionGame() {
        setTitle("Reaction Test");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new ReactionPanel());
        setVisible(true);
    }

    class ReactionPanel extends JPanel {
        JLabel status = new JLabel("Wait for GO...", SwingConstants.CENTER);
        long startTime;

        ReactionPanel() {
            setLayout(new BorderLayout());
            status.setFont(new Font("Arial", Font.BOLD, 24));
            add(status, BorderLayout.CENTER);
            JButton start = new JButton("Start");
            start.addActionListener(e -> startTest());
            add(start, BorderLayout.SOUTH);

            addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE && startTime > 0) {
                        long diff = System.currentTimeMillis() - startTime;
                        status.setText("Reaction: " + diff + " ms");
                        startTime = 0;
                    }
                }
            });
            setFocusable(true);
            requestFocusInWindow();
        }

        void startTest() {
            status.setText("Ready...");
            javax.swing.Timer timer = new javax.swing.Timer(1000 + new Random().nextInt(2000), e -> {
                status.setText("GO! Press SPACE");
                startTime = System.currentTimeMillis();
                ((javax.swing.Timer)e.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}

class NumberGuessingGame extends JFrame {
    private int number;
    private JTextField input;
    private JLabel status;
    private int attempts = 0;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        number = new Random().nextInt(100) + 1;

        status = new JLabel("Guess number (1-100)", SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.BOLD, 16));

        input = new JTextField();
        JButton guessBtn = new JButton("Guess");

        guessBtn.addActionListener(e -> checkGuess());

        add(status, BorderLayout.NORTH);
        add(input, BorderLayout.CENTER);
        add(guessBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(input.getText());
            attempts++;

            if (guess < number) {
                status.setText("Too Low! Attempts: " + attempts);
            } else if (guess > number) {
                status.setText("Too High! Attempts: " + attempts);
            } else {
                status.setText("Correct! Attempts: " + attempts);
                JOptionPane.showMessageDialog(this, "You Won!");
                resetGame();
            }

        } catch (Exception e) {
            status.setText("Enter valid number!");
        }

        input.setText("");
    }

    private void resetGame() {
        number = new Random().nextInt(100) + 1;
        attempts = 0;
        status.setText("New Game! Guess (1-100)");
    }
}

class BirdFlyingGame extends JFrame {
    public BirdFlyingGame() {
        setTitle("Bird Flying Game");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new GamePanel());
        setVisible(true);
    }

    class GamePanel extends JPanel implements ActionListener, KeyListener {
        int birdY = 300;
        int velocity = 0;
        int gravity = 1;

        int pipeX = 400;
        int gapY = 200;
        int score = 0;

        Timer timer = new Timer(20, this);
        boolean gameOver = false;

        GamePanel() {
            setBackground(Color.CYAN);
            setFocusable(true);
            addKeyListener(this);
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.YELLOW);
            g.fillOval(80, birdY, 30, 30);

            g.setColor(Color.GREEN);
            g.fillRect(pipeX, 0, 50, gapY);
            g.fillRect(pipeX, gapY + 150, 50, getHeight());

            g.setColor(Color.BLACK);
            g.drawString("Score: " + score, 10, 20);

            if (gameOver) {
                g.drawString("Game Over! Press SPACE", 120, 300);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                velocity += gravity;
                birdY += velocity;
                pipeX -= 5;

                if (pipeX < -50) {
                    pipeX = getWidth();
                    gapY = new Random().nextInt(300) + 50;
                    score++;
                }

                if (birdY < 0 || birdY > getHeight() ||
                    (pipeX < 110 && pipeX > 50 &&
                    (birdY < gapY || birdY > gapY + 150))) {
                    gameOver = true;
                }
            }
            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (gameOver) {
                    birdY = 300;
                    velocity = 0;
                    pipeX = 400;
                    score = 0;
                    gameOver = false;
                } else {
                    velocity = -10;
                }
            }
        }

        @Override public void keyReleased(KeyEvent e) {}
        @Override public void keyTyped(KeyEvent e) {}
    }
}










// class GamePanel extends JPanel implements ActionListener, KeyListener {

//     int birdY = 300;
//     int velocity = 0;
//     int gravity = 1;

//     int pipeX = 400;
//     int gapY = 200;
//     int score = 0;

//     Timer timer = new Timer(20, this);
//     boolean gameOver = false;

//     Image birdImg, bgImg;

//     GamePanel() {
//         setFocusable(true);
//         addKeyListener(this);

//         // Load Images
//         birdImg = new ImageIcon("bird.png").getImage();
//         bgImg = new ImageIcon("bg.png").getImage();

//         timer.start();
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);

//         // Draw Background
//         g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);

//         // Draw Bird
//         g.drawImage(birdImg, 80, birdY, 40, 40, this);

//         // Draw Pipes (Better Look)
//         g.setColor(new Color(34, 139, 34));
//         g.fillRect(pipeX, 0, 60, gapY);
//         g.fillRect(pipeX, gapY + 150, 60, getHeight());

//         // Pipe Borders
//         g.setColor(Color.BLACK);
//         g.drawRect(pipeX, 0, 60, gapY);
//         g.drawRect(pipeX, gapY + 150, 60, getHeight());

//         // Score
//         g.setFont(new Font("Arial", Font.BOLD, 20));
//         g.drawString("Score: " + score, 10, 30);

//         // Game Over
//         if (gameOver) {
//             g.setFont(new Font("Arial", Font.BOLD, 25));
//             g.drawString("Game Over!", 120, 250);
//             g.drawString("Press SPACE", 110, 300);
//         }
//     }

//     @Override
//     public void actionPerformed(ActionEvent e) {
//         if (!gameOver) {
//             velocity += gravity;
//             birdY += velocity;
//             pipeX -= 5;

//             if (pipeX < -60) {
//                 pipeX = getWidth();
//                 gapY = new Random().nextInt(300) + 50;
//                 score++;
//             }

//             // Collision
//             if (birdY < 0 || birdY > getHeight() ||
//                 (pipeX < 120 && pipeX > 40 &&
//                 (birdY < gapY || birdY > gapY + 150))) {
//                 gameOver = true;
//             }
//         }
//         repaint();
//     }

//     @Override
//     public void keyPressed(KeyEvent e) {
//         if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//             if (gameOver) {
//                 birdY = 300;
//                 velocity = 0;
//                 pipeX = 400;
//                 score = 0;
//                 gameOver = false;
//             } else {
//                 velocity = -10;
//             }
//         }
//     }

//     @Override public void keyReleased(KeyEvent e) {}
//     @Override public void keyTyped(KeyEvent e) {}
// }