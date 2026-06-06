package gui;

import java.util.prefs.Preferences;
import javax.swing.JInternalFrame;
import javax.swing.JFrame;


public class WindowStateManager
{
    private static final Preferences prefs =
        Preferences.userNodeForPackage(WindowStateManager.class);

    private static final String X        = ".x";
    private static final String Y        = ".y";
    private static final String W        = ".width";
    private static final String H        = ".height";
    private static final String ICONIFIED = ".iconified";

    public static void saveFrame(JFrame frame)
    {
        String key = frame.getTitle();
        prefs.putInt(key + X, frame.getX());
        prefs.putInt(key + Y, frame.getY());
        prefs.putInt(key + W, frame.getWidth());
        prefs.putInt(key + H, frame.getHeight());
        prefs.putBoolean(key + ICONIFIED,
            (frame.getExtendedState() & JFrame.ICONIFIED) != 0);
    }

    public static void restoreFrame(JFrame frame)
    {
        String key = frame.getTitle();
        if (prefs.getInt(key + W, -1) == -1)
            return;

        frame.setLocation(
            prefs.getInt(key + X, frame.getX()),
            prefs.getInt(key + Y, frame.getY()));
        frame.setSize(
            prefs.getInt(key + W, frame.getWidth()),
            prefs.getInt(key + H, frame.getHeight()));

        if (prefs.getBoolean(key + ICONIFIED, false))
            frame.setExtendedState(JFrame.ICONIFIED);
        else
            frame.setExtendedState(JFrame.NORMAL);
    }

    public static void saveInternalFrame(JInternalFrame frame)
    {
        String key = frame.getTitle();
        prefs.putInt(key + X, frame.getX());
        prefs.putInt(key + Y, frame.getY());
        prefs.putInt(key + W, frame.getWidth());
        prefs.putInt(key + H, frame.getHeight());
        prefs.putBoolean(key + ICONIFIED, frame.isIcon());
    }

    public static void restoreInternalFrame(JInternalFrame frame)
    {
        String key = frame.getTitle();
        if (prefs.getInt(key + W, -1) == -1)
            return;

        frame.setLocation(
            prefs.getInt(key + X, frame.getX()),
            prefs.getInt(key + Y, frame.getY()));
        frame.setSize(
            prefs.getInt(key + W, frame.getWidth()),
            prefs.getInt(key + H, frame.getHeight()));

        try {
            frame.setIcon(prefs.getBoolean(key + ICONIFIED, false));
        } catch (Exception e) {
        }
    }
}