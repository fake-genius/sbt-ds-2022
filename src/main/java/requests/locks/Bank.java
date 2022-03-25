package requests.locks;

import javax.persistence.*;

public class Bank {
    private final Accounts accounts;

    public Bank(Accounts accounts) {
        this.accounts = accounts;
    }

    public void transferOptimistic(long fromId, long toId, double money) {
        transferLock(fromId, toId, money, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    public void transferPessimistic(long fromId, long toId, double money) {
        transferLock(fromId, toId, money, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
    }

    private void transferLock(long fromId, long toId, double money, LockModeType lockModeType) {
        try {
            var fromAcc = accounts.findById(fromId);
            var toAcc = accounts.findById(toId);

            if (!fromAcc.hasMoney(money)) {
                throw new IllegalStateException("Acc doesn't have enough money, " + money);
            }

            accounts.changeAccountBalance(fromAcc, -money, lockModeType);
            accounts.changeAccountBalance(toAcc, money, lockModeType);
        } catch (PersistenceException e) {
            System.out.println("Transaction failed");
        }
    }
}
