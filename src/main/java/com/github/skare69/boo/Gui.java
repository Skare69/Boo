package com.github.skare69.boo;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Java Swing based GUI for Boo.
 * <p/>
 * User: Dominik
 * Date: 28.02.2015
 * Time: 12:07
 */
public class Gui
        extends JFrame
        implements ActionListener
{
    private static Logger logger = Logger.getLogger(Gui.class);

    public static final String NAME = "Boo";

    private JFileChooser fileChooserPathToScan;
    private JButton buttonChoosePathToScan;
    private JButton buttonStartScan;
    private JCheckBox checkBoxNotFlat;
    private JCheckBox checkBoxHidden;
    private JComboBox<String> comboBoxFileSizes;
    private JLabel labelMaxFileSize;
    private JLabel labelPathToScan;
    private JLabel labelResults;
    private JList<String> listResults;
    private JList<String> listResultDetails;
    private JPanel panelAll;
    private JSeparator separator;
    private JTextField textFieldMaxFileSize;
    private JTextField textFieldPathToScan;
    private JPanel titledPanelOptions;
    private JPanel panelRun;
    private JButton buttonProgressDetails;
    private JLabel labelScanProgress;
    private JScrollPane scrollPaneProgressResults;
    private JScrollPane scrollPaneResults;
    private JSeparator separatorProgressResults;
    private JTextArea textAreaProgressResults;
    private JPanel panelResults;
    private JPanel panelResultsDetail;
    private DefaultListModel<String> resultsListModel;
    private DefaultListModel<String> resultDetailsListModel;
    private JScrollPane scrollPaneResultDetails;
    private final DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder(null);
    private JButton buttonShow;
    private JButton buttonDelete;

    public Gui() throws HeadlessException
    {
        super(NAME);
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(Gui.class.getClassLoader().getResourceAsStream("boo.ico"));
//        Image image = null;
//        try
//        {
//            image = ImageIO.read(bufferedInputStream);
//        }
//        catch (IOException e)
//        {
//            logger.error(e);
//        }
//        finally
//        {
//            try
//            {
//                bufferedInputStream.close();
//            }
//            catch (IOException e)
//            {
//                logger.error(e);
//            }
//        }
//        setIconImage(image);
        initComponents();
    }

    private void initComponents()
    {
        setMinimumSize(new Dimension(811, 455));
        setLocationRelativeTo(null);

        panelAll = new JPanel();
        panelRun = new JPanel();
        textFieldPathToScan = new JTextField();
        buttonChoosePathToScan = new JButton();
        fileChooserPathToScan = new JFileChooser();
        buttonStartScan = new JButton();
        labelPathToScan = new JLabel();
        titledPanelOptions = new JPanel();
        checkBoxHidden = new JCheckBox();
        checkBoxNotFlat = new JCheckBox();
        labelMaxFileSize = new JLabel();
        textFieldMaxFileSize = new JTextField();
        comboBoxFileSizes = new JComboBox<>();
        labelResults = new JLabel();
        separator = new JSeparator();

        buttonShow = new JButton();
        buttonShow.setText("Show");
        buttonShow.setEnabled(false);
        buttonShow.addActionListener(this);

        buttonDelete = new JButton();
        buttonDelete.setText("Delete");
        buttonDelete.setEnabled(false);
        buttonDelete.addActionListener(this);

        labelScanProgress = new JLabel();
        scrollPaneProgressResults = new JScrollPane();

        getRootPane().setDefaultButton(buttonStartScan);

        GuiLoggingAppender guiLoggingAppender = new GuiLoggingAppender();
        guiLoggingAppender.setTextArea(new JTextArea());
        textAreaProgressResults = guiLoggingAppender.getTextArea();

        Logger.getRootLogger().addAppender(guiLoggingAppender);

        buttonProgressDetails = new JButton();
        separatorProgressResults = new JSeparator();
        scrollPaneResults = new JScrollPane();
        scrollPaneResultDetails = new JScrollPane();
        listResults = new JList<>();
        listResultDetails = new JList<>();
        panelResults = new JPanel();
        panelResults.setVisible(false);
        panelResultsDetail = new JPanel();
        panelRun.setVisible(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        textFieldPathToScan.setText("");

        buttonChoosePathToScan.setText("Choose directory");
        buttonChoosePathToScan.addActionListener(this);

        fileChooserPathToScan.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserPathToScan.setAcceptAllFileFilterUsed(false);
        fileChooserPathToScan.setCurrentDirectory(new File(""));

        buttonStartScan.setText("Start scan");
        buttonStartScan.addActionListener(this);
        labelPathToScan.setText("Directory to scan");
        titledPanelOptions.setBorder(BorderFactory.createTitledBorder("Options"));

        checkBoxHidden.setText("Include hidden files");

        checkBoxNotFlat.setSelected(true);
        checkBoxNotFlat.setText("Include sub-directories");

        labelMaxFileSize.setText("Maximum file size to scan");

        textFieldMaxFileSize.setHorizontalAlignment(JTextField.CENTER);
        textFieldMaxFileSize.setText("5");

        comboBoxFileSizes.setModel(new DefaultComboBoxModel<>(new String[]{MaxFileSizes.KILO_BYTE.getValue(), MaxFileSizes.MEGA_BYTE
                .getValue(), MaxFileSizes.GIGA_BYTE.getValue()}));
        comboBoxFileSizes.setSelectedIndex(1);

        GroupLayout titledPanelOptionsLayout = new GroupLayout(titledPanelOptions);
        titledPanelOptions.setLayout(titledPanelOptionsLayout);
        titledPanelOptionsLayout.setHorizontalGroup(
                titledPanelOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(titledPanelOptionsLayout.createSequentialGroup()
                                .addGroup(titledPanelOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(checkBoxHidden)
                                        .addGroup(titledPanelOptionsLayout.createSequentialGroup()
                                                .addComponent(labelMaxFileSize)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldMaxFileSize, GroupLayout.PREFERRED_SIZE, 25, javax
                                                        .swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBoxFileSizes, GroupLayout.PREFERRED_SIZE, javax.swing
                                                        .GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(checkBoxNotFlat))
                                .addContainerGap(64, Short.MAX_VALUE))
        );
        titledPanelOptionsLayout.setVerticalGroup(
                titledPanelOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(titledPanelOptionsLayout.createSequentialGroup()
                                .addComponent(checkBoxHidden)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxNotFlat)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(titledPanelOptionsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelMaxFileSize)
                                        .addComponent(textFieldMaxFileSize, GroupLayout.PREFERRED_SIZE, javax.swing
                                                .GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBoxFileSizes, GroupLayout.PREFERRED_SIZE, GroupLayout
                                                .DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelScanProgress.setText("Scan in progress...");

        textAreaProgressResults.setColumns(20);
        textAreaProgressResults.setRows(15);
        textAreaProgressResults.setEditable(false);
        textAreaProgressResults.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        DefaultCaret caret = (DefaultCaret)textAreaProgressResults.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPaneProgressResults.setViewportView(textAreaProgressResults);
        scrollPaneProgressResults.setVisible(false);

        buttonProgressDetails.setText("Show Details");
        buttonProgressDetails.addActionListener(this);

        labelResults.setText("Duplicated files found:");

        resultsListModel = new DefaultListModel<>();
        listResults.setModel(resultsListModel);
        scrollPaneResults.setViewportView(listResults);
        listResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listResults.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    Object selectedObject = ((JList)e.getSource()).getSelectedValue();
                    if (selectedObject == null)
                    {
                        resultDetailsListModel.clear();
                        return;
                    }

                    resultDetailsListModel.clear();
                    String selectedValue = selectedObject.toString();
                    if (duplicateFileFinder.getFileHashesMap().containsKey(selectedValue))
                    {
                        for (String filePath : duplicateFileFinder.getFileHashesMap().get(selectedValue))
                        {
                            resultDetailsListModel.addElement(filePath);
                        }
                    }
                }
            }
        });

        resultDetailsListModel = new DefaultListModel<>();
        listResultDetails.setModel(resultDetailsListModel);
        scrollPaneResultDetails.setViewportView(listResultDetails);
        listResultDetails.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listResultDetails.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    List selectedValues = ((JList)e.getSource()).getSelectedValuesList();

                    if (selectedValues.isEmpty())
                    {
                        buttonShow.setEnabled(false);
                        buttonDelete.setEnabled(false);
                    }
                    else
                    {
                        if (selectedValues.size() == 1)
                        {
                            buttonShow.setEnabled(true);
                        }
                        else
                        {
                            buttonShow.setEnabled(false);
                        }
                        buttonDelete.setEnabled(true);
                    }
                }
            }
        });

        GroupLayout panelResultsDetailLayout = new GroupLayout(panelResultsDetail);
        panelResultsDetail.setLayout(panelResultsDetailLayout);
        panelResultsDetailLayout.setHorizontalGroup(
                panelResultsDetailLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultsDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonShow, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(buttonDelete, javax.swing.GroupLayout.Alignment.TRAILING)
                        )
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(scrollPaneResultDetails, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
        );
        panelResultsDetailLayout.setVerticalGroup(
                panelResultsDetailLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultsDetailLayout.createSequentialGroup()
                                        .addComponent(buttonShow)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(buttonDelete)
//                                        .addGap(0, 0, Short.MAX_VALUE)
                        )
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(scrollPaneResultDetails, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );

        GroupLayout panelResultsLayout = new GroupLayout(panelResults);
        panelResults.setLayout(panelResultsLayout);
        panelResultsLayout.setHorizontalGroup(
                panelResultsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultsLayout.createSequentialGroup()
                                .addComponent(labelResults)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(panelResultsLayout.createSequentialGroup()
                                .addComponent(scrollPaneResults, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelResultsDetail, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelResultsLayout.setVerticalGroup(
                panelResultsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultsLayout.createSequentialGroup()
                                .addComponent(labelResults)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelResultsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPaneResults, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                        .addComponent(panelResultsDetail, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short
                                                .MAX_VALUE)))
        );

        GroupLayout panelRunLayout = new GroupLayout(panelRun);
        panelRun.setLayout(panelRunLayout);
        panelRunLayout.setHorizontalGroup(
                panelRunLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelRunLayout.createSequentialGroup()
                                .addComponent(labelScanProgress)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonProgressDetails))
                        .addComponent(scrollPaneProgressResults)
                        .addComponent(separatorProgressResults, GroupLayout.Alignment.TRAILING)
                        .addComponent(panelResults, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelRunLayout.setVerticalGroup(
                panelRunLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelRunLayout.createSequentialGroup()
                                .addGroup(panelRunLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelScanProgress)
                                        .addComponent(buttonProgressDetails))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPaneProgressResults, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(separatorProgressResults, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelResults, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout panelAllLayout = new GroupLayout(panelAll);
        panelAll.setLayout(panelAllLayout);
        panelAllLayout.setHorizontalGroup(
                panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, panelAllLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelAllLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(panelRun, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(separator)
                                        .addGroup(GroupLayout.Alignment.LEADING, panelAllLayout.createSequentialGroup()
                                                .addComponent(labelPathToScan)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldPathToScan)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonChoosePathToScan))
                                        .addGroup(GroupLayout.Alignment.LEADING, panelAllLayout.createSequentialGroup()
                                                .addComponent(titledPanelOptions, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonStartScan, GroupLayout.PREFERRED_SIZE, 115, GroupLayout
                                                        .PREFERRED_SIZE)))
                                .addContainerGap())
        );
        panelAllLayout.setVerticalGroup(
                panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelAllLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelAllLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelPathToScan)
                                        .addComponent(textFieldPathToScan, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonChoosePathToScan))
                                .addGroup(panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(panelAllLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(buttonStartScan, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelAllLayout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(titledPanelOptions, GroupLayout.PREFERRED_SIZE, 92, GroupLayout
                                                        .PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(separator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelRun, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panelAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panelAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    public static void main(String[] args) throws InstantiationException
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException | IllegalAccessException | ClassNotFoundException ex)
        {
            logger.error(ex);
        }

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new Gui().setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if (actionEvent.getSource().equals(buttonChoosePathToScan))
        {
            int returnValue = fileChooserPathToScan.showOpenDialog(Gui.this);
            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooserPathToScan.getSelectedFile();
                textFieldPathToScan.setText(selectedFile.getAbsolutePath());
            }
            else if (returnValue == JFileChooser.ERROR_OPTION)
            {
                logger.error("an error occurred while choosing the directory to scan");
            }
        }
        else if (actionEvent.getSource().equals(buttonStartScan))
        {
            List<String> argsList = new ArrayList<>(4);
            if (textFieldPathToScan.getText() != null && !textFieldPathToScan.getText().isEmpty())
            {
                // add -t <getText()>
                argsList.add("-t");
                argsList.add(textFieldPathToScan.getText());
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Directory path to scan cannot be empty.", "Configuration error", JOptionPane
                        .ERROR_MESSAGE);
                return;
            }

            argsList.add("-v");

            if (!checkBoxNotFlat.isSelected())
            {
                // add -f option
                argsList.add("-f");
            }

            if (checkBoxHidden.isSelected())
            {
                // add -s option
                argsList.add("-s");
            }

            if (textFieldMaxFileSize.getText() != null && !textFieldMaxFileSize.getText().isEmpty())
            {
                long byteMultiplier = 1;
                if (String.valueOf(comboBoxFileSizes.getSelectedItem()).equals(MaxFileSizes.KILO_BYTE.getValue()))
                {
                    byteMultiplier *= 1024;
                }
                else if (String.valueOf(comboBoxFileSizes.getSelectedItem()).equals(MaxFileSizes.MEGA_BYTE.getValue()))
                {
                    byteMultiplier *= 1024 * 1024;
                }
                else if (String.valueOf(comboBoxFileSizes.getSelectedItem()).equals(MaxFileSizes.GIGA_BYTE.getValue()))
                {
                    byteMultiplier *= 1024 * 1024 * 1024;
                }
                // add -m <getText() + dropdown calculation>
                argsList.add("-m");
                argsList.add(String.valueOf(Integer.valueOf(textFieldMaxFileSize.getText()) * byteMultiplier));
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Maximum file size must be specified.", "Configuration error", JOptionPane
                        .ERROR_MESSAGE);
                return;
            }

            String[] args = argsList.toArray(new String[4]);

            buttonStartScan.setEnabled(false);
            textAreaProgressResults.setText("");
            labelScanProgress.setText("Scan in progress...");
            panelResults.setVisible(false);
            panelRun.setVisible(true);
            resultsListModel.clear();

            duplicateFileFinder.reInitializeConfig(args);

            SwingWorker<Integer, Integer> swingWorker = new SwingWorker<Integer, Integer>()
            {
                @Override
                protected Integer doInBackground() throws Exception
                {
                    duplicateFileFinder.scanDirectory();
                    return duplicateFileFinder.getFileCount();
                }
            };

            swingWorker.addPropertyChangeListener(
                    new PropertyChangeListener()
                    {
                        public void propertyChange(PropertyChangeEvent evt)
                        {
                            if (evt.getPropertyName().equals("state") && evt.getNewValue().equals(SwingWorker.StateValue.DONE))
                            {
                                buttonStartScan.setEnabled(true);
                                labelScanProgress.setText(String.format("Scan finished: %d files scanned.",
                                        duplicateFileFinder.getFileCount()));
                                panelResults.setVisible(true);

                                duplicateFileFinder.processScannedFileMap();

                                if (duplicateFileFinder.getFileHashesMap().isEmpty())
                                {
                                    labelResults.setText("No duplicated files found.");
                                    scrollPaneResults.setVisible(false);
                                    panelResultsDetail.setVisible(false);
                                    buttonShow.setVisible(false);
                                    buttonDelete.setVisible(false);
                                    return;
                                }
                                else
                                {
                                    labelResults.setText("Duplicated files found:");
                                    scrollPaneResults.setVisible(true);
                                    panelResultsDetail.setVisible(true);
                                    buttonShow.setVisible(true);
                                    buttonDelete.setVisible(true);
                                }

                                for (Map.Entry<String, List<String>> entry : duplicateFileFinder.getFileHashesMap().entrySet())
                                {
                                    resultsListModel.addElement(entry.getKey());
                                }
                            }
                        }
                    });

            swingWorker.execute();
        }
        else if (actionEvent.getSource().equals(buttonProgressDetails))
        {
            if (buttonProgressDetails.getText().equals("Hide Details"))
            {
                scrollPaneProgressResults.setVisible(false);
                buttonProgressDetails.setText("Show Details");
            }
            else if (buttonProgressDetails.getText().equals("Show Details"))
            {
                scrollPaneProgressResults.setVisible(true);
                buttonProgressDetails.setText("Hide Details");
            }
        }
        else if (actionEvent.getSource().equals(buttonShow))
        {
            if (listResultDetails.isSelectionEmpty())
            {
                return;
            }
            String pathToFile = resultDetailsListModel.get(listResultDetails.getSelectedIndex());
            try
            {
                Desktop.getDesktop().open(new File(pathToFile));
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error opening file", JOptionPane.ERROR_MESSAGE);
                logger.error(e);
            }
        }
        else if (actionEvent.getSource().equals(buttonDelete))
        {
            if (listResultDetails.isSelectionEmpty())
            {
                return;
            }
            String pathToFile = resultDetailsListModel.get(listResultDetails.getSelectedIndex());
            int confirmDialog = JOptionPane.showConfirmDialog(this, String.format("Do you really want to delete the file %s?", pathToFile),
                    "Delete file", JOptionPane.OK_CANCEL_OPTION);

            if (confirmDialog == JOptionPane.OK_OPTION)
            {
                File file = new File(pathToFile);
                if (file.delete())
                {
                    resultDetailsListModel.remove(listResultDetails.getSelectedIndex());
                }
                else
                {
                    String message = String.format("File could not be deleted: %s", pathToFile);
                    JOptionPane.showMessageDialog(this, message, "Error deleting file",
                            JOptionPane.ERROR_MESSAGE);
                    logger.error(message);
                }
            }
        }
    }

    private static enum MaxFileSizes
    {
        KILO_BYTE("kB"),
        MEGA_BYTE("MB"),
        GIGA_BYTE("GB");

        private String value;

        MaxFileSizes(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
