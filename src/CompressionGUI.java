import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Huffman Text Compressor
 *
 * Complete polished Swing GUI.
 *
 * Works with:
 *  - HuffmanCompressor
 *  - HuffmanDecompressor
 *  - HuffmanTree
 *  - HuffmanNode
 */
public class CompressionGUI extends JFrame {

    // ------------------------------------------------------------
    // COLORS
    // ------------------------------------------------------------

    private static final Color DARK_HEADER =
            new Color(31, 41, 55);

    private static final Color DARKER =
            new Color(17, 24, 39);

    private static final Color LIGHT_BACKGROUND =
            new Color(245, 247, 250);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color BORDER_COLOR =
            new Color(210, 215, 222);

    private static final Color BLUE =
            new Color(37, 99, 235);

    private static final Color GREEN =
            new Color(22, 163, 74);

    private static final Color RED =
            new Color(220, 38, 38);

    // ------------------------------------------------------------
    // MAIN COMPONENTS
    // ------------------------------------------------------------

    private JTextField compressionFileField;
    private JTextField decompressionFileField;

    private JButton selectCompressionButton;
    private JButton compressButton;

    private JButton selectDecompressionButton;
    private JButton decompressButton;

    private JButton howItWorksButton;
    private JButton aboutButton;
    private JButton resetButton;

    private JLabel originalSizeLabel;
    private JLabel compressedSizeLabel;
    private JLabel compressionRatioLabel;
    private JLabel spaceSavedLabel;

    private JLabel statusLabel;

    private JTable codeTable;
    private DefaultTableModel tableModel;

    private TreePanel treePanel;
    private JScrollPane treeScrollPane;

    // ------------------------------------------------------------
    // DATA
    // ------------------------------------------------------------

    private File selectedCompressionFile;
    private File selectedDecompressionFile;

    private HuffmanTree currentTree;

    // ------------------------------------------------------------
    // CONSTRUCTOR
    // ------------------------------------------------------------

    public CompressionGUI() {

        setTitle("Huffman Text Compressor");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(1100, 750));

        setSize(1500, 950);

        setLocationRelativeTo(null);

        buildGUI();

        setVisible(true);
    }

    // ------------------------------------------------------------
    // BUILD GUI
    // ------------------------------------------------------------

    private void buildGUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(12, 12));

        mainPanel.setBackground(LIGHT_BACKGROUND);

        mainPanel.setBorder(
                new EmptyBorder(12, 12, 12, 12)
        );

        // --------------------------------------------------------
        // HEADER
        // --------------------------------------------------------

        JPanel header =
                createHeader();

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // --------------------------------------------------------
        // CENTER CONTENT
        // --------------------------------------------------------

        JPanel content =
                new JPanel();

        content.setBackground(LIGHT_BACKGROUND);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        // Compression section
        content.add(
                createCompressionPanel()
        );

        content.add(
                Box.createVerticalStrut(10)
        );

        // Decompression section
        content.add(
                createDecompressionPanel()
        );

        content.add(
                Box.createVerticalStrut(10)
        );

        // Statistics
        content.add(
                createStatisticsPanel()
        );

        content.add(
                Box.createVerticalStrut(10)
        );

        // Codes + Tree
        content.add(
                createVisualizationPanel()
        );

        mainPanel.add(
                content,
                BorderLayout.CENTER
        );

        // --------------------------------------------------------
        // STATUS BAR
        // --------------------------------------------------------

        JPanel statusPanel =
                new JPanel(new BorderLayout());

        statusPanel.setBackground(
                DARK_HEADER
        );

        statusPanel.setBorder(
                new EmptyBorder(
                        8,
                        12,
                        8,
                        12
                )
        );

        statusLabel =
                new JLabel(
                        "●  Ready"
                );

        statusLabel.setForeground(
                Color.WHITE
        );

        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        statusPanel.add(
                statusLabel,
                BorderLayout.WEST
        );

        mainPanel.add(
                statusPanel,
                BorderLayout.SOUTH
        );

        setContentPane(mainPanel);
    }

    // ------------------------------------------------------------
    // HEADER
    // ------------------------------------------------------------

    private JPanel createHeader() {

        JPanel header =
                new JPanel(new BorderLayout(20, 0));

        header.setBackground(
                DARK_HEADER
        );

        header.setBorder(
                new EmptyBorder(
                        18,
                        22,
                        18,
                        22
                )
        );

        // --------------------------------------------------------
        // TITLE
        // --------------------------------------------------------

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "HUFFMAN TEXT COMPRESSOR"
                );

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        27
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Lossless Compression • Greedy Algorithm • Binary Tree"
                );

        subtitle.setForeground(
                new Color(
                        215,
                        220,
                        230
                )
        );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // --------------------------------------------------------
        // BUTTONS
        // --------------------------------------------------------

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        howItWorksButton =
                createHeaderButton(
                        "How It Works"
                );

        aboutButton =
                createHeaderButton(
                        "Information"
                );

        resetButton =
                createHeaderButton(
                        "Reset"
                );

        howItWorksButton.addActionListener(
                e -> showHowItWorks()
        );

        aboutButton.addActionListener(
                e -> showAbout()
        );

        resetButton.addActionListener(
                e -> resetGUI()
        );

        buttonPanel.add(
                howItWorksButton
        );

        buttonPanel.add(
                aboutButton
        );

        buttonPanel.add(
                resetButton
        );

        // Reserve enough room so the three buttons cannot be squeezed away.
        buttonPanel.setMinimumSize(
                new Dimension(
                        410,
                        50
                )
        );

        buttonPanel.setPreferredSize(
                new Dimension(
                        410,
                        50
                )
        );

        header.add(
                buttonPanel,
                BorderLayout.EAST
        );

        return header;
    }

    // ------------------------------------------------------------
    // HEADER BUTTON
    // ------------------------------------------------------------

    private JButton createHeaderButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setContentAreaFilled(true);

        button.setBorderPainted(true);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        // High contrast: dark text on white button.
        button.setForeground(
                new Color(
                        17,
                        24,
                        39
                )
        );

        button.setBackground(
                Color.WHITE
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        190,
                                        195,
                                        205
                                ),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                14,
                                8,
                                14
                        )
                )
        );

        button.setPreferredSize(
                new Dimension(
                        125,
                        40
                )
        );

        button.setMinimumSize(
                new Dimension(
                        115,
                        40
                )
        );

        button.setMaximumSize(
                new Dimension(
                        160,
                        40
                )
        );

        // --------------------------------------------------------
        // HOVER EFFECT
        // --------------------------------------------------------

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        button.setBackground(
                                new Color(
                                        225,
                                        232,
                                        245
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        button.setBackground(
                                Color.WHITE
                        );
                    }
                }
        );

        return button;
    }

    // ------------------------------------------------------------
    // COMPRESSION PANEL
    // ------------------------------------------------------------

    private JPanel createCompressionPanel() {

        JPanel panel =
                createSectionPanel(
                        "Compression"
                );

        panel.setLayout(
                new BorderLayout(10, 10)
        );

        JPanel fileRow =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        fileRow.setOpaque(false);

        compressionFileField =
                new JTextField();

        compressionFileField.setEditable(false);

        compressionFileField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        selectCompressionButton =
                createActionButton(
                        "Select .TXT File"
                );

        selectCompressionButton.addActionListener(
                e -> chooseCompressionFile()
        );

        fileRow.add(
                compressionFileField,
                BorderLayout.CENTER
        );

        fileRow.add(
                selectCompressionButton,
                BorderLayout.EAST
        );

        panel.add(
                fileRow,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        buttonPanel.setOpaque(false);

        compressButton =
                createPrimaryButton(
                        "COMPRESS"
                );

        compressButton.setPreferredSize(
                new Dimension(150, 42)
        );

        compressButton.addActionListener(
                e -> compressFile()
        );

        buttonPanel.add(
                compressButton
        );

        panel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ------------------------------------------------------------
    // DECOMPRESSION PANEL
    // ------------------------------------------------------------

    private JPanel createDecompressionPanel() {

        JPanel panel =
                createSectionPanel(
                        "Decompression"
                );

        panel.setLayout(
                new BorderLayout(10, 10)
        );

        JPanel fileRow =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        fileRow.setOpaque(false);

        decompressionFileField =
                new JTextField();

        decompressionFileField.setEditable(false);

        decompressionFileField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        selectDecompressionButton =
                createActionButton(
                        "Select .HUFF File"
                );

        selectDecompressionButton.addActionListener(
                e -> chooseDecompressionFile()
        );

        fileRow.add(
                decompressionFileField,
                BorderLayout.CENTER
        );

        fileRow.add(
                selectDecompressionButton,
                BorderLayout.EAST
        );

        panel.add(
                fileRow,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        buttonPanel.setOpaque(false);

        decompressButton =
                createPrimaryButton(
                        "DECOMPRESS"
                );

        decompressButton.setPreferredSize(
                new Dimension(150, 42)
        );

        decompressButton.addActionListener(
                e -> decompressFile()
        );

        buttonPanel.add(
                decompressButton
        );

        panel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ------------------------------------------------------------
    // STATISTICS
    // ------------------------------------------------------------

    private JPanel createStatisticsPanel() {

        JPanel panel =
                createSectionPanel(
                        "Compression Statistics"
                );

        panel.setLayout(
                new GridLayout(
                        1,
                        4,
                        20,
                        0
                )
        );

        originalSizeLabel =
                createStatisticValue();

        compressedSizeLabel =
                createStatisticValue();

        compressionRatioLabel =
                createStatisticValue();

        spaceSavedLabel =
                createStatisticValue();

        panel.add(
                createStatisticBox(
                        "Original Size",
                        originalSizeLabel
                )
        );

        panel.add(
                createStatisticBox(
                        "Compressed Size",
                        compressedSizeLabel
                )
        );

        panel.add(
                createStatisticBox(
                        "Compression Ratio",
                        compressionRatioLabel
                )
        );

        panel.add(
                createStatisticBox(
                        "Space Saved",
                        spaceSavedLabel
                )
        );

        return panel;
    }

    // ------------------------------------------------------------
    // VISUALIZATION PANEL
    // ------------------------------------------------------------

    private JPanel createVisualizationPanel() {

        JPanel panel =
                createSectionPanel(
                        "Huffman Codes & Tree Visualization"
                );

        panel.setLayout(
                new BorderLayout()
        );

        JSplitPane splitPane =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT
                );

        splitPane.setResizeWeight(
                0.35
        );

        splitPane.setDividerSize(8);

        splitPane.setBorder(null);

        // --------------------------------------------------------
        // TABLE
        // --------------------------------------------------------

        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Huffman Code",
                                "Frequency",
                                "Character"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        codeTable =
                new JTable(
                        tableModel
                );

        codeTable.setRowHeight(28);

        codeTable.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        codeTable.getTableHeader()
                .setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                14
                        )
                );

        codeTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        codeTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(150);

        codeTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(100);

        codeTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(150);

        JScrollPane tableScroll =
                new JScrollPane(
                        codeTable
                );

        tableScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_COLOR
                )
        );

        // --------------------------------------------------------
        // TREE
        // --------------------------------------------------------

        treePanel =
                new TreePanel();

        treeScrollPane =
                new JScrollPane(
                        treePanel
                );

        treeScrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_COLOR
                )
        );

        treeScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        treeScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        // --------------------------------------------------------
        // TREE TOOLBAR
        // --------------------------------------------------------

        JPanel treeArea =
                new JPanel(
                        new BorderLayout()
                );

        treeArea.setBackground(
                Color.WHITE
        );

        JPanel treeToolbar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                5
                        )
                );

        treeToolbar.setBackground(
                new Color(245, 246, 248)
        );

        JButton zoomOut =
                createSmallButton(
                        "−"
                );

        JButton zoomIn =
                createSmallButton(
                        "+"
                );

        JButton fitTree =
                createSmallButton(
                        "Fit Tree"
                );

        JLabel zoomLabel =
                new JLabel(
                        "100%"
                );

        zoomLabel.setPreferredSize(
                new Dimension(
                        55,
                        30
                )
        );

        zoomLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        zoomOut.addActionListener(
                e -> {

                    treePanel.zoomOut();

                    updateZoomLabel(
                            zoomLabel
                    );
                }
        );

        zoomIn.addActionListener(
                e -> {

                    treePanel.zoomIn();

                    updateZoomLabel(
                            zoomLabel
                    );
                }
        );

        fitTree.addActionListener(
                e -> {

                    treePanel.fitToView(
                            treeScrollPane
                    );

                    updateZoomLabel(
                            zoomLabel
                    );
                }
        );

        treeToolbar.add(
                new JLabel("Zoom:")
        );

        treeToolbar.add(
                zoomOut
        );

        treeToolbar.add(
                zoomLabel
        );

        treeToolbar.add(
                zoomIn
        );

        treeToolbar.add(
                fitTree
        );

        treeArea.add(
                treeToolbar,
                BorderLayout.NORTH
        );

        treeArea.add(
                treeScrollPane,
                BorderLayout.CENTER
        );

        splitPane.setLeftComponent(
                tableScroll
        );

        splitPane.setRightComponent(
                treeArea
        );

        panel.add(
                splitPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ------------------------------------------------------------
    // BUTTONS
    // ------------------------------------------------------------

    private JButton createActionButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFocusPainted(false);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setPreferredSize(
                new Dimension(
                        165,
                        38
                )
        );

        return button;
    }

    private JButton createPrimaryButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFocusPainted(false);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        button.setBackground(
                WHITE
        );

        button.setForeground(
                DARKER
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                15,
                                5,
                                15
                        )
                )
        );

        return button;
    }

    private JButton createSmallButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFocusPainted(false);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setPreferredSize(
                new Dimension(
                        text.equals("Fit Tree")
                                ? 90
                                : 42,
                        30
                )
        );

        return button;
    }

    // ------------------------------------------------------------
    // SECTION PANEL
    // ------------------------------------------------------------

    private JPanel createSectionPanel(
            String title) {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        title,
                        0,
                        0,
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                14
                        ),
                        DARKER
                )
        );

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return panel;
    }

    // ------------------------------------------------------------
    // STATISTIC BOX
    // ------------------------------------------------------------

    private JPanel createStatisticBox(
            String title,
            JLabel valueLabel) {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        valueLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                titleLabel
        );

        panel.add(
                Box.createVerticalStrut(4)
        );

        panel.add(
                valueLabel
        );

        return panel;
    }

    private JLabel createStatisticValue() {

        JLabel label =
                new JLabel(
                        "--",
                        SwingConstants.CENTER
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        return label;
    }

    // ------------------------------------------------------------
    // SELECT COMPRESSION FILE
    // ------------------------------------------------------------

    private void chooseCompressionFile() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Select Text File"
        );

        int result =
                chooser.showOpenDialog(
                        this
                );

        if (result ==
                JFileChooser.APPROVE_OPTION) {

            selectedCompressionFile =
                    chooser.getSelectedFile();

            compressionFileField.setText(
                    selectedCompressionFile
                            .getAbsolutePath()
            );

            setStatus(
                    "Text file selected."
            );
        }
    }

    // ------------------------------------------------------------
    // SELECT DECOMPRESSION FILE
    // ------------------------------------------------------------

    private void chooseDecompressionFile() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Select Huffman File"
        );

        int result =
                chooser.showOpenDialog(
                        this
                );

        if (result ==
                JFileChooser.APPROVE_OPTION) {

            selectedDecompressionFile =
                    chooser.getSelectedFile();

            decompressionFileField.setText(
                    selectedDecompressionFile
                            .getAbsolutePath()
            );

            setStatus(
                    "HUFF file selected."
            );
        }
    }

    // ------------------------------------------------------------
    // COMPRESS
    // ------------------------------------------------------------

    private void compressFile() {

        if (selectedCompressionFile == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a .TXT file first.",
                    "No File Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            File input =
                    selectedCompressionFile;

            String fileName =
                    input.getName();

            if (fileName.toLowerCase()
                    .endsWith(".txt")) {

                fileName =
                        fileName.substring(
                                0,
                                fileName.length() - 4
                        );
            }

            File output =
                    new File(
                            input.getParentFile(),
                            fileName + ".huff"
                    );

            // ----------------------------------------------------
            // Perform compression
            // ----------------------------------------------------

            HuffmanCompressor.compress(
                    input.getAbsolutePath(),
                    output.getAbsolutePath()
            );

            // ----------------------------------------------------
            // Build tree for GUI
            // ----------------------------------------------------

            String text =
                    Files.readString(
                            input.toPath(),
                            StandardCharsets.UTF_8
                    );

            currentTree =
                    new HuffmanTree(text);

            // ----------------------------------------------------
            // Update fields
            // ----------------------------------------------------

            selectedDecompressionFile =
                    output;

            decompressionFileField.setText(
                    output.getAbsolutePath()
            );

            // ----------------------------------------------------
            // Update statistics
            // ----------------------------------------------------

            updateStatistics(
                    input,
                    output
            );

            // ----------------------------------------------------
            // Update codes
            // ----------------------------------------------------

            populateCodeTable(
                    text,
                    currentTree
            );

            // ----------------------------------------------------
            // Update tree
            // ----------------------------------------------------

            treePanel.setTree(
                    currentTree.getRoot()
            );

            SwingUtilities.invokeLater(
                    () -> treePanel.fitToView(
                            treeScrollPane
                    )
            );

            setStatus(
                    "Compression completed successfully ✓"
            );

        } catch (IOException ex) {

            showError(
                    "Compression failed:\n"
                            + ex.getMessage()
            );
        }
    }

    // ------------------------------------------------------------
    // DECOMPRESS
    // ------------------------------------------------------------

    private void decompressFile() {

        if (selectedDecompressionFile == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a .HUFF file first.",
                    "No File Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            File input =
                    selectedDecompressionFile;

            String fileName =
                    input.getName();

            if (fileName.toLowerCase()
                    .endsWith(".huff")) {

                fileName =
                        fileName.substring(
                                0,
                                fileName.length() - 5
                        );
            }

            File output =
                    new File(
                            input.getParentFile(),
                            fileName
                                    + "_decompressed.txt"
                    );

            HuffmanDecompressor.decompress(
                    input.getAbsolutePath(),
                    output.getAbsolutePath()
            );

            setStatus(
                    "Decompression completed successfully ✓"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Decompression completed successfully!\n\n"
                            + "Output file:\n"
                            + output.getAbsolutePath(),
                    "Decompression Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {

            showError(
                    "Decompression failed:\n"
                            + ex.getMessage()
            );
        }
    }

    // ------------------------------------------------------------
    // UPDATE STATISTICS
    // ------------------------------------------------------------

    private void updateStatistics(
            File original,
            File compressed) {

        long originalSize =
                original.length();

        long compressedSize =
                compressed.length();

        double ratio;

        double saved;

        if (originalSize == 0) {

            ratio = 0;

            saved = 0;

        } else {

            ratio =
                    (double) compressedSize
                            / originalSize;

            saved =
                    (1.0
                            - ((double) compressedSize
                            / originalSize))
                            * 100.0;
        }

        originalSizeLabel.setText(
                formatBytes(originalSize)
        );

        compressedSizeLabel.setText(
                formatBytes(compressedSize)
        );

        compressionRatioLabel.setText(
                String.format(
                        "%.2fx",
                        ratio
                )
        );

        spaceSavedLabel.setText(
                String.format(
                        "%.2f%%",
                        saved
                )
        );

        if (saved >= 0) {

            spaceSavedLabel.setForeground(
                    GREEN
            );

        } else {

            spaceSavedLabel.setForeground(
                    RED
            );
        }
    }

    // ------------------------------------------------------------
    // FORMAT BYTES
    // ------------------------------------------------------------

    private String formatBytes(
            long bytes) {

        if (bytes < 1024) {

            return bytes + " B";
        }

        if (bytes < 1024 * 1024) {

            return String.format(
                    "%.2f KB",
                    bytes / 1024.0
            );
        }

        if (bytes < 1024L * 1024L * 1024L) {

            return String.format(
                    "%.2f MB",
                    bytes
                            / (1024.0 * 1024.0)
            );
        }

        return String.format(
                "%.2f GB",
                bytes
                        / (1024.0
                        * 1024.0
                        * 1024.0)
        );
    }

    // ------------------------------------------------------------
    // POPULATE HUFFMAN CODE TABLE
    // ------------------------------------------------------------

    private void populateCodeTable(
            String text,
            HuffmanTree tree) {

        tableModel.setRowCount(0);

        Map<Character, Integer> frequencies =
                new HashMap<>();

        for (char c : text.toCharArray()) {

            frequencies.put(
                    c,
                    frequencies.getOrDefault(
                            c,
                            0
                    ) + 1
            );
        }

        Map<Character, String> codes =
                tree.getHuffmanCodes();

        List<Character> characters =
                new ArrayList<>(
                        frequencies.keySet()
                );

        characters.sort(
                Comparator
                        .comparingInt(
                                (Character c)
                                        -> frequencies.get(c)
                        )
                        .reversed()
                        .thenComparing(
                                Character::compareTo
                        )
        );

        for (Character c : characters) {

            String characterDisplay =
                    displayCharacter(c);

            String code =
                    codes.get(c);

            tableModel.addRow(
                    new Object[]{
                            code,
                            frequencies.get(c),
                            characterDisplay
                    }
            );
        }
    }

    // ------------------------------------------------------------
    // DISPLAY SPECIAL CHARACTERS
    // ------------------------------------------------------------

    private String displayCharacter(
            char c) {

        switch (c) {

            case '\n':
                return "[NEWLINE]";

            case '\r':
                return "[CARRIAGE RETURN]";

            case '\t':
                return "[TAB]";

            case ' ':
                return "[SPACE]";

            default:
                return String.valueOf(c);
        }
    }

    // ------------------------------------------------------------
    // STATUS
    // ------------------------------------------------------------

    private void setStatus(
            String message) {

        statusLabel.setText(
                "●  " + message
        );
    }

    // ------------------------------------------------------------
    // ERROR
    // ------------------------------------------------------------

    private void showError(
            String message) {

        setStatus(
                "Error occurred"
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ------------------------------------------------------------
    // RESET
    // ------------------------------------------------------------

    private void resetGUI() {

        selectedCompressionFile = null;

        selectedDecompressionFile = null;

        currentTree = null;

        compressionFileField.setText("");

        decompressionFileField.setText("");

        originalSizeLabel.setText("--");

        compressedSizeLabel.setText("--");

        compressionRatioLabel.setText("--");

        spaceSavedLabel.setText("--");

        spaceSavedLabel.setForeground(
                DARKER
        );

        tableModel.setRowCount(0);

        treePanel.clearTree();

        treePanel.resetZoom();

        setStatus(
                "Ready"
        );
    }

    // ------------------------------------------------------------
    // ZOOM LABEL
    // ------------------------------------------------------------

    private void updateZoomLabel(
            JLabel label) {

        int percent =
                (int)
                        Math.round(
                                treePanel
                                        .getZoom()
                                        * 100
                        );

        label.setText(
                percent + "%"
        );
    }

    // ------------------------------------------------------------
    // HOW IT WORKS
    // ------------------------------------------------------------

    private void showHowItWorks() {

        JTextArea textArea =
                new JTextArea();

        textArea.setEditable(false);

        textArea.setLineWrap(true);

        textArea.setWrapStyleWord(true);

        textArea.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        textArea.setText(
                "HOW HUFFMAN COMPRESSION WORKS\n\n"

                        + "1. Read the text file\n"
                        + "The application reads the selected "
                        + "UTF-8 text file.\n\n"

                        + "2. Count character frequencies\n"
                        + "Every character is counted. Characters "
                        + "that appear more frequently receive "
                        + "shorter Huffman codes.\n\n"

                        + "3. Build the Huffman Tree\n"
                        + "The two nodes with the smallest "
                        + "frequencies are repeatedly combined "
                        + "until one root node remains.\n\n"

                        + "4. Generate binary codes\n"
                        + "Moving left represents 0 and moving "
                        + "right represents 1.\n\n"

                        + "5. Encode the text\n"
                        + "Each character is replaced by its "
                        + "Huffman binary code.\n\n"

                        + "6. Store the compressed file\n"
                        + "The .huff file stores the character "
                        + "frequency information and encoded "
                        + "binary data.\n\n"

                        + "7. Decompress\n"
                        + "The Huffman tree is reconstructed and "
                        + "the binary data is converted back into "
                        + "the original text.\n\n"

                        + "The process is LOSSLESS, meaning the "
                        + "original text can be recovered exactly."
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        textArea
                );

        scrollPane.setPreferredSize(
                new Dimension(
                        650,
                        500
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "How Huffman Compression Works",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ------------------------------------------------------------
    // ABOUT
    // ------------------------------------------------------------

    private void showAbout() {

        JOptionPane.showMessageDialog(
                this,

                "HUFFMAN TEXT COMPRESSOR\n\n"

                        + "A lossless text compression application "
                        + "based on Huffman Coding.\n\n"

                        + "Features:\n"
                        + "• Lossless text compression\n"
                        + "• Huffman frequency analysis\n"
                        + "• Binary Huffman codes\n"
                        + "• Huffman tree visualization\n"
                        + "• Compression statistics\n"
                        + "• File decompression\n"
                        + "• Interactive tree zooming\n"
                        + "• Complete tree fit-to-view\n\n"

                        + "Algorithm:\n"
                        + "Huffman Coding / Greedy Algorithm\n\n"

                        + "Implementation:\n"
                        + "Java Swing",

                "About Project",

                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // HUFFMAN TREE PANEL
    // ============================================================

    private static class TreePanel
            extends JPanel {

        private HuffmanNode root;

        private double zoom =
                1.0;

        private final List<NodePosition>
                positions =
                new ArrayList<>();

        private int treeWidth =
                1000;

        private int treeHeight =
                700;

        private static final int
                HORIZONTAL_SPACING = 90;

        private static final int
                VERTICAL_SPACING = 105;

        private static final int
                LEFT_MARGIN = 70;

        private static final int
                TOP_MARGIN = 70;

        private static final int
                NODE_RADIUS = 25;

        private static final int
                MIN_WIDTH = 1000;

        private static final int
                MIN_HEIGHT = 700;

        TreePanel() {

            setBackground(
                    Color.WHITE
            );

            setOpaque(true);

            setPreferredSize(
                    new Dimension(
                            MIN_WIDTH,
                            MIN_HEIGHT
                    )
            );
        }

        // --------------------------------------------------------
        // SET TREE
        // --------------------------------------------------------

        public void setTree(
                HuffmanNode root) {

            this.root = root;

            zoom = 1.0;

            rebuildLayout();

            repaint();
        }

        // --------------------------------------------------------
        // CLEAR TREE
        // --------------------------------------------------------

        public void clearTree() {

            root = null;

            positions.clear();

            treeWidth = MIN_WIDTH;

            treeHeight = MIN_HEIGHT;

            setPreferredSize(
                    new Dimension(
                            treeWidth,
                            treeHeight
                    )
            );

            revalidate();

            repaint();
        }

        // --------------------------------------------------------
        // RESET ZOOM
        // --------------------------------------------------------

        public void resetZoom() {

            zoom = 1.0;

            rebuildLayout();

            repaint();
        }

        // --------------------------------------------------------
        // GET ZOOM
        // --------------------------------------------------------

        public double getZoom() {

            return zoom;
        }

        // --------------------------------------------------------
        // ZOOM IN
        // --------------------------------------------------------

        public void zoomIn() {

            zoom *= 1.20;

            if (zoom > 4.0) {

                zoom = 4.0;
            }

            rebuildLayout();

            repaint();
        }

        // --------------------------------------------------------
        // ZOOM OUT
        // --------------------------------------------------------

        public void zoomOut() {

            zoom /= 1.20;

            if (zoom < 0.25) {

                zoom = 0.25;
            }

            rebuildLayout();

            repaint();
        }

        // --------------------------------------------------------
        // FIT TREE
        // --------------------------------------------------------

        public void fitToView(
                JScrollPane scrollPane) {

            if (root == null) {

                return;
            }

            // First build the tree at normal size.
            zoom = 1.0;

            rebuildLayout();

            Dimension viewportSize =
                    scrollPane
                            .getViewport()
                            .getExtentSize();

            int availableWidth =
                    Math.max(
                            300,
                            viewportSize.width - 20
                    );

            int availableHeight =
                    Math.max(
                            250,
                            viewportSize.height - 20
                    );

            double widthScale =
                    (double) availableWidth
                            / treeWidth;

            double heightScale =
                    (double) availableHeight
                            / treeHeight;

            double fitScale =
                    Math.min(
                            widthScale,
                            heightScale
                    );

            // Don't make it microscopic.
            if (fitScale < 0.25) {

                fitScale = 0.25;
            }

            if (fitScale > 1.0) {

                fitScale = 1.0;
            }

            zoom = fitScale;

            rebuildLayout();

            SwingUtilities.invokeLater(
                    () -> {

                        JScrollBar horizontal =
                                scrollPane
                                        .getHorizontalScrollBar();

                        JScrollBar vertical =
                                scrollPane
                                        .getVerticalScrollBar();

                        horizontal.setValue(
                                Math.max(
                                        0,
                                        (horizontal
                                                .getMaximum()
                                                - horizontal
                                                .getVisibleAmount())
                                                / 2
                                )
                        );

                        vertical.setValue(0);

                        revalidate();

                        repaint();
                    }
            );
        }

        // --------------------------------------------------------
        // REBUILD LAYOUT
        // --------------------------------------------------------

        private void rebuildLayout() {

            positions.clear();

            if (root == null) {

                treeWidth =
                        MIN_WIDTH;

                treeHeight =
                        MIN_HEIGHT;

                setPreferredSize(
                        new Dimension(
                                treeWidth,
                                treeHeight
                        )
                );

                revalidate();

                return;
            }

            int leafCount =
                    countLeaves(root);

            int depth =
                    getDepth(root);

            /*
             * IMPORTANT:
             *
             * Width is based on the number of leaves.
             * This prevents the right side of the tree
             * from being cut off.
             */

            int naturalWidth =
                    Math.max(
                            MIN_WIDTH,
                            LEFT_MARGIN * 2
                                    + leafCount
                                    * HORIZONTAL_SPACING
                    );

            int naturalHeight =
                    Math.max(
                            MIN_HEIGHT,
                            TOP_MARGIN * 2
                                    + depth
                                    * VERTICAL_SPACING
                    );

            treeWidth =
                    (int)
                            Math.ceil(
                                    naturalWidth
                                            * zoom
                            );

            treeHeight =
                    (int)
                            Math.ceil(
                                    naturalHeight
                                            * zoom
                            );

            setPreferredSize(
                    new Dimension(
                            treeWidth,
                            treeHeight
                    )
            );

            // Assign positions.
            int[] leafIndex =
                    {0};

            calculatePositions(
                    root,
                    0,
                    leafIndex,
                    naturalWidth
            );

            // Scale positions.
            for (NodePosition position
                    : positions) {

                position.x *= zoom;

                position.y *= zoom;
            }

            revalidate();
        }

        // --------------------------------------------------------
        // COUNT LEAVES
        // --------------------------------------------------------

        private int countLeaves(
                HuffmanNode node) {

            if (node == null) {

                return 0;
            }

            if (node.isLeaf()) {

                return 1;
            }

            return countLeaves(node.left)
                    + countLeaves(node.right);
        }

        // --------------------------------------------------------
        // TREE DEPTH
        // --------------------------------------------------------

        private int getDepth(
                HuffmanNode node) {

            if (node == null) {

                return 0;
            }

            if (node.isLeaf()) {

                return 1;
            }

            return 1
                    + Math.max(
                            getDepth(node.left),
                            getDepth(node.right)
                    );
        }

        // --------------------------------------------------------
        // CALCULATE POSITIONS
        // --------------------------------------------------------

        private void calculatePositions(
                HuffmanNode node,
                int depth,
                int[] leafIndex,
                int width) {

            if (node == null) {

                return;
            }

            if (node.isLeaf()) {

                int x =
                        LEFT_MARGIN
                                + leafIndex[0]
                                * HORIZONTAL_SPACING;

                int y =
                        TOP_MARGIN
                                + depth
                                * VERTICAL_SPACING;

                positions.add(
                        new NodePosition(
                                node,
                                x,
                                y
                        )
                );

                leafIndex[0]++;

                return;
            }

            calculatePositions(
                    node.left,
                    depth + 1,
                    leafIndex,
                    width
            );

            calculatePositions(
                    node.right,
                    depth + 1,
                    leafIndex,
                    width
            );

            NodePosition leftPosition =
                    findPosition(node.left);

            NodePosition rightPosition =
                    findPosition(node.right);

            double x;

            if (leftPosition != null
                    && rightPosition != null) {

                x =
                        (leftPosition.x
                                + rightPosition.x)
                                / 2;

            } else if (leftPosition != null) {

                x =
                        leftPosition.x;

            } else if (rightPosition != null) {

                x =
                        rightPosition.x;

            } else {

                x =
                        width / 2.0;
            }

            int y =
                    TOP_MARGIN
                            + depth
                            * VERTICAL_SPACING;

            positions.add(
                    new NodePosition(
                            node,
                            x,
                            y
                    )
            );
        }

        // --------------------------------------------------------
        // FIND POSITION
        // --------------------------------------------------------

        private NodePosition findPosition(
                HuffmanNode node) {

            if (node == null) {

                return null;
            }

            for (NodePosition position
                    : positions) {

                if (position.node == node) {

                    return position;
                }
            }

            return null;
        }

        // --------------------------------------------------------
        // PAINT
        // --------------------------------------------------------

        @Override
        protected void paintComponent(
                Graphics graphics) {

            super.paintComponent(
                    graphics
            );

            Graphics2D g =
                    (Graphics2D)
                            graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            if (root == null) {

                g.setColor(
                        new Color(
                                100,
                                105,
                                115
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
                        "Compress a text file to display the complete Huffman Tree.";

                FontMetrics metrics =
                        g.getFontMetrics();

                int x =
                        Math.max(
                                20,
                                (getWidth()
                                        - metrics
                                        .stringWidth(
                                                message
                                        ))
                                        / 2
                        );

                int y =
                        Math.max(
                                40,
                                getHeight() / 2
                        );

                g.drawString(
                        message,
                        x,
                        y
                );

                g.dispose();

                return;
            }

            // Draw edges first.
            drawEdges(
                    g,
                    root
            );

            // Draw nodes.
            drawNodes(
                    g
            );

            g.dispose();
        }

        // --------------------------------------------------------
        // DRAW EDGES
        // --------------------------------------------------------

        private void drawEdges(
                Graphics2D g,
                HuffmanNode node) {

            if (node == null
                    || node.isLeaf()) {

                return;
            }

            NodePosition parent =
                    findPosition(node);

            if (parent == null) {

                return;
            }

            if (node.left != null) {

                NodePosition child =
                        findPosition(
                                node.left
                        );

                if (child != null) {

                    drawConnection(
                            g,
                            parent,
                            child,
                            "0"
                    );
                }

                drawEdges(
                        g,
                        node.left
                );
            }

            if (node.right != null) {

                NodePosition child =
                        findPosition(
                                node.right
                        );

                if (child != null) {

                    drawConnection(
                            g,
                            parent,
                            child,
                            "1"
                    );
                }

                drawEdges(
                        g,
                        node.right
                );
            }
        }

        // --------------------------------------------------------
        // DRAW CONNECTION
        // --------------------------------------------------------

        private void drawConnection(
                Graphics2D g,
                NodePosition parent,
                NodePosition child,
                String label) {

            int x1 =
                    (int) parent.x;

            int y1 =
                    (int) parent.y;

            int x2 =
                    (int) child.x;

            int y2 =
                    (int) child.y;

            g.setStroke(
                    new BasicStroke(
                            1.5f
                    )
            );

            g.setColor(
                    new Color(
                            90,
                            95,
                            105
                    )
            );

            g.drawLine(
                    x1,
                    y1,
                    x2,
                    y2
            );

            // Position of 0 / 1 label.
            int labelX =
                    (x1 + x2) / 2;

            int labelY =
                    (y1 + y2) / 2;

            g.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            Math.max(
                                    11,
                                    (int)
                                            (14
                                                    * zoom)
                            )
                    )
            );

            FontMetrics metrics =
                    g.getFontMetrics();

            int padding = 4;

            int boxWidth =
                    metrics.stringWidth(
                            label
                    ) + padding * 2;

            int boxHeight =
                    metrics.getHeight();

            g.setColor(
                    Color.WHITE
            );

            g.fillRoundRect(
                    labelX - boxWidth / 2,
                    labelY - boxHeight / 2,
                    boxWidth,
                    boxHeight,
                    8,
                    8
            );

            g.setColor(
                    BLUE
            );

            g.drawString(
                    label,
                    labelX
                            - metrics.stringWidth(
                            label
                    ) / 2,
                    labelY
                            + metrics.getAscent()
                            / 2
            );
        }

        // --------------------------------------------------------
        // DRAW NODES
        // --------------------------------------------------------

        private void drawNodes(
                Graphics2D g) {

            for (NodePosition position
                    : positions) {

                HuffmanNode node =
                        position.node;

                int radius =
                        Math.max(
                                16,
                                (int)
                                        (NODE_RADIUS
                                                * zoom)
                        );

                int x =
                        (int) position.x;

                int y =
                        (int) position.y;

                if (node.isLeaf()) {

                    g.setColor(
                            new Color(
                                    220,
                                    245,
                                    226
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

                g.fillOval(
                        x - radius,
                        y - radius,
                        radius * 2,
                        radius * 2
                );

                g.setColor(
                        new Color(
                                70,
                                75,
                                85
                        )
                );

                g.setStroke(
                        new BasicStroke(
                                1.5f
                        )
                );

                g.drawOval(
                        x - radius,
                        y - radius,
                        radius * 2,
                        radius * 2
                );

                String label;

                if (node.isLeaf()) {

                    label =
                            displayNodeCharacter(
                                    node.character
                            )
                                    + ":"
                                    + node.frequency;

                } else {

                    label =
                            String.valueOf(
                                    node.frequency
                            );
                }

                int fontSize =
                        Math.max(
                                9,
                                (int)
                                        (13
                                                * zoom)
                        );

                g.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                fontSize
                        )
                );

                FontMetrics metrics =
                        g.getFontMetrics();

                // If label is too wide, reduce it.
                if (metrics.stringWidth(label)
                        > radius * 2 - 4) {

                    label =
                            node.isLeaf()
                                    ? String.valueOf(
                                    node.frequency
                            )
                                    : String.valueOf(
                                    node.frequency
                            );
                }

                metrics =
                        g.getFontMetrics();

                int textWidth =
                        metrics.stringWidth(
                                label
                        );

                int textX =
                        x - textWidth / 2;

                int textY =
                        y
                                + (metrics
                                .getAscent()
                                - metrics
                                .getDescent())
                                / 2;

                g.setColor(
                        new Color(
                                30,
                                35,
                                45
                        )
                );

                g.drawString(
                        label,
                        textX,
                        textY
                );
            }
        }

        // --------------------------------------------------------
        // DISPLAY TREE CHARACTER
        // --------------------------------------------------------

        private String displayNodeCharacter(
                char c) {

            switch (c) {

                case '\n':
                    return "NL";

                case '\r':
                    return "CR";

                case '\t':
                    return "TAB";

                case ' ':
                    return "SP";

                default:
                    return String.valueOf(c);
            }
        }

        // --------------------------------------------------------
        // NODE POSITION
        // --------------------------------------------------------

        private static class NodePosition {

            HuffmanNode node;

            double x;

            double y;

            NodePosition(
                    HuffmanNode node,
                    double x,
                    double y) {

                this.node = node;

                this.x = x;

                this.y = y;
            }
        }
    }

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (Exception ignored) {
                    }

                    new CompressionGUI();
                }
        );
    }
}