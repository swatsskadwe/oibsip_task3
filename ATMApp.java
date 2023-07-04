import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String userId;
    private String pin;
    private List<Transaction> transactionHistory;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public void deposit(double amount) {
        Transaction transaction = new Transaction("Deposit", amount);
        addTransaction(transaction);
        System.out.println("Successfully deposited Rs" + amount);
    }

    public void withdraw(double amount) {
        Transaction transaction;
        if (amount > 0 && amount <= getBalance()) {
            transaction = new Transaction("Withdraw", -amount);
            addTransaction(transaction);
            System.out.println("Successfully withdrew Rs" + amount);
        } else {
            System.out.println("Invalid amount or insufficient balance.");
        }
    }

    public void transfer(User recipient, double amount) {
        Transaction transaction;
        if (amount > 0 && amount <= getBalance()) {
            transaction = new Transaction("Transfer to " + recipient.getUserId(), -amount);
            addTransaction(transaction);
            recipient.deposit(amount);
            System.out.println("Successfully transferred Rs" + amount + " to " + recipient.getUserId());
        } else {
            System.out.println("Invalid amount or insufficient balance.");
        }
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction transaction : transactionHistory) {
            balance += transaction.getAmount();
        }
        return balance;
    }
}

class ATM {
    private List<User> users;

    public ATM() {
        this.users = new ArrayList<>();
        // Add dummy users for testing
        users.add(new User("payal", "1234"));
        users.add(new User("radha", "5678"));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the ATM!");

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        User user = getUser(userId, pin);
        if (user != null) {
            System.out.println("Login successful!");

            int choice;
            do {
                displayMenu();
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        displayTransactionHistory(user);
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline character
                        user.withdraw(withdrawAmount);
                        break;
                    case 3:
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline character
                        user.deposit(depositAmount);
                        break;
                    case 4:
                        System.out.print("Enter recipient's User ID: ");
                        String recipientId = scanner.nextLine();
                        User recipient = getUser(recipientId);
                        if (recipient != null) {
                            System.out.print("Enter amount to transfer: ");
                            double transferAmount = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline character
                            user.transfer(recipient, transferAmount);
                        } else {
                            System.out.println("Recipient not found.");
                        }
                        break;
                    case 5:
                        System.out.println("Thank you.Visit Again!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5);
        } else {
            System.out.println("Invalid credentials. Login failed.");
        }

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\nATM Menu:");
        System.out.println("1. View Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    private void displayTransactionHistory(User user) {
        System.out.println("\nTransaction History:");
        List<Transaction> transactions = user.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getType() + ": Rs" + transaction.getAmount());
            }
        }
        System.out.println("Current Balance: Rs" + user.getBalance());
    }

    private User getUser(String userId, String pin) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                return user;
            }
        }
        return null;
    }

    private User getUser(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}

public class ATMApp {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
