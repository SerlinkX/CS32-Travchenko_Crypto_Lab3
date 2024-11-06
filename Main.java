import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        // Створення списку вузлів
        List<Node> nodes = new ArrayList<>();

        // Створення кількох вузлів
        Node node1 = new Node("Node 1", nodes);
        Node node2 = new Node("Node 2", nodes);
        Node node3 = new Node("Node 3", nodes);

        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        // Генерація випадкових транзакцій і додавання до мемпулів
        for (int i = 0; i < 5; i++) {
            Transaction tx = Transaction.generateRandomTransaction();
            node1.addTransaction(tx);
            node2.addTransaction(tx);
            node3.addTransaction(tx);
        }

        // Створення потоків для паралельного майнінгу
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(node1);
        executor.submit(node2);
        executor.submit(node3);

        // Завершення роботи потоків
        executor.shutdown();
    }
}
