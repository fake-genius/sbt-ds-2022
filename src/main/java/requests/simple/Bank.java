package requests.simple;

public class Bank {
    private final Accounts accounts;

    public Bank(Accounts accounts) {
        this.accounts = accounts;
    }

    public void transfer(long fromId, long toId, double money) {
        var fromAcc = accounts.findById(fromId);
        var toAcc = accounts.findById(toId);
        if (!fromAcc.hasMoney(money)) {
            throw new IllegalStateException("Acc doesn't have enough money, " + money);
        }
        fromAcc.changeBalance(-money);
        toAcc.changeBalance(money);
    }
}
