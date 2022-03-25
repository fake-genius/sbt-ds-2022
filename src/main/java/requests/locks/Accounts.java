package requests.locks;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

public class Accounts {
    private final EntityManager entityManager = Persistence.createEntityManagerFactory("org.jbpm.persistence.jp").createEntityManager();

    public Account findById(long id) {
        return entityManager.find(Account.class, id);
    }

    public void changeAccountBalance(Account acc, double money, LockModeType lockModeType) {
        entityManager.lock(acc, lockModeType);
        acc.changeBalance(-money);
        entityManager.getTransaction().commit();
    }
}
