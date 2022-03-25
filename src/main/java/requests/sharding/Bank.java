package requests.sharding;

import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;

public class Bank {
    private final Accounts accountsTotal;
    private final Accounts accountsFirst;
    private final Accounts accountsSecond;

    public Bank(Accounts accountsFirst, Accounts accountsSecond) {
        this.accountsFirst = accountsFirst;
        this.accountsSecond = accountsSecond;
        this.accountsTotal = accountsFirst.combineAccounts(accountsSecond);
    }

    public void transferSharding(long fromId, long toId, double money) {
        try {
            var fromAcc = accountsFirst.findById(fromId);
            var toAcc = accountsSecond.findById(toId);

            if (!fromAcc.hasMoney(money)) {
                throw new IllegalStateException("Acc doesn't have enough money, " + money);
            }

            accountsTotal.changeAccountBalanceWithLock(fromAcc, -money, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            accountsFirst.changeAccountBalance(fromAcc, -money);
            accountsSecond.changeAccountBalance(toAcc, money);
            accountsTotal.changeAccountBalanceWithLock(toAcc, money, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        } catch (PersistenceException e) {
            System.out.println("Transaction failed");
        }
    }
}
