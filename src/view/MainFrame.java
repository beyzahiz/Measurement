package view;

import controller.MeasurementController;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {
    private final MeasurementController controller;
    private JTextField folderPathField;
    private JCheckBox averageBox, maximumBox, minimumBox, stdDevBox, frequencyBox, medianBox;
    private JTextArea messageArea;

    public MainFrame() {
        controller = new MeasurementController();
        setupUI();
    }

    private void setupUI() {
        setTitle("Ölçüm Analiz Programı");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(600, 400));

        // Folder selection panel
        JPanel folderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        folderPathField = new JTextField(30);
        folderPathField.setEditable(false);
        JButton selectFolderButton = new JButton("Klasör Seç");
        selectFolderButton.addActionListener(e -> selectFolder());
        folderPanel.add(new JLabel("Klasör: "));
        folderPanel.add(folderPathField);
        folderPanel.add(selectFolderButton);

        // Checkboxes panel
        JPanel checkboxPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        averageBox = new JCheckBox("Ortalama");
        maximumBox = new JCheckBox("Maximum");
        minimumBox = new JCheckBox("Minimum");
        stdDevBox = new JCheckBox("Standart Sapma");
        frequencyBox = new JCheckBox("Frekans");
        medianBox = new JCheckBox("Medyan");

        checkboxPanel.add(averageBox);
        checkboxPanel.add(maximumBox);
        checkboxPanel.add(minimumBox);
        checkboxPanel.add(stdDevBox);
        checkboxPanel.add(frequencyBox);
        checkboxPanel.add(medianBox);

        // Calculate button
        JButton calculateButton = new JButton("Hesapla");
        calculateButton.addActionListener(e -> calculate());

        // Message area
        messageArea = new JTextArea(5, 40);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(folderPanel, BorderLayout.NORTH);
        topPanel.add(checkboxPanel, BorderLayout.CENTER);
        topPanel.add(calculateButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void selectFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Ölçüm Klasörünü Seçin");

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            folderPathField.setText(selectedFolder.getAbsolutePath());
            controller.setSelectedFolder(selectedFolder.getAbsolutePath());
            messageArea.setText("Klasör seçildi: " + selectedFolder.getAbsolutePath());
        }
    }

    private void calculate() {
        if (folderPathField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen önce bir klasör seçin!", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controller.calculate(
                averageBox.isSelected(),
                maximumBox.isSelected(),
                minimumBox.isSelected(),
                stdDevBox.isSelected(),
                frequencyBox.isSelected(),
                medianBox.isSelected()
            );

            String resultPath = new File(folderPathField.getText()).getParent() + File.separator + "sonuc";
            messageArea.setText("Hesaplamalar başarı ile yapılmıştır.\nSonuçların olduğu klasör: " + resultPath);
        } catch (Exception e) {
            messageArea.setText("Hesaplama esnasında hata ile karşılaşılmıştır: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage(), 
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
