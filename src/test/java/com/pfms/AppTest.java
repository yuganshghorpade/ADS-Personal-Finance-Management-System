// package com.pfms;

// import static org.junit.Assert.assertTrue;

// import org.junit.Test;

// /**
//  * Unit test for simple App.
//  */
// public class AppTest 
// {
//     /**
//      * Rigorous Test :-)
//      */
//     @Test
//     public void shouldAnswerWithTrue()
//     {
//         assertTrue( true );
//     }
// }

package com.pfms;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    
    @Test
    public void testUserAuthentication() {
        User user = new User("john_doe", "password123");
        assertTrue(user.authenticate("password123"));
        assertFalse(user.authenticate("wrong_password"));
    }

    @Test
    public void testBudgetManager() {
        BudgetManager budgetManager = new BudgetManager();
        budgetManager.addCategory("Food", 500.0);
        budgetManager.updateCategory("Food", 100.0);
        assertEquals(400.0, budgetManager.getRemainingBudget("Food"), 0.001);
    }

    // Add more tests for your other modules
}

