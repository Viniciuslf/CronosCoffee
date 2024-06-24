import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CronosCoffee extends JFrame {
    private DefaultListModel<String> activityListModel;
    private JList<String> activityList;
    private JLabel timerLabel;
    private Map<String, Long> activityTimes;
    private long startTime;
    private Timer timer;
    private String currentActivity;

    public CronosCoffee() {
        setTitle("CronosCoffee");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        activityListModel = new DefaultListModel<>();
        activityList = new JList<>(activityListModel);
        JScrollPane scrollPane = new JScrollPane(activityList);

        timerLabel = new JLabel("00:00:00");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 24));

        JButton addButton = new JButton("Add Activity");
        addButton.addActionListener(new AddActivityListener());

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonListener());

        JButton viewSummaryButton = new JButton("View Summary");
        viewSummaryButton.addActionListener(new ViewSummaryListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(viewSummaryButton);

        add(scrollPane, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        activityTimes = new HashMap<>();

        setVisible(true);
    }

    private class AddActivityListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String activity = JOptionPane.showInputDialog("Enter activity name:");
            if (activity != null && !activity.trim().isEmpty()) {
                activityListModel.addElement(activity);
                activityTimes.put(activity, 0L);
            }
        }
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentActivity = activityList.getSelectedValue();
            if (currentActivity != null) {
                startTime = System.currentTimeMillis();
                timer = new Timer(1000, new TimerListener());
                timer.start();
            } else {
                JOptionPane.showMessageDialog(null, "Please select an activity to start.");
            }
        }
    }

    private class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
                long elapsedTime = System.currentTimeMillis() - startTime;
                long totalTime = activityTimes.get(currentActivity) + elapsedTime;
                activityTimes.put(currentActivity, totalTime);
                updateTimerLabel(totalTime);
            }
        }
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            updateTimerLabel(elapsedTime);
        }
    }

    private class ViewSummaryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder summary = new StringBuilder();
            for (Map.Entry<String, Long> entry : activityTimes.entrySet()) {
                summary.append(entry.getKey()).append(": ").append(formatTime(entry.getValue())).append("\n");
            }
            JOptionPane.showMessageDialog(null, summary.toString(), "Activity Summary", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateTimerLabel(long time) {
        timerLabel.setText(formatTime(time));
    }

    private String formatTime(long time) {
        int hours = (int) (time / 3600000);
        int minutes = (int) ((time % 3600000) / 60000);
        int seconds = (int) ((time % 60000) / 1000);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CronosCoffee::new);
    }
}
