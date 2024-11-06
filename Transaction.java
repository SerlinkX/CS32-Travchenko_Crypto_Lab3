import java.security.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Base64;

public class Transaction {
    private String inputAddress;
    private List<String> outputAddresses;
    private double amount;
    private long txTimestamp;
    private String txHash;
    private String digitalSignature;

    public Transaction(String inputAddress, List<String> outputAddresses, double amount, long txTimestamp) throws Exception {
        this.inputAddress = inputAddress;
        this.outputAddresses = new ArrayList<>(outputAddresses);
        this.amount = amount;
        this.txTimestamp = txTimestamp;
        this.txHash = generateTransactionHash();
    }

    // Генерація випадкових транзакцій
    public static Transaction generateRandomTransaction() throws Exception {
        Random rand = new Random();
        String inputAddress = "address" + rand.nextInt(100);
        List<String> outputAddresses = Arrays.asList("address" + rand.nextInt(100), "address" + rand.nextInt(100));
        double amount = rand.nextDouble() * 100;
        long timestamp = System.currentTimeMillis();
        return new Transaction(inputAddress, outputAddresses, amount, timestamp);
    }

    // Генерація хешу транзакції
    private String generateTransactionHash() throws NoSuchAlgorithmException {
        String data = inputAddress + outputAddresses.toString() + amount + txTimestamp;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public void signTransaction(PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(txHash.getBytes(StandardCharsets.UTF_8));
        byte[] signedData = signature.sign();
        this.digitalSignature = Base64.getEncoder().encodeToString(signedData);
    }

    public boolean verifyTransactionSignature(PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(txHash.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(digitalSignature));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "inputAddress='" + inputAddress + '\'' +
                ", outputAddresses=" + outputAddresses +
                ", amount=" + amount +
                ", txTimestamp=" + txTimestamp +
                ", txHash='" + txHash + '\'' +
                ", digitalSignature='" + digitalSignature + '\'' +
                '}';
    }
}
