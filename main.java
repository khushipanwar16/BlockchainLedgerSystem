import core.Blockchain;
import core.Transaction;
import java.util.Scanner;
import structures.MyArray;

public class main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Blockchain blockchain = new Blockchain(); // Initialize blockchain
        blockchain.loadBlockchainFromFiles(); // Load existing blocks

        int choice = -1;

        do {
            System.out.println("\nBlockchain Ledger System");
            System.out.println("-------------------------");
            System.out.println("1. Submit New Transaction(s)");
            System.out.println("2. Display Blockchain");
            System.out.println("3. Validate Blockchain");
            System.out.println("4. Display Blockchain (Reverse)");
            System.out.println("5. View Transactions by User");
            System.out.println("6. Search Transactions by Amount Range");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            choice = Integer.parseInt(scanner.nextLine());
            System.out.println("You chose: " + choice);


            switch (choice) {
                case 1:
                    submitNewTransactions(blockchain, scanner);
                    break;

                case 2:
                    blockchain.displayBlockchain();
                    break;

                case 3:
                    core.Validator validator = new core.Validator(blockchain.getChain());
                    validator.validateBlockchain();
                    break;

                case 4:
                    blockchain.displayBlockchainReverse();
                    break;

                case 5:
                    System.out.print("Enter username to search transactions: ");
                    String username = scanner.nextLine();
                    blockchain.viewTransactionsByUser(username);
                    break;

                case 6:
                    System.out.print("Enter minimum amount: ");
                    int minAmount = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter maximum amount: ");
                    int maxAmount = Integer.parseInt(scanner.nextLine());
                    blockchain.searchTransactionsByAmount(minAmount, maxAmount);
                    break;

                case 7:
                    System.out.println("Saving state and exiting...");
                    break;

                default:
                    System.out.println("❗ Invalid choice. Please try again.");
            }

        } while (choice != 7);

        scanner.close();
    }

    // Submit transactions manually via console
    private static void submitNewTransactions(Blockchain blockchain, Scanner scanner) {
        MyArray<Transaction> transactionList = new MyArray<>();

        System.out.println("Enter transactions (format: Sender,Receiver,Amount)");
        System.out.println("Type 'done' when finished.");

        while (true) {
            System.out.print("Transaction: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            String[] parts = input.split(",");
            if (parts.length != 3) {
                System.out.println("❗ Invalid format. Try again.");
                continue;
            }

            String sender = parts[0].trim();
            String receiver = parts[1].trim();
            int amount;

            try {
                amount = Integer.parseInt(parts[2].trim());
            } catch (NumberFormatException e) {
                System.out.println("❗ Amount must be a number.");
                continue;
            }

            Transaction tx = new Transaction(sender, receiver, amount);
            transactionList.add(tx);
        }

        if (transactionList.size() > 0) {
            blockchain.addBlock(transactionList);
            System.out.println("✅ Block created and added to blockchain.");
        } else {
            System.out.println("❗ No transactions were entered. Block not created.");
        }
    }
}
