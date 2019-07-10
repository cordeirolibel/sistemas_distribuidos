package tranCar;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Cliente {
    int id_clie;
    float saldo;
    ReentrantLock rlock;

    public Cliente(int id_clie,float saldo){
        this.id_clie = id_clie;
        this.saldo = saldo;
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
        rlock.unlock();
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
