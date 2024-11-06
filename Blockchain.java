import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;

    // Конструктор
    public Blockchain() {
        this.chain = new ArrayList<>();
    }

    // Метод для додавання нового блока
    public void addBlock(Block block) {
        this.chain.add(block);
    }

    // Метод для отримання останнього блока
    public Block getLastBlock() {
        if (chain.isEmpty()) return null;
        return chain.get(chain.size() - 1);
    }

    @Override
    public String toString() {
        return "Blockchain{" +
                "chain=" + chain +
                '}';
    }
}
