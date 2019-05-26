import transfer.Horarios;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        Horarios horarios = new Horarios(26,5,16,6,8,12);
        System.out.printf("%d\n",horarios.get_n_dias());

        if(horarios.disponivel(27,5,14)){
            System.out.printf("ta livre\n");
        }
        else{
            System.out.printf("nao ta livre\n");
        }

        horarios.set(27,5,14);
        if(horarios.disponivel(27,5,14)){
            System.out.printf("ta livre\n");
        }
        else{
            System.out.printf("nao ta livre\n");
        }

    }
}
