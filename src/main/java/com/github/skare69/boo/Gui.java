package com.github.skare69.boo;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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
    private FlowLayout flowLayout = new FlowLayout();

    private JButton buttonChoosePathToScan;
    private JFileChooser fileChooserPathToScan;
    private JButton buttonStartScan;
    private JCheckBox checkBoxNotFlat;
    private JCheckBox checkBoxHidden;
    private JComboBox comboBoxFileSizes;
    private JLabel labelMaxFileSize;
    private JLabel labelPathToScan;
    private JLabel labelResults;
    private JList listResults;
    private JPanel panelAll;
    private JScrollPane scrollPaneListResults;
    private JSeparator separator;
    private JTextField textFieldMaxFileSize;
    private JTextField textFieldPathToScan;
    private JPanel titledPanelOptions;

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
        setMinimumSize(new Dimension(411, 355));
        setLocationRelativeTo(null);

        panelAll = new JPanel();
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
        comboBoxFileSizes = new JComboBox();
        labelResults = new JLabel();
        separator = new JSeparator();
        scrollPaneListResults = new JScrollPane();
        listResults = new JList();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        textFieldPathToScan.setText("");

        buttonChoosePathToScan.setText("Choose directory");
        buttonChoosePathToScan.addActionListener(this);

        fileChooserPathToScan.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserPathToScan.setAcceptAllFileFilterUsed(false);
        fileChooserPathToScan.setCurrentDirectory(new File("."));

        buttonStartScan.setText("Start scan");
        buttonStartScan.addActionListener(this);
        labelPathToScan.setText("Directory to scan");
        titledPanelOptions.setBorder(BorderFactory.createTitledBorder("Options"));

        checkBoxHidden.setText("Include hidden files");
        checkBoxHidden.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkBoxHiddenActionPerformed(evt);
            }
        });

        checkBoxNotFlat.setSelected(true);
        checkBoxNotFlat.setText("Include sub-directories");
        checkBoxNotFlat.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkBoxFlatActionPerformed(evt);
            }
        });

        labelMaxFileSize.setText("Maximum file size to scan");

        textFieldMaxFileSize.setHorizontalAlignment(JTextField.CENTER);
        textFieldMaxFileSize.setText("5");
        textFieldMaxFileSize.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                textFieldMaxFileSizeActionPerformed(evt);
            }
        });

        comboBoxFileSizes.setModel(new DefaultComboBoxModel(new String[]{MaxFileSizes.KILO_BYTE.getValue(), MaxFileSizes.MEGA_BYTE
                .getValue(),
                MaxFileSizes.GIGA_BYTE.getValue()}));
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

        labelResults.setText("Results");

        listResults.setModel(new AbstractListModel()
        {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

            public int getSize()
            {
                return strings.length;
            }

            public Object getElementAt(int i)
            {
                return strings[i];
            }
        });
        scrollPaneListResults.setViewportView(listResults);

        GroupLayout panelAllLayout = new GroupLayout(panelAll);
        panelAll.setLayout(panelAllLayout);
        panelAllLayout.setHorizontalGroup(
                panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelAllLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(separator, GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelAllLayout.createSequentialGroup()
                                                .addComponent(labelPathToScan)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldPathToScan)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonChoosePathToScan))
                                        .addGroup(panelAllLayout.createSequentialGroup()
                                                .addComponent(titledPanelOptions, GroupLayout.DEFAULT_SIZE, javax.swing
                                                        .GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonStartScan, GroupLayout.PREFERRED_SIZE, 115, javax.swing
                                                        .GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelAllLayout.createSequentialGroup()
                                                .addGroup(panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelResults)
                                                        .addComponent(scrollPaneListResults, GroupLayout.PREFERRED_SIZE, 98,
                                                                GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        panelAllLayout.setVerticalGroup(
                panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelAllLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelAllLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelPathToScan)
                                        .addComponent(textFieldPathToScan, GroupLayout.PREFERRED_SIZE, javax.swing
                                                .GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonChoosePathToScan))
                                .addGroup(panelAllLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(panelAllLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(buttonStartScan, GroupLayout.PREFERRED_SIZE, 82, javax.swing
                                                        .GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelAllLayout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(titledPanelOptions, GroupLayout.PREFERRED_SIZE, 92, javax.swing
                                                        .GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(separator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(labelResults)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPaneListResults, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
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

    private void checkBoxHiddenActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    private void checkBoxFlatActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    private void textFieldMaxFileSizeActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    public void addComponentsToPane(final Container pane)
    {
        pane.setLayout(flowLayout);

        JLabel labelPathToScan = new JLabel("Path to scan: ");
        JFileChooser fileChooserPathToScan = new JFileChooser();
        fileChooserPathToScan.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooserPathToScan.setAcceptAllFileFilterUsed(false);
        fileChooserPathToScan.setCurrentDirectory(new File("."));

        JButton buttonScan = new JButton("Scan");
        JCheckBox optionHiddenFiles = new JCheckBox("Include hidden files");

        pane.add(labelPathToScan);
        pane.add(fileChooserPathToScan);
        pane.add(buttonScan);
        pane.add(optionHiddenFiles);
    }

    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException ex)
        {
            logger.error(ex);
        }
        catch (IllegalAccessException ex)
        {
            logger.error(ex);
        }
        catch (InstantiationException ex)
        {
            logger.error(ex);
        }
        catch (ClassNotFoundException ex)
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
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(buttonChoosePathToScan))
        {
            int returnValue = fileChooserPathToScan.showOpenDialog(Gui.this);
            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooserPathToScan.getSelectedFile();
                textFieldPathToScan.setText(selectedFile.getAbsolutePath());
            }
            else if (returnValue == JFileChooser.CANCEL_OPTION)
            {

            }
            else if (returnValue == JFileChooser.ERROR_OPTION)
            {
                logger.error("an error occurred while choosing the directory to scan");
            }
        }
        else if (e.getSource().equals(buttonStartScan))
        {
            java.util.List<String> argsList = new ArrayList<>(4);
            if (textFieldPathToScan.getText() != null && !textFieldPathToScan.getText().isEmpty())
            {
                // add -t <getText()>
                argsList.add("-t");
                argsList.add(textFieldPathToScan.getText());
            }

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
                int byteMultiplier = 1024;
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
            String[] args = argsList.toArray(new String[4]);

            DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder(args);
            duplicateFileFinder.scanDirectory();

            // TODO replace this with something like: add found duplicate groups to gui etc.
            DuplicateFileFinder.displayResults(duplicateFileFinder);
        }
    }

    private static enum MaxFileSizes
    {
        KILO_BYTE("kB"),
        MEGA_BYTE("MB"),
        GIGA_BYTE("");

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
