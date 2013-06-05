package com.vpundit.www.tests.integration

import static org.junit.Assert.*
import org.junit.*

import com.vpundit.www.User
import com.vpundit.www.UserService;

class UserServiceIntegrationTests {

	
	UserService userService
	
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testPersistence() {
		
		assert userService.getUser("varun") == null
		
		userService.updateUser("varun", "tata", new User(username: "varun", passwd: "hello"))
		
		assert userService.getUser("varun") == null	
		
		userService.updateUser(null, null, new User(username: "varun", passwd: "tata"))
		
		assert userService.getUser("varun").passwd == "tata"
		
		assert userService.updateUser(null, null, new User(username: "varun", passwd: "hello")) == false		
		
		assert userService.getUser("varun").passwd == "tata"
		
		userService.updateUser("varun", "tata", new User(username: "varun", passwd: "hello"))
		
		assert userService.getUser("varun").passwd == "hello"
		
		assert userService.login("varun", "tata") == false
		
		assert userService.login("varun", "hello") == true
			
    }
}
