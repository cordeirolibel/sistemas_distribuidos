package multicastpackage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Relogio extends Thread{

    long ajusteTempo;
    SimpleDateFormat sdf;
    LinkedList<Processo> processos;
    boolean ligado;
    int tempo_ms;

    public Relogio(LinkedList<Processo> aprocessos, int atempo_ms){
        // args give message contents and destination multicast group (e.g. "228.5.6.7")
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        ajusteTempo = 0;
        aprocessos = processos;
        tempo_ms = atempo_ms;

        this.start();
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

    public void run()  {
        //envia mensagem de estou vivo
        try {
            while(true) {
                if(ligado) {

                }
                Thread.sleep(tempo_ms);
            }
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}

    }

    public void Berkeley(){
        Processo processo;
        int size = processos.size();
        //for (int i=0;i<size;i++){
        //    processo = processos.get(i);
        //   if (processo.id == id){
        //        processo.setTempo(tempo);
        //    }
        //}

    }
}
