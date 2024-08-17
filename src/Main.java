import java.util.concurrent.Semaphore;

public class Main{
    // Número de filósofos
    private static final int N = 5;

    // Criação dos semáforos, um para cada hashi
    private static final Semaphore[] hashis = new Semaphore[N];

    static {
        for (int i = 0; i < N; i++) {
            hashis[i] = new Semaphore(1);
        }
    }

    public static void main(String[] args) {
        // Criação de threads para cada filósofo
        Filosofo[] filosofos = new Filosofo[N];
        for (int i = 0; i < N; i++) {
            filosofos[i] = new Filosofo(i);
            new Thread(filosofos[i]).start();
        }
    }

    // Classe que representa um filósofo
    static class Filosofo implements Runnable {
        private final int id;

        Filosofo(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    pensar();
                    pegarHashis();
                    comer();
                    soltarHashis();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void pensar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está pensando.");
            Thread.sleep((long) (Math.random() * 3000));
        }

        private void pegarHashis() throws InterruptedException {
            int hashiEsquerda = id;
            int hashiDireita = (id + 1) % N;

            // Para evitar deadlock, pegamos o hashi de menor índice primeiro
            if (id % 2 == 0) {
                hashis[hashiEsquerda].acquire();
                hashis[hashiDireita].acquire();
            } else {
                hashis[hashiDireita].acquire();
                hashis[hashiEsquerda].acquire();
            }

            System.out.println("Filósofo " + id + " pegou os hashis " + hashiEsquerda + " e " + hashiDireita + ".");
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está comendo.");
            Thread.sleep((long) (Math.random() * 3000));
        }

        private void soltarHashis() {
            int hashiEsquerda = id;
            int hashiDireita = (id + 1) % N;

            hashis[hashiEsquerda].release();
            hashis[hashiDireita].release();

            System.out.println("Filósofo " + id + " soltou os hashis " + hashiEsquerda + " e " + hashiDireita + ".");
        }
    }
}
