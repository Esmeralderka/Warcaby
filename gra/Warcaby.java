package gra;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import static gra.Gra_serwer.PUSTE;

class Plansza_klient extends JComponent implements ActionListener, MouseListener
{
    private int szerokosc = 400;
    private int wysokosc = 400;

    public Plansza_klient()
    {
        addMouseListener(this);
        Klient.przycisk_startu.addActionListener(this);
        Klient.przycisk_konca.addActionListener(this);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(szerokosc, wysokosc);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        setBorder(new LineBorder(Color.black));

        for (int wiersz = 0; wiersz < 8; wiersz++)
        {
            for (int kolumna = 0; kolumna < 8; kolumna++)
            {
                if (wiersz % 2 == 0 && kolumna % 2 == 0 || wiersz % 2 != 0 && kolumna % 2 != 0)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect(kolumna * 50, wiersz * 50, 50, 50);

                }
                else
                {
                    g.setColor(Color.BLACK);
                    g.fillRect(kolumna * 50, wiersz * 50, 50, 50);
                }

                switch (Gra_klient.getMiejsce_na_planszy(wiersz, kolumna))
                {
                    case Gra_klient.BIALE:
                        g.setColor(Color.YELLOW);
                        g.fillOval((kolumna * 50) + 5, (wiersz * 50) + 5, 40, 40);
                        break;

                    case Gra_klient.CZARNE:
                        g.setColor(Color.CYAN);
                        g.fillOval((kolumna * 50) + 5, (wiersz * 50) + 5, 40, 40);
                        break;

                    case Gra_klient.BIALA_DAMKA:
                        g.setColor(Color.YELLOW);
                        g.fillOval((kolumna * 50) + 5, (wiersz * 50) + 5, 40, 40);
                        g.setColor(Color.BLACK);
                        g.drawString("Q", (kolumna * 50) + 20, (wiersz * 50) + 30);// draw
                        break;

                    case Gra_klient.CZARNA_DAMKA:
                        g.setColor(Color.CYAN);
                        g.fillOval((kolumna * 50) + 5, (wiersz * 50) + 5, 40, 40);
                        g.setColor(Color.BLACK);
                        g.drawString("Q", (kolumna * 50) + 20, (wiersz * 50) + 30);// draw
                        break;
                }
            }
        }

        if (Gra_klient.gra_trwa && Gra_klient.getAktualny_kolor() == Gra_klient.getAktualny_gracz())
        {
            Klient.przycisk_konca.setEnabled(true);
            Klient.infoLabel.setText("Twoja kolej!");

            for (int i = 0; i < Gra_klient.mozliwe_ruchy.length; i++)
            {
                g.drawRect(Gra_klient.mozliwe_ruchy[i].getKolumna() * 50, Gra_klient.mozliwe_ruchy[i].getWiersz() * 50, 49, 49);
            }

            if (Gra_klient.aktualny_wiersz >= 0)
            {
                g.setColor(Color.green);
                g.drawRect(Gra_klient.wybrana_kolumna * 50, Gra_klient.aktualny_wiersz * 50, 49, 49);
                g.setColor(Color.pink);
                for (int i = 0; i < Gra_klient.mozliwe_ruchy.length; i++)
                {
                    if (Gra_klient.mozliwe_ruchy[i].getKolumna() == Gra_klient.wybrana_kolumna && Gra_klient.mozliwe_ruchy[i].getWiersz() == Gra_klient.aktualny_wiersz)
                    {
                        g.fillRect(Gra_klient.mozliwe_ruchy[i].getRuch_doWykonania_kolumna() * 50, Gra_klient.mozliwe_ruchy[i].getRuch_doWykonania_wiersz() * 50, 49, 49);
                    }
                }
            }
        }
        else if (Gra_klient.gra_trwa && Gra_klient.getAktualny_kolor() != Gra_klient.getAktualny_gracz())
        {
            Klient.infoLabel.setText("Poczekaj na ruch przeciwnika");
            Klient.przycisk_konca.setEnabled(true);
        }
        else if (!Gra_klient.gra_trwa && Gra_klient.isOczekiwanie_na_gracza())
        {
            Klient.infoLabel.setText("Oczekiwanie na przeciwnika...");
            Klient.przycisk_konca.setEnabled(false);
        }
        else if (!Gra_klient.gra_trwa && Lacznosc_klient.polaczono_z_serwerem == false)
        {
            Klient.infoLabel.setText("Brak połączenia");
            Klient.przycisk_konca.setEnabled(false);
        }
        else if (!Gra_klient.gra_trwa && Gra_klient.getZwyciesca() == Gra_klient.getAktualny_kolor())
        {
            Klient.infoLabel.setText("Wygrana");
        }
        else if (!Gra_klient.gra_trwa && Gra_klient.getZwyciesca() != Gra_klient.getAktualny_kolor() && Gra_klient.getZwyciesca() != -1)
        {
            Klient.infoLabel.setText("Przegrana");
        }
        else if (!Gra_klient.gra_trwa && Gra_klient.getZwyciesca() != Gra_klient.getAktualny_kolor())
        {
            Klient.infoLabel.setText("");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (Gra_klient.gra_trwa == false)
        {
            Klient.infoLabel.setText("Aby rozpocząć nową gre naciśnij 'Start'");
        }
        else
        {
            int kolumna = (e.getX() / 50);
            int wiersz = (e.getY() / 50);
            if (kolumna >= 0 && kolumna < 8 && wiersz >= 0 && wiersz < 8) {
                Lacznosc_klient.info_na_serwer(wiersz, kolumna, Gra_klient.jest_Dezertercja());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent e)//przyciski
    {
        if (e.getSource() == Klient.przycisk_startu)
        {
            Gra_klient.setOczekiwanie_na_gracza(true);
            Gra_klient.start();
        }

        else if (e.getSource() == Klient.przycisk_konca)
        {
            Gra_klient.setDezertercja(true);
            Lacznosc_klient.info_na_serwer(-1, -1, Gra_klient.jest_Dezertercja());
        }
    }
}


class Gra_klient
{
    static boolean gra_trwa = false;
    static final int NIEWYBRANO =- 1 ,PUSTE = 0, BIALE = 1, BIALA_DAMKA = 2, CZARNE = 3, CZARNA_DAMKA = 4;
    static private int[][] tablica = new int[8][8];//tablica po stronie klienta

    static Pionek_klient[] mozliwe_ruchy;

    static int aktualny_gracz;//czy BIALE czy CZARNE
    static int aktualny_wiersz = NIEWYBRANO;// w innych klasach dla uproszczenia czasami zamiast enumeratora "niewybrano" jest po prostu -1
    static int wybrana_kolumna = NIEWYBRANO;
    static int aktualny_kolor;
    static int zwyciesca = NIEWYBRANO;
    static boolean dezertercja = false;

    static boolean oczekiwanie_na_gracza = false;
    static Lacznosc_klient laczenie;

    public static boolean isOczekiwanie_na_gracza()
    {
        return oczekiwanie_na_gracza;
    }

    public static void setOczekiwanie_na_gracza(boolean oczekiwanie_na_gracza)
    {
        Gra_klient.oczekiwanie_na_gracza = oczekiwanie_na_gracza; //jest static wiec nie moze byc this
    }

    public static boolean jest_Dezertercja()
    {
        return dezertercja;
    }

    public static void setDezertercja(boolean dezertercja)
    {
        Gra_klient.dezertercja = dezertercja;
    }

    public static int getZwyciesca()
    {
        return zwyciesca;
    }

    public static void setZwyciesca(int zwyciesca)
    {
        Gra_klient.zwyciesca = zwyciesca;
    }

    public static int getAktualny_kolor()
    {
        return aktualny_kolor;
    }

    public static void setAktualny_kolor(int aktualny_kolor)
    {
        Gra_klient.aktualny_kolor = aktualny_kolor;
    }

    public static boolean isGra_trwa()
    {
        return gra_trwa;
    }

    public static void setGra_trwa(boolean gra_trwa)
    {
        Gra_klient.gra_trwa = gra_trwa;
    }

    public static int getAktualny_wiersz()
    {
        return aktualny_wiersz;
    }

    public static void setAktualny_wiersz(int aktualny_wiersz)
    {
        Gra_klient.aktualny_wiersz = aktualny_wiersz;
    }

    public static int getWybrana_kolumna()
    {
        return wybrana_kolumna;
    }

    public static void setWybrana_kolumna(int wybrana_kolumna)
    {
        Gra_klient.wybrana_kolumna = wybrana_kolumna;
    }

    public static int getAktualny_gracz()
    {
        return aktualny_gracz;
    }

    public static void setAktualny_gracz(int aktualny_gracz)
    {
        Gra_klient.aktualny_gracz = aktualny_gracz;
    }

    public static void setTablica(int[][] tablica)
    {
        Gra_klient.tablica = tablica;
    }

    public static Pionek_klient[] getMozliwe_ruchy()
    {
        return mozliwe_ruchy;
    }

    public static void setMozliwe_ruchy(Pionek_klient[] mozliwe_ruchy)
    {
        Gra_klient.mozliwe_ruchy = mozliwe_ruchy;
    }

    public Gra_klient()
    {
        inicjalizacja_planszy();
    }

    public static void inicjalizacja_planszy()
    {
        for (int wiersz = 0; wiersz < 8; wiersz++)
        {
            for (int kolumna = 0; kolumna < 8; kolumna++)
            {
                if (wiersz % 2 != kolumna % 2)
                {
                    if (wiersz < 3)
                    {
                        tablica[wiersz][kolumna] = CZARNE;
                    }

                    else if (wiersz > 4)
                    {
                        tablica[wiersz][kolumna] = BIALE;
                    }

                    else
                    {
                        tablica[wiersz][kolumna] = PUSTE;
                    }
                }
                else
                {
                    tablica[wiersz][kolumna] = PUSTE;
                }
            }
        }
    }

    public static int getMiejsce_na_planszy(int wiersz, int kolumna)
    {
        return tablica[wiersz][kolumna];
    }


    static void start()//po kliknieciu przycisku statru
    {
        Klient.przycisk_startu.setEnabled(false);
        Klient.przycisk_konca.setEnabled(true);

        laczenie = new Lacznosc_klient();
        laczenie.start();
    }
}


class Lacznosc_klient extends Thread  implements Serializable
{
    static String adres_serwera ="localhost";// Klient.getAdres_Serwera();// "localhost";

    private Socket socket;
    private ObjectInputStream in;
    private static ObjectOutputStream out;

    private Object obiekt;

    public static Pionek_klient info_do_serwera;
    private Info_serwer info_z_serwera;

    static boolean polaczono_z_serwerem = true;
    private volatile boolean watek_start = true;

    public Lacznosc_klient()
    {
        info_do_serwera = new Pionek_klient();
    }

    @Override
    public void run()//sprawdza polaczenie
    {
        while (watek_start)
        {
            try
            {
                polaczono_z_serwerem = true;
                socket = new Socket(adres_serwera, 9001);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

            }
            catch (IOException e)
            {
                Gra_klient.setOczekiwanie_na_gracza(false);
                polaczono_z_serwerem = false;
                Klient.przycisk_startu.setEnabled(true);
                Klient.przycisk_konca.setEnabled(false);
            }

            while (polaczono_z_serwerem)
            {
                try
                {
                    obiekt = in.readObject();
                    info_z_serwera = (Info_serwer) obiekt;

                    Gra_klient.setOczekiwanie_na_gracza(false);
                    Gra_klient.setDezertercja(false);

                    odbierz_od_serwera
                            (
                                    info_z_serwera.getTablica(), info_z_serwera.getWybrany_wiersz(),
                                    info_z_serwera.getAktualna_kolumna(), info_z_serwera.isTrwa(),
                                    info_z_serwera.getAktualny_gracz(), info_z_serwera.getRuchy(),
                                    info_z_serwera.getKolor(), info_z_serwera.Wygrany()
                            );

                    if (Gra_klient.jest_Dezertercja() == true)
                    {
                        info_na_serwer(-1, -1, Gra_klient.jest_Dezertercja());
                        Klient.przycisk_startu.setEnabled(true);
                        Klient.przycisk_konca.setEnabled(false);
                        break;

                    }
                    else if (info_z_serwera.Wygrany() != Gra_klient.PUSTE)
                    {
                        if (info_z_serwera.Wygrany() == Gra_klient.getAktualny_kolor())
                        {
                            Klient.przycisk_startu.setEnabled(true);
                            Klient.przycisk_konca.setEnabled(false);

                            break;
                        }
                        else
                        {
                            Klient.przycisk_startu.setEnabled(true);
                            Klient.przycisk_konca.setEnabled(false);

                            break;
                        }
                    }

                }
                catch (ClassNotFoundException e)
                {
                }
                catch (IOException e)
                {
                }
            }
            watek_start = false;
            try
            {
                out.close();
                in.close();
                socket.close();
            }
            catch (IOException e)
            {
            }
        }
    }

    private void odbierz_od_serwera(int[][] tablica, int aktualny_wiersz, int aktualna_kolumna, boolean start, int aktualny_gracz, Pionek_klient[] mozliwe_ruchy, int kolor, int zwyciesca)
    {
        Gra_klient.setTablica(tablica);
        Gra_klient.setAktualny_wiersz(aktualny_wiersz);
        Gra_klient.setWybrana_kolumna(aktualna_kolumna);
        Gra_klient.setGra_trwa(start);
        Gra_klient.setAktualny_gracz(aktualny_gracz);
        Gra_klient.setMozliwe_ruchy(mozliwe_ruchy);
        Gra_klient.setAktualny_kolor(kolor);
        Gra_klient.setZwyciesca(zwyciesca);

    }

    private static void wiadomosc_dla_serwera(int wiersz, int kolumna, boolean dezercja)
    {
        info_do_serwera.setWybrana_kolumna(kolumna);
        info_do_serwera.setWybrany_wiersz(wiersz);
        info_do_serwera.setDezercja(dezercja);

    }

    public static void info_na_serwer(int wiersz, int kolumna, boolean dezercja)
    {
        wiadomosc_dla_serwera(wiersz, kolumna, dezercja);
        try
        {
            out.reset();
            out.writeObject(info_do_serwera);
        }
        catch (IOException e)
        {
        }
    }
}


class Pionek_klient implements Serializable
{

    private int ruch_wykonany_wiersz, ruch_wykonany_kolumna; // miejsce w ktorym znajduje sie zaznaczony pionek
    private int ruch_doWykonania_wiersz, ruch_doWykonania_kolumna; // miejsca do ktorych moze byc przestawiony pionek

    private boolean ruch_krolowej = false;
    private boolean bicie_krolowej = false;

    private int wiersz;
    private int kolumna;
    private boolean dezerter; ///poddac sie? moze lepiej

    public boolean jest_Bicie_krolowej()
    {
        return bicie_krolowej;
    }

    public void setBicie_krolowej(boolean bicie_krolowej)
    {
        this.bicie_krolowej = bicie_krolowej;
    }

    public boolean isRuch_krolowej()
    {
        return ruch_krolowej;
    }

    public void setRuch_krolowej(boolean ruch_krolowej)
    {
        this.ruch_krolowej = ruch_krolowej;
    }

    public boolean isBicie()
    {
        return (ruch_wykonany_kolumna - ruch_doWykonania_kolumna == 2 || ruch_wykonany_kolumna - ruch_doWykonania_kolumna == -2);
    }

    public int getWiersz()
    {
        return ruch_wykonany_wiersz;
    }

    public void setRuch_wykonany_wiersz(int ruch_wykonany_wiersz)
    {
        this.ruch_wykonany_wiersz = ruch_wykonany_wiersz;
    }

    public int getKolumna()
    {
        return ruch_wykonany_kolumna;
    }

    public void setRuch_wykonany_kolumna(int ruch_wykonany_kolumna)
    {
        this.ruch_wykonany_kolumna = ruch_wykonany_kolumna;
    }

    public int getRuch_doWykonania_wiersz()
    {
        return ruch_doWykonania_wiersz;
    }

    public void setRuch_doWykonania_wiersz(int ruch_doWykonania_wiersz)
    {
        this.ruch_doWykonania_wiersz = ruch_doWykonania_wiersz;
    }

    public int getRuch_doWykonania_kolumna()
    {
        return ruch_doWykonania_kolumna;
    }

        public void setRuch_doWykonania_kolumna(int ruch_doWykonania_kolumna)
    {
        this.ruch_doWykonania_kolumna = ruch_doWykonania_kolumna;
    }

    public Pionek_klient()
    {
    }

    public Pionek_klient(int wykonany_wiersz, int wykonana_kolumna, int dowykonania_wiersz, int dowykonania_kolumna)
    {
        this.ruch_wykonany_wiersz = wykonany_wiersz;
        this.ruch_wykonany_kolumna = wykonana_kolumna;
        this.ruch_doWykonania_wiersz = dowykonania_wiersz;
        this.ruch_doWykonania_kolumna = dowykonania_kolumna;
    }

    public boolean getDezercja()
    {
        return dezerter;
    }

    public void setDezercja(boolean zdezerterowac)
    {
        this.dezerter = zdezerterowac;
    }

    public int getWybrany_wiersz()
    {
        return wiersz;
    }

    public void setWybrany_wiersz(int wiersz)
    {
        this.wiersz = wiersz;
    }

    public int getWybrana_kolumna()
    {
        return kolumna;
    }

    public void setWybrana_kolumna(int kolumna)
    {
        this.kolumna = kolumna;
    }
}
/////////////S////////////E///////////////R/////////////////W////////////////E////////////////R//////////////////////////////////////


class Info_serwer implements Serializable
{
    private int[][] tablica = new int[8][8];
    private boolean trwa;// watek czy trwa
    private int aktualny_gracz;
    private Pionek_klient[] ruchy;
    private int aktualny_wiersz;
    private int aktualna_kolumna;
    private int zwyciesca;
    private int kolor;

    public int getKolor()
    {
        return kolor;
    }

    public void setKolor(int kolor)
    {
        this.kolor = kolor;
    }

    public int getWybrany_wiersz()
    {
        return aktualny_wiersz;
    }

    public void setWybrany_wiersz(int wiersz)
    {
        this.aktualny_wiersz = wiersz;
    }

    public int getAktualna_kolumna()
    {
        return aktualna_kolumna;
    }

    public void setWybrana_kolumna(int kolumna)
    {
        this.aktualna_kolumna = kolumna;
    }

    public int[][] getTablica()
    {
        return tablica;
    }

    public int Wygrany()
    {
        return zwyciesca;
    }

    public void setZwyciesca(int zwyciesca)
    {
        this.zwyciesca = zwyciesca;
    }

    public void setTablica(int[][] tablica)
    {
        this.tablica = tablica;
    }

    public boolean isTrwa()
    {
        return trwa;
    }

    public void setTrwa(boolean gra_trwa)
    {
        this.trwa = gra_trwa;
    }

    public int getAktualny_gracz()
    {
        return aktualny_gracz;
    }

    public void setAktualny_gracz(int aktualny_gracz)
    {
        this.aktualny_gracz = aktualny_gracz;
    }

    public Pionek_klient[] getRuchy()
    {
        return ruchy;
    }

    public void setMozliwe_ruchy(Pionek_klient[] ruchy)
    {
        this.ruchy = ruchy;
    }
}

class Gra_serwer
{
    static final int  NIEWYBRANO=-1, PUSTE = 0, BIALE = 1, BIALA_DAMKA = 2, CZARNE = 3, CZARNA_DAMKA = 4;
    private int[][] tablica = new int[8][8];
    private boolean gra_trwa = false;
    private int zwyciesca = PUSTE;

    private int Aktualny_gracz;

    private int wiersz = NIEWYBRANO;
    private int kolumna = NIEWYBRANO;

    Pionek_klient[] ruchy;//mozliwe do wykonania ruchy

    public int[][] getTablica()
    {
        return tablica;
    }

    public void Gra()
    {
        inicjalizacja_planszy();
    }

    public void inicjalizacja_planszy()
    {
        for (int wiersz = 0; wiersz < 8; wiersz++)
        {
            for (int kolumna = 0; kolumna < 8; kolumna++)
            {
                if (wiersz % 2 != kolumna % 2)
                {
                    if (wiersz < 3)
                    {
                        tablica[wiersz][kolumna] = CZARNE;
                    }
                    else if (wiersz > 4)
                    {
                        tablica[wiersz][kolumna] = BIALE;
                    }
                    else
                    {
                        tablica[wiersz][kolumna] = PUSTE;
                    }
                }
                else
                {
                    tablica[wiersz][kolumna] = PUSTE;
                }
            }
        }
    }

    public void wykonaj_ruch(Pionek_klient ruch)
    {
        //jezeli jest dama
        if (tablica[ruch.getWiersz()][ruch.getKolumna()] == CZARNA_DAMKA || tablica[ruch.getWiersz()][ruch.getKolumna()] == BIALA_DAMKA)
        {
            zbicie_przez_dame(ruch);
            przesun_pionek(ruch);
        }

        //jezeli normalny pionek
        else if (ruch.isBicie())
        {
            usun_pionek(ruch);
            przesun_pionek(ruch);
        }
        else
        {
            przesun_pionek(ruch);
        }

        Sprawdz_czy_dama(ruch);
    }

    private void zbicie_przez_dame(Pionek_klient ruch)
    {
        int przeciwnik_wiersz = 0, przeciwnik_kolumna = 0; // Pozycja możliwych ruchów przeciwnika
        int sprawdzany_wiersz = ruch.getWiersz();
        int sprawdzana_kolumna = ruch.getKolumna();

        //sprawdzanie dla 4 kierunkow przesuniecia damki na wybrane miejsce
        if (ruch.getWiersz() < ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() < ruch.getRuch_doWykonania_kolumna())
        {
            while (sprawdzana_kolumna < ruch.getRuch_doWykonania_kolumna() && sprawdzany_wiersz < ruch.getRuch_doWykonania_wiersz())
            {
                sprawdzana_kolumna++;
                sprawdzany_wiersz++;

                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    ruch.setBicie_krolowej(true);
                    ruch.setRuch_krolowej(false);
                    break;
                }
            }
        }
        else if (ruch.getWiersz() < ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() > ruch.getRuch_doWykonania_kolumna())
        {
            while (sprawdzana_kolumna > ruch.getRuch_doWykonania_kolumna() && sprawdzany_wiersz < ruch.getRuch_doWykonania_wiersz())
            {
                sprawdzana_kolumna--;
                sprawdzany_wiersz++;

                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    ruch.setBicie_krolowej(true);
                    ruch.setRuch_krolowej(false);
                    break;
                }
            }
        }
        else if (ruch.getWiersz() > ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() < ruch.getRuch_doWykonania_kolumna())
        {
            while (sprawdzana_kolumna < ruch.getRuch_doWykonania_kolumna() && sprawdzany_wiersz > ruch.getRuch_doWykonania_wiersz())
            {
                sprawdzana_kolumna++;
                sprawdzany_wiersz--;

                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    ruch.setBicie_krolowej(true);
                    ruch.setRuch_krolowej(false);
                    break;
                }
            }
        }
        else if (ruch.getWiersz() > ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() > ruch.getRuch_doWykonania_kolumna())
        {
            while (sprawdzana_kolumna > ruch.getRuch_doWykonania_kolumna() && sprawdzany_wiersz > ruch.getRuch_doWykonania_wiersz())
            {
                sprawdzana_kolumna--;
                sprawdzany_wiersz--;

                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    ruch.setBicie_krolowej(true);
                    ruch.setRuch_krolowej(false);
                    break;
                }
            }
        }

        przeciwnik_kolumna = sprawdzana_kolumna;
        przeciwnik_wiersz = sprawdzany_wiersz;

        tablica[przeciwnik_wiersz][przeciwnik_kolumna] = PUSTE;//usuwanie zbitego pionka

        if (ruch.jest_Bicie_krolowej() == false)
        {
            ruch.setRuch_krolowej(true); //jesli nie ma bicia to sie rusza
        }
    }

    private void Sprawdz_czy_dama(Pionek_klient aktualne_miejsce) //sprawdza i tworzy nowa dame
    {
        if (aktualne_miejsce.getRuch_doWykonania_wiersz() == 0 && tablica[aktualne_miejsce.getRuch_doWykonania_wiersz()][aktualne_miejsce.getRuch_doWykonania_kolumna()] == BIALE)
        {
            aktualne_miejsce.setRuch_krolowej(true);
            tablica[aktualne_miejsce.getRuch_doWykonania_wiersz()][aktualne_miejsce.getRuch_doWykonania_kolumna()] = BIALA_DAMKA;

        }
        if (aktualne_miejsce.getRuch_doWykonania_wiersz() == 7 && tablica[aktualne_miejsce.getRuch_doWykonania_wiersz()][aktualne_miejsce.getRuch_doWykonania_kolumna()] == CZARNE)
        {
            aktualne_miejsce.setRuch_krolowej(true);
            tablica[aktualne_miejsce.getRuch_doWykonania_wiersz()][aktualne_miejsce.getRuch_doWykonania_kolumna()] = CZARNA_DAMKA;
        }
    }

    private void przesun_pionek(Pionek_klient ruch)
    {
        tablica[ruch.getRuch_doWykonania_wiersz()][ruch.getRuch_doWykonania_kolumna()] = tablica[ruch.getWiersz()][ruch.getKolumna()];
        tablica[ruch.getWiersz()][ruch.getKolumna()] = PUSTE;
    }

    private void usun_pionek(Pionek_klient ruch)
    {
        int przeciwnik_wiersz = 0;
        int przeciwnik_kolumna = 0;

        if (ruch.getWiersz() < ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() < ruch.getRuch_doWykonania_kolumna())
        {
            przeciwnik_wiersz = ruch.getRuch_doWykonania_wiersz() - 1;
            przeciwnik_kolumna = ruch.getRuch_doWykonania_kolumna() - 1;
        }
        else if (ruch.getWiersz() < ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() > ruch.getRuch_doWykonania_kolumna())
        {
            przeciwnik_wiersz = ruch.getRuch_doWykonania_wiersz() - 1;
            przeciwnik_kolumna = ruch.getRuch_doWykonania_kolumna() + 1;
        }
        else if (ruch.getWiersz() > ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() < ruch.getRuch_doWykonania_kolumna())
        {
            przeciwnik_wiersz = ruch.getRuch_doWykonania_wiersz() + 1;
            przeciwnik_kolumna = ruch.getRuch_doWykonania_kolumna() - 1;
        }
        else if (ruch.getWiersz() > ruch.getRuch_doWykonania_wiersz() && ruch.getKolumna() > ruch.getRuch_doWykonania_kolumna())
        {
            przeciwnik_wiersz = ruch.getRuch_doWykonania_wiersz() + 1;
            przeciwnik_kolumna = ruch.getRuch_doWykonania_kolumna() + 1;
        }

        tablica[przeciwnik_wiersz][przeciwnik_kolumna] = PUSTE;
    }

    public Pionek_klient[] Mozliwe_ruchy(int gracz)
    {
        int damka;

        if (gracz != BIALE && gracz != CZARNE)
        {
            return null;
        }

        if (gracz == BIALE)
        {
            damka = BIALA_DAMKA;
        }
        else
        {
            damka = CZARNA_DAMKA;
        }

        ArrayList<Pionek_klient> ruchy = new ArrayList<Pionek_klient>();

        Sprawdz_bicie(ruchy, gracz, damka);

        //jezeli nie ma bicia to sprawdzane sa mozliwe ruchy
        if (ruchy.size() == 0)
        {
            Sprawdz_ruchy(ruchy, gracz, damka);
        }

        // jak nie ma ruchów to jest null
        if (ruchy.size() == 0)
        {
            return null;
        }
        else
        {
            Pionek_klient[] mozliwe_ruchy = new Pionek_klient[ruchy.size()];
            for (int i = 0; i < ruchy.size(); i++)
            {
                mozliwe_ruchy[i] = ruchy.get(i);
            }
            return mozliwe_ruchy;
        }
    }

    private void Sprawdz_ruchy(ArrayList<Pionek_klient> ruch, int gracz, int damka)
    {
        for (int wiersz = 0; wiersz < 8; wiersz++)
        {
            for (int kolumna = 0; kolumna < 8; kolumna++)
            {
                if (tablica[wiersz][kolumna] == gracz)//ssprawdzanie dla 4 kierunkow
                {
                    if (isRuch(gracz, wiersz, kolumna, wiersz + 1, kolumna + 1))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz + 1, kolumna + 1));
                    }

                    if (isRuch(gracz, wiersz, kolumna, wiersz - 1, kolumna + 1))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz - 1, kolumna + 1));
                    }

                    if (isRuch(gracz, wiersz, kolumna, wiersz + 1, kolumna - 1))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz + 1, kolumna - 1));
                    }

                    if (isRuch(gracz, wiersz, kolumna, wiersz - 1, kolumna - 1))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz - 1, kolumna - 1));
                    }

                }
                else if (tablica[wiersz][kolumna] == damka) // ewentualne sprawdzenie czy to dama i jej ruchow
                {
                    isRuch_damki(ruch, gracz, wiersz, kolumna);
                }
            }
        }
    }


    private void isRuch_damki(ArrayList<Pionek_klient> ruch, int gracz, int wiersz, int kolumna)
    {
        int sprawdzany_wiersz = wiersz;//poczatkowa jest pozycja damy
        int sprawdzana_kolumna = kolumna;

        if (gracz == BIALE)
        {
            while (--sprawdzany_wiersz >= 0 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;

            while (--sprawdzany_wiersz >= 0 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }
            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;

            while (++sprawdzany_wiersz <= 7 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }
            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;

            while (++sprawdzany_wiersz <= 7 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }
        }
        else //czyli jezeli gracz gra czarnymi
        {
            while (--sprawdzany_wiersz >= 0 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;

            while (--sprawdzany_wiersz >= 0 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;

            while (++sprawdzany_wiersz <= 7 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;

            while (++sprawdzany_wiersz <= 7 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
                {
                    break;
                }
                ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
            }
        }
    }


    private boolean isRuch(int gracz, int wiersz, int kolumna, int sprawdzany_wiersz, int sprawdzana_kolumna)
    {
        // jezeli jest poza plansza
        if (sprawdzana_kolumna > 7 || sprawdzana_kolumna < 0 || sprawdzany_wiersz > 7 || sprawdzany_wiersz < 0)
        {
            return false;
        }

        // jezeli jest tam jakis pionek
        if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
        {
            return false;
        }

        if (gracz == BIALE)
        {
            if (sprawdzany_wiersz > wiersz)
            {
                return false; //zwykle biale moga isc tylko do gory
            }
            return true;
        }
        else
        {
            if (sprawdzany_wiersz < wiersz)
            {
                return false;//zwykle czarne moga w dol isc
            }
            return true;
        }
    }

    private void Sprawdz_bicie(ArrayList<Pionek_klient> ruch, int gracz, int damka)
    {
        for (int wiersz = 0; wiersz < 8; wiersz++)
        {
            for (int kolumna = 0; kolumna < 8; kolumna++)
            {
                if (tablica[wiersz][kolumna] == gracz)
                {
                    if (isBicie(gracz, wiersz, kolumna, wiersz + 1, kolumna + 1, wiersz + 2, kolumna + 2))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz + 2, kolumna + 2));
                    }

                    if (isBicie(gracz, wiersz, kolumna, wiersz - 1, kolumna + 1, wiersz - 2, kolumna + 2))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz - 2, kolumna + 2));
                    }

                    if (isBicie(gracz, wiersz, kolumna, wiersz + 1, kolumna - 1, wiersz + 2, kolumna - 2))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz + 2, kolumna - 2));
                    }

                    if (isBicie(gracz, wiersz, kolumna, wiersz - 1, kolumna - 1, wiersz - 2, kolumna - 2))
                    {
                        ruch.add(new Pionek_klient(wiersz, kolumna, wiersz - 2, kolumna - 2));
                    }
                }
                else if (tablica[wiersz][kolumna] == damka)
                {
                    isBicie_damki(ruch, gracz, wiersz, kolumna);
                }
            }
        }
    }

    private void isBicie_damki(ArrayList<Pionek_klient> ruch, int gracz, int wiersz, int kolumna)
    {
        int sprawdzany_wiersz = wiersz;
        int sprawdzana_kolumna = kolumna;

        boolean pionek_przeciwnika = false;

        if (gracz == BIALE)
        {
            while (--sprawdzany_wiersz >= 0 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA) && pionek_przeciwnika == true)
                {
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    pionek_przeciwnika = true;// znaleziono pionek przeciwnika
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna)); //wolne miejsce, czyli mozna wykonac ruch
                }
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;
            pionek_przeciwnika = false;

            while (--sprawdzany_wiersz >= 0 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA) && pionek_przeciwnika == true)
                {
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    pionek_przeciwnika = true;
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
                }
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;
            pionek_przeciwnika = false;

            while (++sprawdzany_wiersz <= 7 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA) && pionek_przeciwnika == true) {// second enemy checker -
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    pionek_przeciwnika = true;
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
                }
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;
            pionek_przeciwnika = false;

            while (++sprawdzany_wiersz <= 7 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA) && pionek_przeciwnika == true)
                {
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    pionek_przeciwnika = true;
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
                }
            }
        }
        else //dla czarnych pionkow
        {
            while (--sprawdzany_wiersz >= 0 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA) && pionek_przeciwnika == true)
                {
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    pionek_przeciwnika = true;
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
                }
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;
            pionek_przeciwnika = false;

            while (--sprawdzany_wiersz >= 0 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA) && pionek_przeciwnika == true)
                {
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    pionek_przeciwnika = true;
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
                }
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;
            pionek_przeciwnika = false;

            while (++sprawdzany_wiersz <= 7 && --sprawdzana_kolumna >= 0)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA) && pionek_przeciwnika == true)
                {
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    pionek_przeciwnika = true;
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
                }
            }

            sprawdzany_wiersz = wiersz;
            sprawdzana_kolumna = kolumna;
            pionek_przeciwnika = false;

            while (++sprawdzany_wiersz <= 7 && ++sprawdzana_kolumna <= 7)
            {
                if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == CZARNA_DAMKA)
                {
                    break;
                }
                else if ((tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA) && pionek_przeciwnika == true)
                {
                    pionek_przeciwnika = false;
                    break;
                }
                else if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALE || tablica[sprawdzany_wiersz][sprawdzana_kolumna] == BIALA_DAMKA)
                {
                    pionek_przeciwnika = true;
                }
                else if (pionek_przeciwnika == true && tablica[sprawdzany_wiersz][sprawdzana_kolumna] == PUSTE)
                {
                    ruch.add(new Pionek_klient(wiersz, kolumna, sprawdzany_wiersz, sprawdzana_kolumna));
                }
            }
        }
    }


    private boolean isBicie(int gracz, int wiersz, int kolumna, int przeskoczony_wiersz, int przekoczona_kolumna, int sprawdzany_wiersz, int sprawdzana_kolumna)
    {

        //spr czy jest poza plansza
        if (sprawdzana_kolumna > 7 || sprawdzana_kolumna < 0 || sprawdzany_wiersz > 7 || sprawdzany_wiersz < 0)
        {
            return false;
        }

        // jezeli jest juz tam jakis pionek
        if (tablica[sprawdzany_wiersz][sprawdzana_kolumna] != PUSTE)
        {
            return false;
        }

        if (gracz == BIALE)
        {
            if (sprawdzany_wiersz > wiersz && tablica[wiersz][kolumna] == BIALE)
            {
                return false;
            }

            if (tablica[przeskoczony_wiersz][przekoczona_kolumna] != CZARNE && tablica[przeskoczony_wiersz][przekoczona_kolumna] != CZARNA_DAMKA)
            {
                return false;
            }
            return true;
        }
        else
        {
            if (sprawdzany_wiersz < wiersz && tablica[wiersz][kolumna] == CZARNE)
            {
                return false;
            }
            if (tablica[przeskoczony_wiersz][przekoczona_kolumna] != BIALE && tablica[przeskoczony_wiersz][przekoczona_kolumna] != BIALA_DAMKA)
            {
                return false;
            }
            return true;
        }
    }

    public Pionek_klient[] isDrugie_bicie(int player, int wiersz, int kolumna)
    {
        int damka;

        if (player == BIALE)
        {
            damka = BIALA_DAMKA;
        }

        else
        {
            damka = CZARNA_DAMKA;
        }

        ArrayList<Pionek_klient> ruchy = new ArrayList<Pionek_klient>();

        Sprawdz_drugie_bicie(ruchy, player, damka, wiersz, kolumna);

        if (ruchy.size() == 0)
        {
            return null;
        }
        else
        {
            Pionek_klient[] drugi_ruch = new Pionek_klient[ruchy.size()];
            for (int i = 0; i < ruchy.size(); i++)
            {
                drugi_ruch[i] = ruchy.get(i);
            }
            return drugi_ruch;
        }
    }

    private void Sprawdz_drugie_bicie(ArrayList<Pionek_klient> ruch, int gracz, int damka, int wiersz, int kolumna)
    {
        if (tablica[wiersz][kolumna] == gracz)
        {
            if (isBicie(gracz, wiersz, kolumna, wiersz + 1, kolumna + 1, wiersz + 2, kolumna + 2))
            {
                ruch.add(new Pionek_klient(wiersz, kolumna, wiersz + 2, kolumna + 2));
            }

            if (isBicie(gracz, wiersz, kolumna, wiersz - 1, kolumna + 1, wiersz - 2, kolumna + 2))
            {
                ruch.add(new Pionek_klient(wiersz, kolumna, wiersz - 2, kolumna + 2));
            }

            if (isBicie(gracz, wiersz, kolumna, wiersz + 1, kolumna - 1, wiersz + 2, kolumna - 2))
            {
                ruch.add(new Pionek_klient(wiersz, kolumna, wiersz + 2, kolumna - 2));
            }

            if (isBicie(gracz, wiersz, kolumna, wiersz - 1, kolumna - 1, wiersz - 2, kolumna - 2))
            {
                ruch.add(new Pionek_klient(wiersz, kolumna, wiersz - 2, kolumna - 2));
            }

        }
        else if (tablica[wiersz][kolumna] == damka)
        {
            isBicie_damki(ruch, gracz, wiersz, kolumna);
        }
    }

    public Gra_serwer()
    {
        inicjalizacja_gry();
    }

    private void inicjalizacja_gry()
    {
        Gra();
        Aktualny_gracz = BIALE;//białe zaczynają (żółte), czarne wygrywają (niebieskie)
        ruchy = Mozliwe_ruchy(Aktualny_gracz);
        gra_trwa = true;
    }

    private void koniec_gry(int zwyciesca)
    {
        gra_trwa = false;
        this.zwyciesca = zwyciesca;
    }

    synchronized void Klikniecie(int wiersz, int kolumna, boolean dezercja)
    {
        if (dezercja == true)
        {
            if (Aktualny_gracz == BIALE)
            {
                koniec_gry(CZARNE);
            }
            else
            {
                koniec_gry(BIALE);
            }
        }
        else
        {
            //jezeli jeszcze nie jest wybrany pionek, to go wybiera
            for (int i = 0; i < ruchy.length; i++)
            {
                if (ruchy[i].getWiersz() == wiersz && ruchy[i].getKolumna() == kolumna)
                {
                    this.wiersz = wiersz;
                    this.kolumna = kolumna;
                    return;
                }
            }

            // przeciwnik jeszcze nie wybral
            if (this.wiersz < 0)
            {
                return;
            }

            for (int i = 0; i < ruchy.length; i++) //zmienia ruch
            {
                if (ruchy[i].getWiersz() == this.wiersz && ruchy[i].getKolumna() == this.kolumna && ruchy[i].getRuch_doWykonania_wiersz() == wiersz && ruchy[i].getRuch_doWykonania_kolumna() == kolumna)
                {
                    Ruch(ruchy[i]);
                    return;
                }
            }

        }
    }

    synchronized private void Ruch(Pionek_klient ruch)
    {
        wykonaj_ruch(ruch);

        //sprawdza czy jest jakies bicie mozliwe
        if ((ruch.isBicie() && !ruch.isRuch_krolowej()) || ruch.jest_Bicie_krolowej())
        {
            ruchy = isDrugie_bicie(Aktualny_gracz, ruch.getRuch_doWykonania_wiersz(), ruch.getRuch_doWykonania_kolumna());
            if (ruchy != null)
            {
                wiersz = ruch.getRuch_doWykonania_wiersz();
                kolumna = ruch.getRuch_doWykonania_kolumna();
                return;
            }
        }

        ruch.setRuch_krolowej(false);
        ruch.setBicie_krolowej(false);

        if (Aktualny_gracz == BIALE)
        {
            Aktualny_gracz = CZARNE;
            ruchy = Mozliwe_ruchy(Aktualny_gracz);
            if (ruchy == null)
            {
                koniec_gry(BIALE);
            }
        }
        else
        {
            Aktualny_gracz = BIALE;
            ruchy = Mozliwe_ruchy(Aktualny_gracz);
            if (ruchy == null)
            {
                koniec_gry(CZARNE);
            }
        }

        wiersz = NIEWYBRANO;
        kolumna = NIEWYBRANO;
    }
    public synchronized int getAktulny_wiersz()
    {
        return wiersz;
    }

    public synchronized int getAktualny_gracz()
    {
        return Aktualny_gracz;
    }

    public synchronized void setAktualny_gracz(int aktualny_gracz)
    {
        this.Aktualny_gracz = aktualny_gracz;
    }

    public synchronized void setWiersz(int wiersz)
    {
        this.wiersz = wiersz;
    }

    public synchronized int getAktualna_kolumna()
    {
        return kolumna;
    }

    public synchronized void setKolumna(int kolumna)
    {
        this.kolumna = kolumna;
    }

    public synchronized Pionek_klient[] getRuchy()
    {
        return ruchy;
    }

    public synchronized int getZwyciesca()
    {
        return zwyciesca;
    }

    public synchronized boolean isGra_trwa()
    {
        return gra_trwa;
    }

    public synchronized void setGra_trwa(boolean gra_trwa)
    {
        this.gra_trwa = gra_trwa;
    }

    public synchronized void setZwyciesca(int zwyciesca)
    {
        this.zwyciesca = zwyciesca;
    }

}


class Mecz_serwer
{
    int numer_meczu;
    Gra_serwer gra;

    public Mecz_serwer(int meczyk)
    {
        this.numer_meczu = meczyk;

        gra = new Gra_serwer();
        gra.setGra_trwa(true);
    }

    class Gracz extends Thread
    {
        private int Kolor;
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        private Pionek_klient klient_do_serwera = new Pionek_klient();
        private Info_serwer serwer_do_klienta = new Info_serwer();
        private boolean watek_trwa = true;
        public boolean dezercja = false;

        public Gracz(Socket socket, int kolor)
        {
            this.socket = socket;
            this.Kolor = kolor;
        }

        public void run()
        {
            while (watek_trwa)
            {
                try
                {
                    in = new ObjectInputStream(socket.getInputStream());
                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();//flush od razu zapisuje dane wyjsciowe
                }
                catch (IOException e)
                {
                    System.out.println("Gracz poszedl!: " + e);
                    dezercja = true;
                    gra.Klikniecie(-1, -1, dezercja);
                    watek_trwa = false;
                }

                if (dezercja != true)
                {
                    try
                    {
                        info_dla_klienta(gra.getTablica(), gra.getAktualna_kolumna(), gra.getAktulny_wiersz(), true, gra.getAktualny_gracz(), gra.getRuchy(), PUSTE, Kolor);
                        out.writeObject(serwer_do_klienta);

                        while (watek_trwa)
                        {
                            if (gra.getAktualny_gracz() == Kolor && gra.isGra_trwa())
                            {
                                info_dla_klienta(gra.getTablica(), gra.getAktualna_kolumna(), gra.getAktulny_wiersz(), gra.isGra_trwa(), gra.getAktualny_gracz(), gra.getRuchy(), gra.getZwyciesca(), Kolor);
                                out.reset(); //musi byc reset watku, bo inaczej nie bedzie przkazywac ruchu
                                out.writeObject(serwer_do_klienta);

                                klient_do_serwera = (Pionek_klient) in.readObject(); // odczytanie wadomosci od klienta

                                // zebranie informacji
                                gra.Klikniecie(klient_do_serwera.getWybrany_wiersz(), klient_do_serwera.getWybrana_kolumna(), klient_do_serwera.getDezercja());

                                // wysłanie
                                info_dla_klienta(gra.getTablica(), gra.getAktualna_kolumna(), gra.getAktulny_wiersz(), gra.isGra_trwa(), gra.getAktualny_gracz(), gra.getRuchy(), gra.getZwyciesca(), Kolor);

                                out.reset();//musi byc reset watku, bo inaczej nie bedzie informacji o ruchu
                                out.writeObject(serwer_do_klienta);

                            }
                            else if (!gra.isGra_trwa() && gra.getZwyciesca() != PUSTE) //jezeli jest koniec gry
                            {
                                info_dla_klienta(gra.getTablica(), gra.getAktualna_kolumna(), gra.getAktulny_wiersz(), gra.isGra_trwa(), gra.getAktualny_gracz(), gra.getRuchy(), gra.getZwyciesca(), Kolor);
                                out.reset();
                                out.writeObject(serwer_do_klienta);
                                watek_trwa = false;// zeby przerwac petle
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        // gdyby gracz wyszedl to pozostaly wygrywa
                        dezercja = true;
                        gra.Klikniecie(-1, -1, dezercja);
                        watek_trwa = false;
                    }
                    catch (ClassNotFoundException e)
                    {
                        dezercja = true;
                        gra.Klikniecie(-1, -1, dezercja);
                        watek_trwa = false;
                    }
                    finally
                    {
                        try
                        {
                            out.close();
                            in.close();
                        }
                        catch (IOException e)
                        {
                        }
                    }
                }
            }
        }

        private void info_dla_klienta(int[][] tablica, int wybrana_kolumna, int wybrany_wiersz, boolean gra_trwa, int aktualny_gracz, Pionek_klient[] ruchy, int zwyciesca, int kolor)
        {
            serwer_do_klienta.setTablica(tablica);
            serwer_do_klienta.setWybrana_kolumna(wybrana_kolumna);
            serwer_do_klienta.setWybrany_wiersz(wybrany_wiersz);
            serwer_do_klienta.setTrwa(gra_trwa);
            serwer_do_klienta.setAktualny_gracz(aktualny_gracz);
            serwer_do_klienta.setMozliwe_ruchy(ruchy);
            serwer_do_klienta.setZwyciesca(zwyciesca);
            serwer_do_klienta.setKolor(kolor);
        }
    }
}


