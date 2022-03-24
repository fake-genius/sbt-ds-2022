package requests.simple;

public class Account {
    private final long id;
    private double balance;

    public Account(long id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public boolean hasMoney(double money) {
        return balance >= money;
    }

    public void changeBalance(double money) {
        balance += money;
    }
}
