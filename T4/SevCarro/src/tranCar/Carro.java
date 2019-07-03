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

    public boolean isLivre() {
        return livre;
    }


    public void carroAlugado() {
        livre = false;
    }
    public void carroLivre() {
        livre = true;
    }

    //Bloqueia o uso do carro
    //timeout de 0.1s
    //retorna true se foi possivel realizar a operacao
    public boolean lock() {
        try {
            rlock.tryLock(100L, TimeUnit.MILLISECONDS);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Libera o recurso (carro)
    public void unlock() {
        rlock.unlock();
    }


}
