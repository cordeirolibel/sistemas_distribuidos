package tranCar;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Carro implements Serializable {
    String modelo;
    int id_carro;
    boolean livre;
    ReentrantLock rlock;

    public Carro(int id,String _modelo){
        id_carro = id;
        livre = true;
        modelo = _modelo;
        rlock = new ReentrantLock();
    }

    public int getId_carro() {
        return id_carro;
    }

    public boolean isLivre() {
        return livre;
    }


    public void carroAlugado() {
        livre = false;
    }
    public void carroLivre() {
        livre = true;
    }

    //Bloqueia o uso do carro como recurso
    //timeout de 0.1s
    //retorna true se foi possivel realizar a operacao
    public boolean bloqueiaRecurso() {
        try {
            rlock.tryLock(100L, TimeUnit.MILLISECONDS);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Libera o uso do carro como recurso
    public void liberaRecurso() {
        //substitui o lock por um novo, pois foi definido em outra thread

        if (rlock instanceof ReentrantLock) {
            rlock = new ReentrantLock();
        } else {
            throw new UnsupportedOperationException(
                    "Cannot force unlock of lock type "
                            + rlock.getClass().getSimpleName());
        }
    }

    public String carInfo(){
        return String.format(" ID[%s] - Veiculo: %s\n", id_carro, modelo);
    }

}
