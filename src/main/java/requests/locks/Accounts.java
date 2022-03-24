package requests.locks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Accounts {
    private final Map<Long, Account> accById = new ConcurrentHashMap<>();

    public Account findById(long id) {
        return accById.computeIfAbsent(id, (k) -> new Account(k, 100));
    }
}
