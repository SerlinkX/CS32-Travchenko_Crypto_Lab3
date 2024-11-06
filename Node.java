import java.security.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node implements Runnable {
    private Blockchain blockchain;
    private List<Transaction> mempool;
    private String nodeName;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private List<Node> nodes; // Інші вузли для імітації мережі
    private static Lock lock = new ReentrantLock();

    // Конструктор
    public Node(String nodeName, List<Node> nodes) throws NoSuchAlgorithmException {
        this.nodeName = nodeName;
        this.blockchain = new Blockchain();
        this.mempool = new ArrayList<>();
        this.nodes = nodes;
        // Генерація пари ключів для цифрового підпису
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    // Додавання транзакції в мемпул (тимчасове сховище)
    public void addTransaction(Transaction tx) {
        mempool.add(tx);
    }

    // Майнінг нового блоку
    public void mineBlock() throws Exception {
        lock.lock();
        try {
            List<Transaction> blockTransactions = new ArrayList<>(mempool);
            if (blockTransactions.isEmpty()) {
                System.out.println(nodeName + " має порожній мемпул.");
                return;
            }
            mempool.clear();

            // Створення нового блока
            String prevHash = blockchain.getLastBlock() != null ? blockchain.getLastBlock().calculateBlockHash() : "0";
            Block newBlock = new Block(1, prevHash, System.currentTimeMillis(), 2, new Random().nextInt(1000), "merkleRoot", blockTransactions);
            newBlock.signBlock(privateKey);

            // Імітація "майнінгу" (робота по добуванню блоку)
            System.out.println(nodeName + " починає майнінг блоку...");
            Thread.sleep(new Random().nextInt(3000)); // Імітація складності

            // Розсилка блоку іншим вузлам
            broadcastBlock(newBlock);

            System.out.println(nodeName + " успішно добув блок: " + newBlock);
        } finally {
            lock.unlock();
        }
    }

    // Метод для отримання блоку від іншого вузла
    public void receiveBlock(Block block) throws Exception {
        if (block.verifyBlockSignature(publicKey)) {
            blockchain.addBlock(block);
            System.out.println(nodeName + " успішно додав блок від іншого вузла: " + block);
        } else {
            System.out.println(nodeName + " отримав недійсний блок.");
        }
    }

    // Розсилка блоку іншим вузлам
    public void broadcastBlock(Block block) throws Exception {
        for (Node node : nodes) {
            if (node != this) {
                node.receiveBlock(block);
            }
        }
    }

    @Override
    public void run() {
        try {
            mineBlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
