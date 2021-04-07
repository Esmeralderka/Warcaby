package gra;

import java.io.IOException;
import java.net.ServerSocket;
public class Serwer
{
    private static int numer_meczu = 1;

    public static void main(String[] args) throws IOException
    {
        ServerSocket Socket = new ServerSocket(9001);
        System.out.println("Serwer działa");

        try
        {
            while (true)
            {
                Mecz_serwer mecz = new Mecz_serwer(numer_meczu);
                System.out.println("Oczekiwanie na graczy");

                try
                {
                    Mecz_serwer.Gracz biale = mecz.new Gracz(Socket.accept(), Gra_serwer.BIALE);
                    System.out.println("Do meczu nr " + numer_meczu + " dołączył gracz pierwszy");

                    Mecz_serwer.Gracz czarne = mecz.new Gracz(Socket.accept(), Gra_serwer.CZARNE);
                    System.out.println("Do meczu nr " + numer_meczu + " dołączył gracz drugi");

                    biale.start();
                    czarne.start();
                    numer_meczu++;
                }
                catch (IOException e)
                {
                }
            }
        }
        finally
        {
            Socket.close();
        }
    }
}
