package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer;
    private final RobotModel m_model;

    public GameVisualizer(RobotModel model)
    {
        m_model = model;
        m_timer = new Timer("events generator", true);

        m_timer.schedule(new TimerTask() {
            @Override public void run() {
                EventQueue.invokeLater(GameVisualizer.this::repaint);
            }
        }, 0, 50);

        m_timer.schedule(new TimerTask() {
            @Override public void run() {
                m_model.update(getWidth(), getHeight());
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_model.setTargetPosition(e.getPoint());
                repaint();
            }
        });

        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d,
            (int) Math.round(m_model.getRobotPositionX()),
            (int) Math.round(m_model.getRobotPositionY()),
            m_model.getRobotDirection());
        drawTarget(g2d,
            m_model.getTargetPositionX(),
            m_model.getTargetPositionY());
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y)
    {
        g.setTransform(new AffineTransform());
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static void fillOval(Graphics g, int cx, int cy, int w, int h)
    {
        g.fillOval(cx - w / 2, cy - h / 2, w, h);
    }

    private static void drawOval(Graphics g, int cx, int cy, int w, int h)
    {
        g.drawOval(cx - w / 2, cy - h / 2, w, h);
    }
}