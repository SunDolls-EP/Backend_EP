package com.sundolls.epbackend.integraion;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends BaseIntegrationTest{

    @Test
    public void findUser() throws Exception {
        String username = "Test";
        String tag = "0001";

        ResultActions resultActions = mvc.perform(get("/api/user/"+username))
                .andDo(print());

        resultActions.andDo(print());
    }

}
