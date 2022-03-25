package requests.synch;

public class Bank {
    private final Accounts accounts;

    public Bank(Accounts accounts) {
        this.accounts = accounts;
    }

    public void transfer(long fromId, long toId, double money) {
        var fromAcc = accounts.findById(fromId);
        var toAcc = accounts.findById(toId);

        var minAcc = fromAcc.getId() < toAcc.getId() ? fromAcc : toAcc;
        var maxAcc = fromAcc.getId() >= toAcc.getId() ? fromAcc : toAcc;

        synchronized (minAcc) {
            synchronized (maxAcc) {
                if (!fromAcc.hasMoney(money)) {
                    throw new IllegalStateException("Acc doesn't have enough money, " + money);
                }
                fromAcc.changeBalance(-money);
                toAcc.changeBalance(money);
            }
        }
    }
}
