package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

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

    private final JLabel titleX;
    private final JLabel titleY;
    private final JLabel titleDirection;

    public RobotInfoWindow(RobotModel model)
    {
        super(LocaleManager.getInstance().getString("window.robot.info"),
              true, true, true, true);

        LocaleManager lm = LocaleManager.getInstance();

        titleX = new JLabel(lm.getString("robot.x") + ":");
        titleY = new JLabel(lm.getString("robot.y") + ":");
        titleDirection = new JLabel(lm.getString("robot.direction") + ":");

        labelX = new JLabel("—");
        labelY = new JLabel("—");
        labelDirection = new JLabel("—");

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(titleX); panel.add(labelX);
        panel.add(titleY); panel.add(labelY);
        panel.add(titleDirection); panel.add(labelDirection);

        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(260, 130);

        model.addPropertyChangeListener(this);

        LocaleManager.getInstance().addListener(this::onLocaleChanged);
    }

    private void onLocaleChanged(ResourceBundle b)
    {
        setTitle(b.getString("window.robot.info"));
        titleX.setText(b.getString("robot.x") + ":");
        titleY.setText(b.getString("robot.y") + ":");
        titleDirection.setText(b.getString("robot.direction") + ":");
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