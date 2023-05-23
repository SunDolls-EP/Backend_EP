package com.sundolls.epbackend.service;

import com.sundolls.epbackend.dto.response.FriendResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

@SpringBootTest
public class FriendServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void makeFriendA(){
        UserResponse response = userService.requestFriend("test_b","friend_test_a");
        System.out.println("==========requestFriend=========");
        System.out.println(response.toString());
        System.out.println("==================================");
    }

    @Test
    public void makeFriendB(){
        UserResponse response = userService.requestFriend("test_c","friend_test_a");
        System.out.println("==========requestFriend=========");
        System.out.println(response.toString());
        System.out.println("==================================");
    }

    @Test
    public void getFriends(){
        ArrayList<FriendResponse> friends = userService.getFriendList("friend_test_a");
        System.out.println("==========getFriendList=========");
        System.out.println(friends.get(0).toString());
        System.out.println(friends.get(1).toString());
        System.out.println("==================================");
    }

    @Test
    public void deleteFriend(){
        FriendResponse friend = userService.deleteFriend("test_c","friend_test_a");
        System.out.println("==========deleteFriend=========");
        System.out.println(friend.toString());
        System.out.println("==================================");

    }

    @Test
    public void getFriend(){
        ArrayList<FriendResponse> friends = userService.getFriendList("friend_test_a");
        System.out.println("==========getFriendList=========");
        System.out.println(friends.get(0).toString());
        System.out.println("==================================");
    }

}
