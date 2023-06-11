package com.example.deliveryapp.security.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {

    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;

    private LoadingCache<String, Integer> loginAttemptCache; //un fel de localStorage pe backend=> care tine minte de cate ori a incercat logarea

    public LoginAttemptService(){
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)//pot gresi de maxim 5 ori in 15 minute
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key){
                        return 0;
                    }
                });
    }

    public void evictUserFromLoginAttemptCache(String username){
        loginAttemptCache.invalidate(username);// daca s-a autentificat cu succes => anulez cacheul pentru ca nu mai este nevoie de el
    }

    public void addUserToLoginAttemptCache(String username) {
        int attempts = 0;
        try{
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        loginAttemptCache.put(username, attempts);
    }

    public boolean hasExceededMaxAttempts(String username){
        try{
            return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return false;
    }

}
