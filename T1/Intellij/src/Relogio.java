package multicastpackage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Relogio {

    long ajusteTempo;
    SimpleDateFormat sdf;
    Processos processos;
    boolean ligado;
    int tempo_ms;

    public Relogio(Processos aprocessos, int atempo_ms,long ajusteTempoManual){
        // args give message contents and destination multicast group (e.g. "228.5.6.7")
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        ajusteTempo = ajusteTempoManual;
        aprocessos = processos;
        tempo_ms = atempo_ms;

    }

    // adiciona um ajuste ao relogio
    public long ajustaTempo(long ajuste) {
        ajusteTempo = ajusteTempo + ajuste;
        return ajusteTempo;
    }

    //retorna o time do PC em formato string
    //  "2019/04/03 15:06:47.954"
    public String timePCstring(){
        return sdf.format(new Date(timePC()));
    }

    //retorna o time do PC em formato long
    public long timePC(){
        //time do pc em fomato long
        Date now = new Date();
        long nowL = now.getTime();

        //add ajuste
        long timeProcessoL = nowL + ajusteTempo;
        Date timeProcesso =  new Date(timeProcessoL);
        return timeProcessoL;
    }

}
