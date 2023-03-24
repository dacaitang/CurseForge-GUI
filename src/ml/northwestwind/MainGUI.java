package ml.northwestwind;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class MainGUI extends JFrame {
    public static String getLastLogLines() {
        for (Window window : getWindows()) {
            if (window instanceof MainGUI) {
                String[] arr = ((MainGUI)window).mTextArea.getText().split("\n");
                return "... (see log output)\n" + arr[arr.length - 2] + "\n" + arr[arr.length - 1];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new MainGUI().setVisible(true);
    }

    private JTextArea mTextArea;

    public MainGUI() {
        super("CurseForge-GUI");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton button = new JButton("Install modpack");
        button.addActionListener((e) -> {
            showInputDialog();
        });

        mTextArea = new JTextArea();
        mTextArea.setAutoscrolls(true);
        mTextArea.setEditable(false);
        mTextArea.setLineWrap(true);
        mTextArea.setWrapStyleWord(true);

        System.setOut(new CFGUIStream(System.out));
        System.setErr(new CFGUIStream(System.err));

        JScrollPane pane = new JScrollPane(mTextArea);
        pane.setAutoscrolls(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);
        panel.add(pane, BorderLayout.CENTER);
        setContentPane(panel);

        System.out.println("Derived from https://github.com/North-West-Wind/CurseForge-CLI");
        Main.main(new String[]{"--args", "config", "directory", System.getProperty("user.home") + "/curseforge-cli"});

        /*
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu();
        menu.setText("Options");



        menuBar.add(menu);
        setJMenuBar(menuBar);
        */
    }

    public void showInputDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Project ID");
        if (input != null && !input.isEmpty())
            new CFGUIWorker(input).execute();
    }

    class CFGUIStream extends PrintStream {
        public CFGUIStream(PrintStream parent) {
            super(parent);
        }

        @Override
        public void print(Object x) {
            super.print(x);
            mTextArea.append(x.toString());
        }

        @Override
        public void println(Object x) {
            print(x + "\n");
        }

        @Override
        public void print(String x) {
            super.print(x);
            mTextArea.append(x);
        }

        @Override
        public void println(String x) {
            print(x + "\n");
        }
    };

    class CFGUIWorker extends SwingWorker<Void, Void> {
        String input;

        CFGUIWorker(String input) {
            this.input = input;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Main.main(new String[]{"--args", "modpack", "install", input});
            return null;
        }

        @Override
        protected void done() {
            super.done();
            try {
                get();
                JOptionPane.showMessageDialog(MainGUI.this, "Task finisned!");
            } catch (Throwable th) {
                th.printStackTrace();
                JOptionPane.showMessageDialog(MainGUI.this, "An error occurred during installation. Check latestlog.txt for more details.");
            }
        }
    }
}
