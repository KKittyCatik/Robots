package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private JTextArea m_logContent;

    public LogWindow(LogWindowSource logSource)
    {
        super(LocaleManager.getInstance().getString("window.log"),
              true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);

        m_logContent = new JTextArea();
        m_logContent.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(m_logContent), BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
            content.append(entry.getMessage()).append("\n");
        m_logContent.setText(content.toString());
        m_logContent.setCaretPosition(m_logContent.getDocument().getLength());
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
