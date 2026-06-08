package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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

    private JMenu menuView, menuTests, menuFile, menuLanguage;
    private JMenuItem menuViewSystem, menuViewCross;
    private JMenuItem menuTestsLog;
    private JMenuItem menuFileExit;
    private JMenuItem menuLangRu, menuLangEn;

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private RobotInfoWindow robotInfoWindow;

    public MainApplicationFrame()
    {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        logWindow = createLogWindow();
        addWindow(logWindow);

        gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        robotInfoWindow = new RobotInfoWindow(gameWindow.getModel());
        robotInfoWindow.setLocation(420, 10);
        addWindow(robotInfoWindow);

        WindowStateManager.restoreFrame(this);
        WindowStateManager.restoreInternalFrame(logWindow);
        WindowStateManager.restoreInternalFrame(gameWindow);
        WindowStateManager.restoreInternalFrame(robotInfoWindow);

        setJMenuBar(generateMenuBar());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });

        LocaleManager.getInstance().addListener(this::onLocaleChanged);
    }

    private void onLocaleChanged(ResourceBundle b)
    {
        menuView.setText(b.getString("menu.view"));
        menuViewSystem.setText(b.getString("menu.view.system"));
        menuViewCross.setText(b.getString("menu.view.crossplatform"));
        menuTests.setText(b.getString("menu.tests"));
        menuTestsLog.setText(b.getString("menu.tests.log"));
        menuFile.setText(b.getString("menu.file"));
        menuFileExit.setText(b.getString("menu.file.exit"));
        menuLanguage.setText(b.getString("menu.language"));
        menuLangRu.setText(b.getString("menu.language.ru"));
        menuLangEn.setText(b.getString("menu.language.en"));
        logWindow.setTitle(b.getString("window.log"));
        gameWindow.setTitle(b.getString("window.game"));
        robotInfoWindow.setTitle(b.getString("window.robot.info"));
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(LocaleManager.getInstance().getString("log.started"));
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
        menuBar.add(menuView = createLookAndFeelMenu());
        menuBar.add(menuTests = createTestMenu());
        menuBar.add(menuFile = createFileMenu());
        menuBar.add(menuLanguage = createLanguageMenu());
        return menuBar;
    }

    private JMenu createLookAndFeelMenu()
    {
        JMenu menu = new JMenu(LocaleManager.getInstance().getString("menu.view"));
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
            "Управление режимом отображения приложения");
        menu.add(menuViewSystem = createSystemLookAndFeelItem());
        menu.add(menuViewCross = createCrossplatformLookAndFeelItem());
        return menu;
    }

    private JMenuItem createSystemLookAndFeelItem()
    {
        JMenuItem item = new JMenuItem(
            LocaleManager.getInstance().getString("menu.view.system"), KeyEvent.VK_S);
        item.addActionListener(event -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return item;
    }

    private JMenuItem createCrossplatformLookAndFeelItem()
    {
        JMenuItem item = new JMenuItem(
            LocaleManager.getInstance().getString("menu.view.crossplatform"), KeyEvent.VK_U);
        item.addActionListener(event -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return item;
    }

    private JMenu createTestMenu()
    {
        JMenu menu = new JMenu(LocaleManager.getInstance().getString("menu.tests"));
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        menu.add(menuTestsLog = createAddLogMessageItem());
        return menu;
    }

    private JMenuItem createAddLogMessageItem()
    {
        JMenuItem item = new JMenuItem(
            LocaleManager.getInstance().getString("menu.tests.log"), KeyEvent.VK_S);
        item.addActionListener(event ->
            Logger.debug(LocaleManager.getInstance().getString("log.message")));
        return item;
    }

    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu(LocaleManager.getInstance().getString("menu.file"));
        menu.setMnemonic(KeyEvent.VK_F);
        menu.add(menuFileExit = createExitItem());
        return menu;
    }

    private JMenuItem createExitItem()
    {
        JMenuItem item = new JMenuItem(
            LocaleManager.getInstance().getString("menu.file.exit"), KeyEvent.VK_Q);
        item.addActionListener(event -> onWindowClosing());
        return item;
    }

    private JMenu createLanguageMenu()
    {
        LocaleManager lm = LocaleManager.getInstance();
        JMenu menu = new JMenu(lm.getString("menu.language"));
        menuLangRu = new JMenuItem(lm.getString("menu.language.ru"));
        menuLangEn = new JMenuItem(lm.getString("menu.language.en"));
        menuLangRu.addActionListener(e ->
            LocaleManager.getInstance().setLocale(Locale.of("ru")));
        menuLangEn.addActionListener(e ->
            LocaleManager.getInstance().setLocale(Locale.ENGLISH));
        menu.add(menuLangRu);
        menu.add(menuLangEn);
        return menu;
    }

    private void onWindowClosing()
    {
        ResourceBundle b = LocaleManager.getInstance().getBundle();
        UIManager.put("OptionPane.yesButtonText", b.getString("exit.yes"));
        UIManager.put("OptionPane.noButtonText", b.getString("exit.no"));

        int result = JOptionPane.showConfirmDialog(
            this,
            b.getString("exit.message"),
            b.getString("exit.title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION)
        {
            WindowStateManager.saveFrame(this);
            WindowStateManager.saveInternalFrame(logWindow);
            WindowStateManager.saveInternalFrame(gameWindow);
            WindowStateManager.saveInternalFrame(robotInfoWindow);
            dispose();
            System.exit(0);
        }
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
}
