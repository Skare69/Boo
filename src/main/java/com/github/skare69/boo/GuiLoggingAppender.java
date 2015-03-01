package com.github.skare69.boo;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import java.text.SimpleDateFormat;

/**
 * Custom Appender to redirect log4j logging events to the gui.
 * <p/>
 * User: Dominik
 * Date: 01.03.2015
 * Time: 12:48
 */
public class GuiLoggingAppender
        extends AppenderSkeleton
{
    private JTextArea textArea;

    public JTextArea getTextArea()
    {
        return textArea;
    }

    public void setTextArea(JTextArea textArea)
    {
        this.textArea = textArea;
    }

    @Override
    protected void append(LoggingEvent loggingEvent)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        String formattedDate = simpleDateFormat.format(System.currentTimeMillis());
        textArea.setText(textArea.getText() + formattedDate + "\t" + loggingEvent.getMessage() + System.getProperty("line.separator"));
    }

    @Override
    public boolean requiresLayout()
    {
        return false;
    }

    @Override
    public void close()
    {

    }
}
