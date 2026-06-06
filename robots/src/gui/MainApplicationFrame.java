package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.util.prefs.Preferences;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame()
    {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        addWindow(createLogWindow());

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);
        WindowStateManager.restoreInternalFrame(gameWindow);
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
        WindowStateManager.restoreFrame(this);
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        WindowStateManager.restoreInternalFrame(logWindow);
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createLookAndFeelMenu()
    {
        JMenu menu = new JMenu("Режим отображения");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
            "Управление режимом отображения приложения");

        menu.add(createSystemLookAndFeelItem());
        menu.add(createCrossplatformLookAndFeelItem());
        return menu;
    }

    private JMenuItem createSystemLookAndFeelItem()
    {
        JMenuItem item = new JMenuItem("Системная схема", KeyEvent.VK_S);
        item.addActionListener(event -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return item;
    }

    private JMenuItem createCrossplatformLookAndFeelItem()
    {
        JMenuItem item = new JMenuItem("Универсальная схема", KeyEvent.VK_U);
        item.addActionListener(event -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return item;
    }

    private JMenu createTestMenu()
    {
        JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        menu.add(createAddLogMessageItem());
        return menu;
    }

    private JMenuItem createAddLogMessageItem()
    {
        JMenuItem item = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        item.addActionListener(event -> Logger.debug("Новая строка"));
        return item;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu("Выход");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.add(createExitItem());
        return menu;
    }

    private JMenuItem createExitItem()
    {
        JMenuItem item = new JMenuItem("Выход", KeyEvent.VK_Q);
        item.addActionListener(event -> onWindowClosing());
        return item;
    }

    private void onWindowClosing()
    {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText",  "Нет");

        int result = JOptionPane.showConfirmDialog(
            this,
            "Вы уверены, что хотите выйти?",
            "Подтверждение выхода",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION)
        {
            WindowStateManager.saveFrame(this);
            for (JInternalFrame frame : desktopPane.getAllFrames())
                WindowStateManager.saveInternalFrame(frame);
            dispose();
            System.exit(0);
        }
    }
}