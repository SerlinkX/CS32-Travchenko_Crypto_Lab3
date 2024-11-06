import java.security.*;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Block {
    private int version;
    private String prevHash;
    private long timestamp;
    private int difficultyTarget;
    private int nonce;
    private String merkleRoot;
    private List<Transaction> transactions;
    private String blockHash;
    private String digitalSignature;

    // Конструктор для блока
    public Block(int version, String prevHash, long timestamp, int difficultyTarget, int nonce, String merkleRoot, List<Transaction> transactions) throws Exception {
        this.version = version;
        this.prevHash = prevHash;
        this.timestamp = timestamp;
        this.difficultyTarget = difficultyTarget;
        this.nonce = nonce;
        this.merkleRoot = merkleRoot;
        this.transactions = new ArrayList<>(transactions);
        this.blockHash = calculateBlockHash();
    }

    // Метод для генерації гешу блока
    public String calculateBlockHash() throws NoSuchAlgorithmException {
        String data = version + prevHash + timestamp + difficultyTarget + nonce + merkleRoot + transactions.toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    // Метод для підпису блока
    public void signBlock(PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(blockHash.getBytes(StandardCharsets.UTF_8));
        byte[] signedData = signature.sign();
        this.digitalSignature = Base64.getEncoder().encodeToString(signedData);
    }

    // Метод для верифікації підпису блока
    public boolean verifyBlockSignature(PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(blockHash.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(digitalSignature));
    }

    @Override
    public String toString() {
        return "Block{" +
                "version=" + version +
                ", prevHash='" + prevHash + '\'' +
                ", timestamp=" + timestamp +
                ", difficultyTarget=" + difficultyTarget +
                ", nonce=" + nonce +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", transactions=" + transactions +
                ", blockHash='" + blockHash + '\'' +
                ", digitalSignature='" + digitalSignature + '\'' +
                '}';
    }
}
