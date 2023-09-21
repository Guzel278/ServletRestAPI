package x5.servlet.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import x5.servlet.model.User;

public class UserService {
    private final Map<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong userIdSequence = new AtomicLong();
    public User getUserById(Long id) {return userStore.get(id);}

    public User createNewUser(User user){
        user.setId(userIdSequence.getAndIncrement());
        userStore.put(user.getId(), user);
        return user;
    }
    public boolean deleteUser(Long id) throws Exception{
        User user = getUserById(id);
        if(user == null){
           throw new Exception("user not found");
        }
        return userStore.remove(id,user) ? true : false;
    }
    public User updateUser(Long id, User newUser){
        return userStore.replace(id, newUser);
    }
}
