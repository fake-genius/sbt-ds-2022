package requests.locks;

import javax.persistence.*;

public class BankOptimistic {
    private final Accounts accounts;
    private final EntityManagerFactory entityManagerFactory;

    public BankOptimistic(Accounts accounts) {
        this.accounts = accounts;
        this.entityManagerFactory = Persistence.createEntityManagerFactory("org.jbpm.persistence.jp");
    }

    public void transfer(long fromId, long toId, double money) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            var fromAcc = entityManager.find(Account.class, fromId);
            var toAcc = entityManager.find(Account.class, fromId);

            if (!fromAcc.hasMoney(money)) {
                throw new IllegalStateException("Acc doesn't have enough money, " + money);
            }

            entityManager.lock(fromAcc, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            fromAcc.changeBalance(-money);
            entityManager.getTransaction().commit();

            entityManager.lock(toAcc, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            toAcc.changeBalance(money);
            entityManager.getTransaction().commit();

            entityManager.close();
        } catch (PersistenceException) {
            System.out.println("Transaction failed");
        }
    }
}
