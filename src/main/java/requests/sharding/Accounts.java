package requests.sharding;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

public class Accounts {
    private final EntityManager entityManager = Persistence.createEntityManagerFactory("org.jbpm.persistence.jp").createEntityManager();

    public Account findById(long id) {
        return entityManager.find(Account.class, id);
    }

    public void changeAccountBalance(Account acc, double money) {
        entityManager.lock(acc, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        acc.changeBalance(-money);
        entityManager.getTransaction().commit();
    }

    public void changeAccountBalanceWithLock(Account acc, double money, LockModeType lockModeType) {
        entityManager.lock(acc, lockModeType);
        acc.changeBalance(-money);
        entityManager.getTransaction().commit();
    }

    public Accounts combineAccounts(Accounts accountsAdding) {
        Accounts accounts = new Accounts();
        // some logic combining all accountsAdding and this accounts;
        return accounts;
    }
}
