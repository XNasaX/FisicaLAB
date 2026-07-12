package modulos;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class OcioJuegaAprendiendo extends JFrame {

    private final CardLayout cards;
    private final JPanel root;
    private final ScoreStore scoreStore;
    private final StartPanel startPanel;
    private final AngryLearningPanel gamePanel;

    public OcioJuegaAprendiendo() {

        super("ocio juegaaprendiendo");

        scoreStore = new ScoreStore("record.txt");
        cards = new CardLayout();
        root = new JPanel(cards);
        startPanel = new StartPanel(this, scoreStore);
        gamePanel = new AngryLearningPanel(this, scoreStore);

        root.add(startPanel, "menu");
        root.add(gamePanel, "game");

        add(root);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public void showMenu() {

        startPanel.refresh();
        cards.show(root, "menu");
    }

    public void startGame() {

        gamePanel.resetGame();
        cards.show(root, "game");
        gamePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {

        new OcioJuegaAprendiendo().setVisible(true);
    }
}

class StartPanel extends JPanel {

    private final OcioJuegaAprendiendo frame;
    private final ScoreStore scoreStore;
    private final JLabel recordLabel;

    StartPanel(
            OcioJuegaAprendiendo frame,
            ScoreStore scoreStore
    ) {

        this.frame = frame;
        this.scoreStore = scoreStore;
        setLayout(null);
        setBackground(new Color(90, 180, 235));

        JLabel title = new JLabel("ROCA CONTRA TORRES", JLabel.CENTER);
        title.setBounds(160, 130, 700, 70);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("JetBrains Mono", Font.BOLD, 42));
        add(title);

        recordLabel = new JLabel("", JLabel.CENTER);
        recordLabel.setBounds(310, 235, 400, 40);
        recordLabel.setForeground(new Color(25, 45, 70));
        recordLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        add(recordLabel);

        JButton startButton = new JButton("ocio juegaaprendiendo");
        startButton.setBounds(330, 335, 360, 62);
        startButton.setBackground(new Color(255, 170, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setFont(new Font("JetBrains Mono", Font.BOLD, 19));
        startButton.addActionListener(e -> frame.startGame());
        add(startButton);

        JLabel help = new JLabel(
                "<html><center>Arrastra la roca hacia atras, suelta y derriba la estructura.<br>"
                + "Tienes exactamente 5 rocas por intento.</center></html>",
                JLabel.CENTER
        );
        help.setBounds(230, 445, 560, 90);
        help.setForeground(new Color(30, 60, 80));
        help.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        add(help);

        refresh();
    }

    void refresh() {

        recordLabel.setText("Record historico: " + scoreStore.getHighScore());
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(80, 160, 80));
        g2.fillRect(0, 620, getWidth(), 160);

        g2.setColor(new Color(255, 255, 255, 80));
        g2.fillOval(90, 90, 160, 55);
        g2.fillOval(700, 80, 220, 70);
        g2.fillOval(110, 360, 230, 75);
    }
}

class AngryLearningPanel extends JPanel {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int GROUND_Y = 650;
    private static final int MAX_ROCKS = 5;

    private final OcioJuegaAprendiendo frame;
    private final ScoreStore scoreStore;
    private final ArrayList<GameBlock> blocks;
    private final Random random;
    private final Timer timer;
    private Rock rock;
    private Point dragPoint;
    private boolean dragging;
    private int rocksUsed;
    private int score;
    private boolean gameOver;

    AngryLearningPanel(
            OcioJuegaAprendiendo frame,
            ScoreStore scoreStore
    ) {

        this.frame = frame;
        this.scoreStore = scoreStore;
        blocks = new ArrayList<>();
        random = new Random();
        setLayout(null);
        setBackground(new Color(135, 205, 245));
        setFocusable(true);

        timer = new Timer(16, e -> updateGame());
        timer.start();

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (gameOver || rock == null || rock.launched) {

                    return;
                }

                if (rock.contains(e.getX(), e.getY())) {

                    dragging = true;
                    dragPoint = e.getPoint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                if (dragging) {

                    dragPoint = e.getPoint();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (!dragging || rock == null) {

                    return;
                }

                dragging = false;
                launchRock(e.getPoint());
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    void resetGame() {

        removeAll();
        blocks.clear();
        score = 0;
        rocksUsed = 0;
        gameOver = false;
        dragging = false;
        createStructure();
        resetRock();
        repaint();
    }

    private void createStructure() {

        int baseX = 680;
        int blockW = 74;
        int blockH = 34;
        int rows = 7;
        int columns = 4;
        int obsidianCount = 0;

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < columns; col++) {

                if (random.nextDouble() < 0.10 && row > 1) {

                    continue;
                }

                BlockType type = randomType();

                if (type == BlockType.OBSIDIAN) {

                    if (obsidianCount >= 2) {

                        type = BlockType.STONE;
                    }

                    else {

                        obsidianCount++;
                    }
                }

                double x = baseX + col * (blockW + 8);
                double y = GROUND_Y - (row + 1) * (blockH + 3);

                blocks.add(new GameBlock(x, y, blockW, blockH, type));
            }
        }

        blocks.add(new GameBlock(baseX - 20, GROUND_Y - 34, 360, 34, BlockType.OBSIDIAN));
    }

    private BlockType randomType() {

        double value = random.nextDouble();

        if (value < 0.34) {

            return BlockType.ICE;
        }

        if (value < 0.66) {

            return BlockType.WOOD;
        }

        if (value < 0.93) {

            return BlockType.STONE;
        }

        return BlockType.OBSIDIAN;
    }

    private void resetRock() {

        if (rocksUsed >= MAX_ROCKS) {

            rock = null;
            return;
        }

        rock = new Rock(150, GROUND_Y - 58, 20);
    }

    private void launchRock(Point release) {

        double cannonX = 150;
        double cannonY = GROUND_Y - 58;
        double dx = cannonX - release.x;
        double dy = cannonY - release.y;

        rock.vx = Math.max(-2, Math.min(19, dx * 0.14));
        rock.vy = Math.max(-19, Math.min(9, dy * 0.14));
        rock.launched = true;
        rocksUsed++;
    }

    private void updateGame() {

        if (!gameOver) {

            updateRock();
            updateBlocks();
            checkEndGame();
        }

        repaint();
    }

    private void updateRock() {

        if (rock == null || !rock.launched) {

            return;
        }

        rock.vy += 0.42;
        rock.x += rock.vx;
        rock.y += rock.vy;

        if (rock.y + rock.radius >= GROUND_Y) {

            rock.y = GROUND_Y - rock.radius;
            rock.vy = -rock.vy * 0.35;
            rock.vx *= 0.82;

            if (Math.abs(rock.vy) < 1.2) {

                rock.vy = 0;
            }
        }

        for (GameBlock block : blocks) {

            if (!block.destroyed && block.bounds().intersects(rock.bounds())) {

                double force = Math.sqrt(rock.vx * rock.vx + rock.vy * rock.vy);
                block.damage(force);

                if (block.destroyed) {

                    score += block.type.points;
                }

                block.vx += rock.vx * 0.18;
                block.vy -= Math.abs(rock.vx) * 0.05;
                rock.vx *= -0.35;
                rock.vy *= 0.65;
            }
        }

        if (rock.x > WIDTH + 120 || rock.x < -120 || rock.y > HEIGHT + 120
                || (Math.abs(rock.vx) < 0.15 && Math.abs(rock.vy) < 0.15 && rock.y + rock.radius >= GROUND_Y - 1)) {

            resetRock();
        }
    }

    private void updateBlocks() {

        for (GameBlock block : blocks) {

            if (block.destroyed || block.type == BlockType.OBSIDIAN) {

                continue;
            }

            block.vy += 0.36;
            block.x += block.vx;
            block.y += block.vy;
            block.vx *= 0.92;

            if (block.y + block.height >= GROUND_Y) {

                block.y = GROUND_Y - block.height;
                block.vy = 0;
            }
        }

        for (int i = 0; i < blocks.size(); i++) {

            GameBlock upper = blocks.get(i);

            if (upper.destroyed) {

                continue;
            }

            for (int j = 0; j < blocks.size(); j++) {

                if (i == j) {

                    continue;
                }

                GameBlock lower = blocks.get(j);

                if (lower.destroyed) {

                    continue;
                }

                if (upper.vy >= 0
                        && upper.bottom() >= lower.y
                        && upper.y < lower.y
                        && upper.right() > lower.x + 6
                        && upper.x + 6 < lower.right()) {

                    upper.y = lower.y - upper.height;
                    upper.vy = 0;
                }
            }
        }
    }

    private void checkEndGame() {

        if (rocksUsed < MAX_ROCKS || rock != null) {

            return;
        }

        for (GameBlock block : blocks) {

            if (!block.destroyed && Math.abs(block.vy) > 0.1) {

                return;
            }
        }

        gameOver = true;
        scoreStore.saveIfRecord(score);
        showEndPanel();
    }

    private void showEndPanel() {

        JPanel panel = new JPanel(null);
        panel.setBounds(312, 210, 400, 250);
        panel.setBackground(new Color(20, 25, 35));
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel title = new JLabel("FIN DEL INTENTO", JLabel.CENTER);
        title.setBounds(30, 25, 340, 36);
        title.setForeground(new Color(255, 190, 0));
        title.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        panel.add(title);

        JLabel finalScore = new JLabel("Puntaje final: " + score, JLabel.CENTER);
        finalScore.setBounds(30, 78, 340, 30);
        finalScore.setForeground(Color.WHITE);
        finalScore.setFont(new Font("JetBrains Mono", Font.BOLD, 17));
        panel.add(finalScore);

        JButton retry = new JButton("Volver a intentar");
        retry.setBounds(80, 130, 240, 38);
        retry.addActionListener(e -> resetGame());
        panel.add(retry);

        JButton menu = new JButton("Volver al menú");
        menu.setBounds(80, 180, 240, 38);
        menu.addActionListener(e -> frame.showMenu());
        panel.add(menu);

        add(panel);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2);
        drawCannon(g2);
        drawTrajectory(g2);
        drawBlocks(g2);
        drawRock(g2);
        drawHud(g2);
    }

    private void drawBackground(Graphics2D g2) {

        g2.setColor(new Color(95, 180, 235));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(255, 255, 255, 110));
        g2.fillOval(90, 80, 160, 50);
        g2.fillOval(410, 115, 230, 62);
        g2.fillOval(760, 70, 180, 55);

        g2.setColor(new Color(70, 155, 70));
        g2.fillRect(0, GROUND_Y, getWidth(), getHeight() - GROUND_Y);
        g2.setColor(new Color(75, 90, 70));
        g2.fillRect(0, GROUND_Y, getWidth(), 12);
    }

    private void drawCannon(Graphics2D g2) {

        g2.setColor(new Color(55, 55, 60));
        g2.fillOval(90, GROUND_Y - 72, 110, 70);
        g2.setStroke(new BasicStroke(18));
        g2.draw(new Line2D.Double(140, GROUND_Y - 50, 210, GROUND_Y - 90));
        g2.setStroke(new BasicStroke(1));
    }

    private void drawTrajectory(Graphics2D g2) {

        if (!dragging || rock == null || dragPoint == null) {

            return;
        }

        double cannonX = 150;
        double cannonY = GROUND_Y - 58;
        double vx = Math.max(-2, Math.min(19, (cannonX - dragPoint.x) * 0.14));
        double vy = Math.max(-19, Math.min(9, (cannonY - dragPoint.y) * 0.14));
        double x = cannonX;
        double y = cannonY;

        g2.setColor(new Color(255, 255, 255, 190));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[] {6, 8}, 0));

        for (int i = 0; i < 50; i++) {

            double nextX = x + vx * 3;
            double nextY = y + vy * 3;
            vy += 0.42 * 3;

            g2.draw(new Line2D.Double(x, y, nextX, nextY));
            x = nextX;
            y = nextY;

            if (y >= GROUND_Y) {

                break;
            }
        }

        g2.setStroke(new BasicStroke(1));
    }

    private void drawBlocks(Graphics2D g2) {

        for (GameBlock block : blocks) {

            if (!block.destroyed) {

                block.draw(g2);
            }
        }
    }

    private void drawRock(Graphics2D g2) {

        if (rock != null) {

            rock.draw(g2);
        }
    }

    private void drawHud(Graphics2D g2) {

        g2.setColor(new Color(20, 25, 35, 190));
        g2.fillRoundRect(18, 18, 360, 92, 14, 14);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        g2.drawString("Rocas: " + (MAX_ROCKS - rocksUsed) + "/" + MAX_ROCKS, 38, 48);
        g2.drawString("Puntaje Actual: " + score, 38, 75);
        g2.drawString("Record: " + scoreStore.getHighScore(), 38, 100);
    }
}

class Rock {

    double x;
    double y;
    double vx;
    double vy;
    final double radius;
    boolean launched;

    Rock(double x, double y, double radius) {

        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    boolean contains(int px, int py) {

        double dx = px - x;
        double dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    Rectangle2D.Double bounds() {

        return new Rectangle2D.Double(
                x - radius,
                y - radius,
                radius * 2,
                radius * 2
        );
    }

    void draw(Graphics2D g2) {

        g2.setColor(new Color(70, 70, 75));
        g2.fillOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
        g2.setColor(new Color(35, 35, 40));
        g2.drawOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
    }
}

class GameBlock {

    double x;
    double y;
    double vx;
    double vy;
    final int width;
    final int height;
    final BlockType type;
    double health;
    boolean destroyed;

    GameBlock(
            double x,
            double y,
            int width,
            int height,
            BlockType type
    ) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.health = type.health;
    }

    void damage(double force) {

        if (type == BlockType.OBSIDIAN) {

            return;
        }

        health -= force;

        if (health <= 0) {

            destroyed = true;
        }
    }

    Rectangle2D.Double bounds() {

        return new Rectangle2D.Double(x, y, width, height);
    }

    double right() {

        return x + width;
    }

    double bottom() {

        return y + height;
    }

    void draw(Graphics2D g2) {

        g2.setColor(type.color);
        g2.fillRect((int)x, (int)y, width, height);
        g2.setColor(new Color(0, 0, 0, 110));
        g2.drawRect((int)x, (int)y, width, height);

        if (type != BlockType.OBSIDIAN) {

            int barWidth = (int)(width * Math.max(0, health / type.health));
            g2.setColor(new Color(0, 255, 90, 160));
            g2.fillRect((int)x + 4, (int)y + 4, barWidth - 8, 5);
        }
    }
}

enum BlockType {
    ICE(new Color(173, 216, 230), 7, 50),
    WOOD(new Color(139, 69, 19), 18, 100),
    STONE(new Color(128, 128, 128), 34, 300),
    OBSIDIAN(new Color(30, 30, 30), Double.MAX_VALUE, 0);

    final Color color;
    final double health;
    final int points;

    BlockType(Color color, double health, int points) {

        this.color = color;
        this.health = health;
        this.points = points;
    }
}

class ScoreStore {

    private final File file;
    private int highScore;

    ScoreStore(String path) {

        file = new File(path);
        highScore = load();
    }

    int getHighScore() {

        return highScore;
    }

    void saveIfRecord(int score) {

        if (score > highScore) {

            highScore = score;
            save();
        }
    }

    private int load() {

        if (!file.exists()) {

            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            return Integer.parseInt(reader.readLine().trim());

        } catch (Exception ex) {

            return 0;
        }
    }

    private void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write(String.valueOf(highScore));

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
