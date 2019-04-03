
package multicastpackage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Relogio {

    static long ajusteTempo;
    static SimpleDateFormat sdf;

    public Relogio(){
        // args give message contents and destination multicast group (e.g. "228.5.6.7")
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        ajusteTempo = 0;
    }

    // adiciona um ajuste ao relogio
    public static long ajustaTempo(long ajuste) {
        ajusteTempo = ajusteTempo + ajuste;
        return ajusteTempo;
    }

    //retorna o time do PC em formato string
    //  "2019/04/03 15:06:47.954"
    public static String timePCstring(){
        return sdf.format(new Date(timePC()));
    }

    //retorna o time do PC em formato long
    public static long timePC(){
        //time do pc em fomato long
        Date now = new Date();
        long nowL = now.getTime();

        //add ajuste
        long timeProcessoL = nowL + ajusteTempo;
        Date timeProcesso =  new Date(timeProcessoL);
        return timeProcessoL;
    }
}
