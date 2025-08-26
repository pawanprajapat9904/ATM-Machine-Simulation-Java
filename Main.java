import java.util.*;
import java.text.SimpleDateFormat;

// ------------------ Account Class ------------------
class Account {
    private String accountNumber;
    private String pin;
    private double balance;
    private List<String> transactionHistory;

    public Account(String accountNumber, String pin, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Account created", initialDeposit);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposit", amount);
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            addTransaction("Withdrawal", amount);
            return true;
        }
        return false;
    }

    public void addTransaction(String type, double amount) {
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        transactionHistory.add(type + " : ₹" + amount + " on " + timestamp);
    }

    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("📜 Transaction History:");
            for (String tx : transactionHistory) {
                System.out.println(" - " + tx);
            }
        }
    }
}

// ------------------ ATM Class ------------------
class ATM {
    private Map<String, Account> accounts = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n===== ATM Menu =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> {
                    System.out.println("✅ Thank you for using ATM. Goodbye!");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Try again.");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter 10-digit Account Number: ");
        String accountNumber = scanner.next();
        if (accounts.containsKey(accountNumber) || accountNumber.length() != 10) {
            System.out.println("⚠️ Invalid or already used account number.");
            return;
        }

        System.out.print("Enter 4-digit PIN: ");
        String pin1 = scanner.next();
        System.out.print("Confirm 4-digit PIN: ");
        String pin2 = scanner.next();

        if (!pin1.equals(pin2) || pin1.length() != 4) {
            System.out.println("⚠️ PIN mismatch or invalid length.");
            return;
        }

        System.out.print("Enter initial deposit: ");
        double deposit = getDoubleInput();

        accounts.put(accountNumber, new Account(accountNumber, pin1, deposit));
        System.out.println("🎉 Account created successfully!");
    }

    private void login() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.next();
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("⚠️ Account not found.");
            return;
        }

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter PIN: ");
            String pin = scanner.next();
            if (account.getPin().equals(pin)) {
                System.out.println("✅ Login successful!");
                accountMenu(account);
                return;
            } else {
                attempts++;
                System.out.println("❌ Wrong PIN. Attempts left: " + (3 - attempts));
            }
        }
        System.out.println("🚫 Account locked for security reasons.");
    }

    private void accountMenu(Account account) {
        while (true) {
            System.out.println("\n===== Account Menu =====");
            System.out.println("1. Balance Inquiry");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction History");
            System.out.println("5. Change PIN");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> System.out.println("💰 Balance: ₹" + account.getBalance());
                case 2 -> deposit(account);
                case 3 -> withdraw(account);
                case 4 -> account.showTransactionHistory();
                case 5 -> changePin(account);
                case 6 -> {
                    System.out.println("🔒 Logged out.");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice.");
            }
        }
    }

    private void deposit(Account account) {
        System.out.print("Enter deposit amount: ");
        double amount = getDoubleInput();
        if (amount > 0) {
            account.deposit(amount);
            System.out.println("✅ Deposit successful.");
        } else {
            System.out.println("⚠️ Invalid amount.");
        }
    }

    private void withdraw(Account account) {
        System.out.print("Enter withdrawal amount: ");
        double amount = getDoubleInput();
        if (account.withdraw(amount)) {
            System.out.println("✅ Withdrawal successful.");
        } else {
            System.out.println("⚠️ Insufficient balance or invalid amount.");
        }
    }

    private void changePin(Account account) {
        System.out.print("Enter current PIN: ");
        String oldPin = scanner.next();
        if (!account.getPin().equals(oldPin)) {
            System.out.println("⚠️ Incorrect current PIN.");
            return;
        }

        System.out.print("Enter new 4-digit PIN: ");
        String newPin1 = scanner.next();
        System.out.print("Confirm new 4-digit PIN: ");
        String newPin2 = scanner.next();

        if (newPin1.equals(newPin2) && newPin1.length() == 4) {
            account.setPin(newPin1);
            System.out.println("✅ PIN changed successfully.");
        } else {
            System.out.println("⚠️ PIN mismatch or invalid length.");
        }
    }

    // Utility: Safely get integer input
    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("⚠️ Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // Utility: Safely get double input
    private double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.println("⚠️ Invalid input. Enter a valid number.");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}

// ------------------ Main Class ------------------
public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
