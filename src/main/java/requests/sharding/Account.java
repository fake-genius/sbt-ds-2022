package requests.sharding;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Account {

    @Id
    private final long id;
    private double balance;
    @Version
    private long version;

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
