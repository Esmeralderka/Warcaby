package gra;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;

import javax.swing.JComponent;

public class Klient
{
    private JPanel Panel = new JPanel();
    static JButton przycisk_startu = new JButton("Rozpocznij"); //przycisk do rozpoczecia gry
    static JButton przycisk_konca = new JButton("Zakończ"); // przycisk zeby sie poddac lub wyjsc z gry
    static JLabel infoLabel = new JLabel();// label zeby wyswietlic wiadomosci
    static  JFrame frame = new JFrame("Warcaby");

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame gra = new JFrame("Warcaby");
                gra.setPreferredSize(new Dimension(550, 550));
                gra.setContentPane(new Klient().Panel);
                gra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gra.pack();
                gra.setResizable(false);
                gra.setLocationRelativeTo(null);
                gra.setVisible(true);
            }
        });
    }

//    static public String getAdres_Serwera()
//    {
//        return JOptionPane.showInputDialog(
//                frame,
//                "Proszę wpisać adres serwera",
//                "Witamy w warcabach!",
//                JOptionPane.QUESTION_MESSAGE);
//    }

    public Klient()
    {
        Panel.setLayout(new GridBagLayout());
        Gra_klient gra = new Gra_klient();

        Plansza_klient plansza = new Plansza_klient();

        Font font = new Font("DialogInput", Font.PLAIN, 28);
        infoLabel.setFont(font);
        Box przyciski = Box.createHorizontalBox();
        przyciski.add(przycisk_startu);
        przyciski.add(Box.createHorizontalStrut(50));// okreslony odstep
        przyciski.add(przycisk_konca);

        Komponent(Panel, infoLabel, 0, 0, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE);
        Komponent(Panel, plansza, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        Komponent(Panel, przyciski, 0, 0, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.CENTER);

    }

    private void Komponent(JPanel panel, JComponent komponent, int x, int y, int szerokosc, int wysokosc, int miejsce, int wypelnienie)
    {

        GridBagConstraints umiejscowienie = new GridBagConstraints();

        umiejscowienie.gridx = x;
        umiejscowienie.gridy = y;
        umiejscowienie.gridwidth = szerokosc;
        umiejscowienie.gridheight = wysokosc;
        umiejscowienie.weightx = 50;
        umiejscowienie.weighty = 50;
        umiejscowienie.insets = new Insets(0, 20, 25, 20);
        umiejscowienie.anchor = miejsce;
        umiejscowienie.fill = wypelnienie;

        panel.add(komponent, umiejscowienie);

    }
}
