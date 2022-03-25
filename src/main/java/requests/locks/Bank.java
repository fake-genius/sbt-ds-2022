package requests.locks;

import javax.persistence.*;

public class Bank {
    private final Accounts accounts;
    private final EntityManagerFactory entityManagerFactory;

    public Bank(Accounts accounts) {
        this.accounts = accounts;
        this.entityManagerFactory = Persistence.createEntityManagerFactory("org.jbpm.persistence.jp");
    }

    public void transferOptimistic(long fromId, long toId, double money) {
        transferLock(fromId, toId, money, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    public void transferPessimistic(long fromId, long toId, double money) {
        transferLock(fromId, toId, money, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
    }

    private void transferLock(long fromId, long toId, double money, LockModeType lockModeType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var fromAcc = entityManager.find(Account.class, fromId);
            var toAcc = entityManager.find(Account.class, fromId);

            if (!fromAcc.hasMoney(money)) {
                throw new IllegalStateException("Acc doesn't have enough money, " + money);
            }

            entityManager.lock(fromAcc, lockModeType);
            fromAcc.changeBalance(-money);
            entityManager.getTransaction().commit();
            entityManager.lock(toAcc, lockModeType);
            toAcc.changeBalance(money);
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (PersistenceException e) {
            System.out.println("Transaction failed");
        }
    }
}
