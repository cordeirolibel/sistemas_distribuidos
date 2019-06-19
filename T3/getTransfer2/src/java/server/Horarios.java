/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author cordeiro
 */
public class Horarios implements Serializable{
    boolean calendario [][];
    int n_dias;

    //datas e horas q inicia o calendario
    int dia1;
    int mes1;
    int hora1;

    LocalDate startDate;

    public Horarios(){
        this(26,5,27,6,8,12);
    }

    public Horarios(int dia1_, int mes1_, int dia2, int mes2, int hora_inicio_do_dia, int n_horas_por_dia){
        dia1 = dia1_;
        mes1 = mes1_;
        hora1 = hora_inicio_do_dia;

        //calendario com n_dias
        startDate =  LocalDate.of(2019, mes1, dia1);
        LocalDate endDate =  LocalDate.of(2019, mes2, dia2);
        n_dias = (int)ChronoUnit.DAYS.between(startDate, endDate);

        //criando calendario
        calendario = new boolean[n_dias][n_horas_por_dia];

        for (int i=0;i<n_dias;i++){
            for (int j=0;j<n_horas_por_dia;j++) {
                calendario[i][j] = true;
            }
        }
    }

    //retorna true se esta disponivel um horario
    public boolean disponivel(int dia, int mes, int hora){

        //procurando a posicao no calendario
        startDate =  LocalDate.of(2019, mes1, dia1);
        LocalDate endDate =  LocalDate.of(2019, mes, dia);
        int i_dias = (int)ChronoUnit.DAYS.between(startDate, endDate);
        int i_horas = hora-hora1;
        return calendario[i_dias][i_horas];

    }

    //coloca com indisponivel um horario
    public void set(int dia, int mes, int hora){

        //procurando a posicao no calendario
        startDate =  LocalDate.of(2019, mes1, dia1);
        LocalDate endDate =  LocalDate.of(2019, mes, dia);
        int i_dias = (int)ChronoUnit.DAYS.between(startDate, endDate);
        int i_horas = hora-hora1;
        System.out.printf("---------------------- \n");
        System.out.printf("%d %d %d %d %d \n", i_dias, i_horas, dia, mes, hora);
        System.out.printf("%b \n",calendario[i_dias][i_horas]);
        calendario[i_dias][i_horas] = false;
        System.out.printf("%b \n",calendario[i_dias][i_horas]);
    }

    public int get_n_dias(){
        return n_dias;
    }


}
