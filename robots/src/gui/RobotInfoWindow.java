package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class RobotInfoWindow extends JInternalFrame
    implements PropertyChangeListener
{
    private final JLabel labelX;
    private final JLabel labelY;
    private final JLabel labelDirection;

    public RobotInfoWindow(RobotModel model)
    {
        super("Координаты робота", true, true, true, true);

        labelX = new JLabel("—");
        labelY = new JLabel("—");
        labelDirection = new JLabel("—");

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("X:"));
        panel.add(labelX);
        panel.add(new JLabel("Y:"));
        panel.add(labelY);
        panel.add(new JLabel("Направление (рад):"));
        panel.add(labelDirection);

        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(250, 130);

        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (!"position".equals(evt.getPropertyName()))
            return;

        RobotModel model = (RobotModel) evt.getNewValue();
        SwingUtilities.invokeLater(() -> {
            labelX.setText(String.format("%.2f", model.getRobotPositionX()));
            labelY.setText(String.format("%.2f", model.getRobotPositionY()));
            labelDirection.setText(String.format("%.4f", model.getRobotDirection()));
        });
    }
}