package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;


public class GameWindow extends JInternalFrame
{   
    private final RobotModel m_model;
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_model = new RobotModel();
        m_visualizer = new GameVisualizer(m_model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public RobotModel getModel()
    {
        return m_model;
    }
}
