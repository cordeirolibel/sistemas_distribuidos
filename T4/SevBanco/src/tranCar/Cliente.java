package tranCar;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Cliente implements Serializable {
    int id_clie;
    float saldo;
    ReentrantLock rlock;

    public Cliente(int id_clie,float saldo){
        this.id_clie = id_clie;
        this.saldo = saldo;
        rlock = new ReentrantLock();
    }

    //Bloqueia o uso do cliente
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

    //Libera o recurso (cliente)
    public void liberaRecurso() {
        //rlock.unlock();
        //substitui o lock por um novo, pois foi definido em outra thread
        if (rlock instanceof ReentrantLock) {
            rlock = new ReentrantLock();
        } else {
            throw new UnsupportedOperationException(
                    "Cannot force unlock of lock type "
                            + rlock.getClass().getSimpleName());
        }
    }

    public boolean temSaldo(float valor){
        return saldo>=valor;
    }

    public void debitaValor(float valor){
        saldo -= valor;
    }

    public int getId_clie(){
        return id_clie;
    }


}
