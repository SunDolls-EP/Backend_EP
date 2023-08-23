package com.sundolls.epbackend.config.util;

import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class TagMaker {

     public static String makeTag(String username, List<User> users) {
        Random random = new Random();
        String tag = null;

        Verifier: while(true) {
            tag = String.format("%04d", random.nextInt(10000));
            for (User user : users) {
                if (user.getTag().equals(tag)) {
                    continue Verifier;
                }
            }
            break;
        }
        return tag;
    }
}
