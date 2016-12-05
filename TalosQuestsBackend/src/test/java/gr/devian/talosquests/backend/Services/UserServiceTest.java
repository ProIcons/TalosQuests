package gr.devian.talosquests.backend.Services;

import gr.devian.talosquests.backend.AbstractTest;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsNullSessionException;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.Session;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Utilities.SecurityTools;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Nikolas on 5/12/2016.
 */

@Transactional
public class UserServiceTest extends AbstractTest {

    @Autowired
    UserService userService;



    private User testUserWithSession;
    private Session testSession;
    private User testUserWithoutSession;

    private String userName;
    private String userNameWithSession;
    private String userNameWithoutSession;
    private String token;

    private SecureRandom random = new SecureRandom();

    @Before
    public void setUp() {

        userService.wipe();
        userService.evictCache();

        //Let userName = test_92jf923jf923jg923
        userName = "test_" + new BigInteger(130, random).toString(32);

        //Let userNameWithSession = test_92jf923jf923jg923_withSession
        userNameWithSession = userName + "_withSession";

        // AuthRegisterModel
        // -> Username: test_92jf923jf923jg923_withSession
        // -> Password: test
        // -> Email   : test_92jf923jf923jg923_wi@test.gr
        // -> IMEI    : test



        testUserWithSession = userService.createUser(
                new AuthRegisterModel(
                        userNameWithSession,
                        "test",
                        userName + "_wi@test.gr",
                        "test"));


        //Let userNameWithSession = test_92jf923jf923jg923_withoutSession
        userNameWithoutSession = userName + "_withoutSession";

        // AuthRegisterModel
        // -> Username: test_92jf923jf923jg923_withoutSesison
        // -> Password: test
        // -> Email   : test_92jf923jf923jg923_wo@test.gr
        // -> IMEI    : test

        testUserWithoutSession = userService.createUser(
                new AuthRegisterModel(
                        userName + "_withoutSession",
                        "test",
                        userName + "_wo@test.gr",
                        "test"));


        testSession = userService.createSession(testUserWithSession);

    }

    @Test
    public void testFindAllNotNull() {

        Collection<User> list = userService.findAllUsers();

        assertNotNull("Failure - Expected not null", list);

        assertThat("Failure - Expected List Size Greater than 0", list.size(), greaterThan(0));

    }

    @Test
    public void testFindAllEmpty() {
        userService.wipe();
        Collection<User> list = userService.findAllUsers();

        assertTrue("Failure - Expected list to be empty",list.isEmpty());
    }


    @Test
    public void testFindByIdWhenExists() {
        User user = userService.getUserById(testUserWithSession.getId());

        assertNotNull("Failure - Expected not null", user);

        assertEquals("Failure - Expected user.Username = testUserWithSession.username", user.getUserName(), testUserWithSession.getUserName());

        assertNotNull("Failure - ID cannot be null", user.getId());
    }



    @Test
    public void testFindByIdWhenNotExists() {
        User user = userService.getUserById(Long.MAX_VALUE);

        assertNull("Failure - Expected null", user);

    }

    @Test
    public void testFindByUsernameWhenExists() {
        User user = userService.getUserByUsername(userNameWithSession);

        assertNotNull("Failure - Expected not null", user);

        assertEquals("Failure - Expected user.Username = testUserWithSession.username", user.getUserName(), testUserWithSession.getUserName());

        assertNotNull("Failure - ID cannot be null", user.getId());
    }

    @Test
    public void testFindByUsernameWhenNotExists() {
        String temp = SecurityTools.MD5(new BigInteger(130, random).toString(32));
        User user = userService.getUserByUsername(temp);

        assertNull("Failure - Expected null", user);

    }

    @Test
    public void testGetSessionByUserWhenUserIsNull() {
        Session session = userService.getSessionByUser(null);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByUserWhenUserDoesntHaveSession() {
        Session session = userService.getSessionByUser(testUserWithoutSession);
        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByUserWhenUserHasSession() {
        Session session = userService.getSessionByUser(testUserWithSession);
        assertNotNull("Failure - Expected not null", session);

        assertEquals("Failure - Session Ids not Equals", session.getSessionId(),testSession.getSessionId());

    }

    @Test
    public void testGetSessionByUserWhenExpired() {
        userService.expireSession(testSession);
        Session session = userService.getSessionByUser(testUserWithSession);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsNull() {
        Session session = userService.getSessionByToken(null);

        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsNotValid() {
        Session session = userService.getSessionByToken("testToken");
        assertNull("Failure - Expected null", session);
    }

    @Test
    public void testGetSessionByTokenWhenTokenIsValid() {
        Session session = userService.getSessionByToken(testSession.getToken());
        assertNotNull("Failure - Expected Not null", session);
    }

    @Test(expected = TalosQuestsNullSessionException.class)
    public void testRemoveSessionWhenNullGiven() throws TalosQuestsNullSessionException {
        userService.removeSession(null);
    }

    @Test
    public void testGetUserByEmailWhenEmailIsValid() {
        String email = testUserWithSession.getEmail();
        User user = userService.getUserByEmail(email);
        assertNotNull("Failure - User cannot be null", user);
    }

    @Test
    public void testGetUserByEmailWhenEmailIsNotValid() {
        String email = "asdagadh";
        User user = userService.getUserByEmail(email);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByEmailWhenEmailIsNull() {
        User user = userService.getUserByEmail(null);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByUsenameWhenUsernameIsNull() {
        User user = userService.getUserByUsername(null);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByIdWhenIdIsNull() {
        User user = userService.getUserById(null);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testGetUserByIdWhenIdIsNegative() {
        User user = userService.getUserById((long)-7);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testCreateUserOnInsufficientUserData() {
        AuthRegisterModel model = new AuthRegisterModel();
        User user = userService.createUser(model);
        assertNull("Failure - Expected null", user);
    }

    /*@Test
    public void testRemoveUserWhenUserIsNull() {
        userService.removeUser(null);
        verify(userService,times(1)).removeUser(null);
    }*/

    @Test
    public void testUpdateUserOnNullModel() {
        AuthRegisterModel model = null;
        User user = userService.updateUser(testUserWithSession,model);
        assertNull("Failure - Expected null", user);
    }

    @Test
    public void testUpdateUserImei() {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setImei("test15");

        User user = userService.updateUser(testUserWithSession,model);

        assertEquals("Imei didn't changed",user.getDeviceIMEI(),"test15");
    }

    @Test
    public void testUpdateUserEmail() {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setEmail("test15");

        User user = userService.updateUser(testUserWithSession,model);

        assertEquals("Imei didn't changed",user.getEmail(),"test15");
    }

    @Test
    public void testUpdateUserPassword() {
        AuthRegisterModel model = new AuthRegisterModel();
        model.setPassWord("test15");

        User user = userService.updateUser(testUserWithSession,model);

        assertEquals("Imei didn't changed",user.getPassWord(),"test15");
    }

}
