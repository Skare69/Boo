package com.github.skare69.boo;

import javax.swing.*;

/**
 * The Java Swing based GUI for Boo.
 * <p/>
 * User: Dominik
 * Date: 28.02.2015
 * Time: 12:07
 */
public class Gui
{
    public static final String NAME = "Boo";

    private static void test()
    {
        //Create and set up the window.
        JFrame frame = new JFrame(NAME);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                test();
            }
        });
    }
}
