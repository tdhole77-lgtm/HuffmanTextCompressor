import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Visual representation of a Huffman Tree.
 *
 * Left branch  = 0
 * Right branch = 1
 *
 * Leaf nodes show:
 * Character
 * Frequency
 *
 * Internal nodes show:
 * Frequency
 */
public class HuffmanTreePanel extends JPanel {

    private HuffmanNode root;

    // Position of every node on the screen
    private final Map<HuffmanNode, NodePosition> positions =
            new HashMap<>();

    // Appearance settings
    private static final int NODE_WIDTH = 86;
    private static final int NODE_HEIGHT = 54;

    private static final int HORIZONTAL_GAP = 30;
    private static final int VERTICAL_GAP = 85;

    private static final int LEFT_MARGIN = 70;
    private static final int TOP_MARGIN = 60;
    private static final int RIGHT_MARGIN = 70;
    private static final int BOTTOM_MARGIN = 70;

    // Used to calculate horizontal positions
    private int nextX = LEFT_MARGIN;

    /**
     * Default constructor.
     */
    public HuffmanTreePanel() {
        setBackground(Color.WHITE);
        setOpaque(true);

        setPreferredSize(new Dimension(900, 500));
    }

    /**
     * Constructor that immediately displays a tree.
     */
    public HuffmanTreePanel(HuffmanNode root) {
        this();
        setRoot(root);
    }

    /**
     * Set a new Huffman Tree root.
     */
    public void setRoot(HuffmanNode root) {

        this.root = root;

        positions.clear();

        calculateTreeSize();

        revalidate();
        repaint();
    }

    /**
     * Alternative method name for convenience.
     */
    public void setTree(HuffmanNode root) {
        setRoot(root);
    }

    /**
     * Remove the currently displayed tree.
     */
    public void clearTree() {

        this.root = null;

        positions.clear();

        setPreferredSize(new Dimension(900, 500));

        revalidate();
        repaint();
    }

    /**
     * Calculate a suitable panel size before painting.
     */
    private void calculateTreeSize() {

        if (root == null) {
            setPreferredSize(new Dimension(900, 500));
            return;
        }

        int leafCount = countLeaves(root);
        int depth = getTreeDepth(root);

        /*
         * Make the panel wider for trees with many characters.
         */
        int width = Math.max(
                900,
                LEFT_MARGIN
                        + RIGHT_MARGIN
                        + Math.max(1, leafCount)
                        * (NODE_WIDTH + HORIZONTAL_GAP)
        );

        /*
         * Make the panel taller for deeper trees.
         */
        int height = Math.max(
                500,
                TOP_MARGIN
                        + BOTTOM_MARGIN
                        + Math.max(1, depth)
                        * VERTICAL_GAP
        );

        setPreferredSize(new Dimension(width, height));
    }

    /**
     * Count the number of leaf nodes.
     */
    private int countLeaves(HuffmanNode node) {

        if (node == null) {
            return 0;
        }

        if (node.isLeaf()) {
            return 1;
        }

        return countLeaves(node.left)
                + countLeaves(node.right);
    }

    /**
     * Calculate the maximum depth of the tree.
     */
    private int getTreeDepth(HuffmanNode node) {

        if (node == null) {
            return 0;
        }

        if (node.isLeaf()) {
            return 1;
        }

        return 1 + Math.max(
                getTreeDepth(node.left),
                getTreeDepth(node.right)
        );
    }

    /**
     * Paint the Huffman Tree.
     */
    @Override
    protected void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();

        // Anti-aliasing makes the tree look smoother.
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        if (root == null) {

            drawEmptyMessage(g);

            g.dispose();

            return;
        }

        /*
         * Calculate positions from left to right.
         */
        positions.clear();

        nextX = LEFT_MARGIN;

        calculatePositions(root, 0);

        /*
         * Draw connections first.
         * This makes the lines appear behind the nodes.
         */
        drawConnections(g, root);

        /*
         * Draw nodes after connections.
         */
        drawNodes(g, root);

        g.dispose();
    }

    /**
     * Calculate the position of every node.
     *
     * Leaf nodes are placed from left to right.
     * Internal nodes are placed between their children.
     */
    private int calculatePositions(
            HuffmanNode node,
            int depth) {

        if (node == null) {
            return 0;
        }

        int y = TOP_MARGIN
                + depth * VERTICAL_GAP;

        if (node.isLeaf()) {

            int x = nextX;

            positions.put(
                    node,
                    new NodePosition(x, y)
            );

            nextX += NODE_WIDTH + HORIZONTAL_GAP;

            return x;
        }

        int leftX = 0;
        int rightX = 0;

        if (node.left != null) {

            leftX = calculatePositions(
                    node.left,
                    depth + 1
            );
        }

        if (node.right != null) {

            rightX = calculatePositions(
                    node.right,
                    depth + 1
            );
        }

        int x;

        if (node.left != null && node.right != null) {

            x = (leftX + rightX) / 2;

        } else if (node.left != null) {

            x = leftX;

        } else {

            x = rightX;
        }

        positions.put(
                node,
                new NodePosition(x, y)
        );

        return x;
    }

    /**
     * Draw lines connecting parent and child nodes.
     */
    private void drawConnections(
            Graphics2D g,
            HuffmanNode node) {

        if (node == null || node.isLeaf()) {
            return;
        }

        NodePosition parentPosition =
                positions.get(node);

        if (parentPosition == null) {
            return;
        }

        /*
         * Left child = 0
         */
        if (node.left != null) {

            NodePosition childPosition =
                    positions.get(node.left);

            if (childPosition != null) {

                drawBranch(
                        g,
                        parentPosition,
                        childPosition,
                        "0"
                );
            }

            drawConnections(
                    g,
                    node.left
            );
        }

        /*
         * Right child = 1
         */
        if (node.right != null) {

            NodePosition childPosition =
                    positions.get(node.right);

            if (childPosition != null) {

                drawBranch(
                        g,
                        parentPosition,
                        childPosition,
                        "1"
                );
            }

            drawConnections(
                    g,
                    node.right
            );
        }
    }

    /**
     * Draw one branch and its 0/1 label.
     */
    private void drawBranch(
            Graphics2D g,
            NodePosition parent,
            NodePosition child,
            String label) {

        int parentX = parent.x;
        int parentY = parent.y;

        int childX = child.x;
        int childY = child.y;

        /*
         * Start from the bottom center of parent.
         */
        int startX = parentX;
        int startY = parentY + NODE_HEIGHT / 2;

        /*
         * End at the top center of child.
         */
        int endX = childX;
        int endY = childY - NODE_HEIGHT / 2;

        g.setColor(Color.DARK_GRAY);

        g.setStroke(
                new BasicStroke(
                        2.0f
                )
        );

        g.drawLine(
                startX,
                startY,
                endX,
                endY
        );

        /*
         * Calculate position for 0/1 label.
         */
        int labelX =
                (startX + endX) / 2;

        int labelY =
                (startY + endY) / 2;

        /*
         * Small white background behind the label.
         */
        g.setColor(Color.WHITE);

        g.fillRoundRect(
                labelX - 11,
                labelY - 11,
                22,
                22,
                8,
                8
        );

        g.setColor(Color.BLACK);

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        FontMetrics metrics =
                g.getFontMetrics();

        int textWidth =
                metrics.stringWidth(label);

        int textHeight =
                metrics.getAscent();

        g.drawString(
                label,
                labelX - textWidth / 2,
                labelY + textHeight / 2
        );
    }

    /**
     * Draw every node recursively.
     */
    private void drawNodes(
            Graphics2D g,
            HuffmanNode node) {

        if (node == null) {
            return;
        }

        NodePosition position =
                positions.get(node);

        if (position == null) {
            return;
        }

        drawNode(
                g,
                node,
                position
        );

        drawNodes(
                g,
                node.left
        );

        drawNodes(
                g,
                node.right
        );
    }

    /**
     * Draw an individual node.
     */
    private void drawNode(
            Graphics2D g,
            HuffmanNode node,
            NodePosition position) {

        int x =
                position.x - NODE_WIDTH / 2;

        int y =
                position.y - NODE_HEIGHT / 2;

        /*
         * Leaf nodes and internal nodes are visually
         * distinguished.
         */
        if (node.isLeaf()) {

            g.setColor(
                    new Color(
                            225,
                            245,
                            235
                    )
            );

        } else {

            g.setColor(
                    new Color(
                            225,
                            235,
                            250
                    )
            );
        }

        g.fillRoundRect(
                x,
                y,
                NODE_WIDTH,
                NODE_HEIGHT,
                18,
                18
        );

        /*
         * Node border.
         */
        g.setColor(
                new Color(
                        70,
                        70,
                        70
                )
        );

        g.setStroke(
                new BasicStroke(
                        2.0f
                )
        );

        g.drawRoundRect(
                x,
                y,
                NODE_WIDTH,
                NODE_HEIGHT,
                18,
                18
        );

        /*
         * Prepare text.
         */
        String firstLine;
        String secondLine;

        if (node.isLeaf()) {

            firstLine =
                    formatCharacter(
                            node.character
                    );

            secondLine =
                    "freq: " + node.frequency;

        } else {

            firstLine = "Internal";

            secondLine =
                    "freq: " + node.frequency;
        }

        g.setColor(Color.BLACK);

        /*
         * First line.
         */
        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        drawCenteredText(
                g,
                firstLine,
                position.x,
                position.y - 6
        );

        /*
         * Second line.
         */
        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        drawCenteredText(
                g,
                secondLine,
                position.x,
                position.y + 13
        );
    }

    /**
     * Draw text centered around an X coordinate.
     */
    private void drawCenteredText(
            Graphics2D g,
            String text,
            int centerX,
            int baselineY) {

        FontMetrics metrics =
                g.getFontMetrics();

        int width =
                metrics.stringWidth(text);

        g.drawString(
                text,
                centerX - width / 2,
                baselineY
        );
    }

    /**
     * Make invisible/control characters readable.
     */
    private String formatCharacter(
            char character) {

        switch (character) {

            case ' ':
                return "[SPACE]";

            case '\n':
                return "[NEWLINE]";

            case '\t':
                return "[TAB]";

            case '\r':
                return "[CR]";

            default:
                return "'" + character + "'";
        }
    }

    /**
     * Message displayed when there is no tree.
     */
    private void drawEmptyMessage(
            Graphics2D g) {

        g.setColor(
                new Color(
                        100,
                        100,
                        100
                )
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        16
                )
        );

        String message =
                "Compress a text file to display the Huffman Tree.";

        FontMetrics metrics =
                g.getFontMetrics();

        int x =
                (getWidth()
                        - metrics.stringWidth(message))
                        / 2;

        int y =
                Math.max(
                        80,
                        getHeight() / 2
                );

        g.drawString(
                message,
                x,
                y
        );
    }

    /**
     * Stores the screen position of a tree node.
     */
    private static class NodePosition {

        int x;
        int y;

        NodePosition(
                int x,
                int y) {

            this.x = x;
            this.y = y;
        }
    }
}