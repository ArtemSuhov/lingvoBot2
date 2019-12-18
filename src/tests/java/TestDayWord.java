import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;


public class TestDayWord {
    @Test
    public void testWrongArguments(){
        var base = FireBase.getInstance();
        var bot = new Bot(base);
        var user = new User("UserId","0 1");
        var respond = bot.getDayWord(new String[] {"test time"}, user);
        Assert.assertArrayEquals(new String[] {"Wrong arguments!!!"}, respond);
    }

    @Test
    public void testSetTimeDayWord(){
        var base = FireBase.getInstance();
        var bot = new Bot(base);
        var user = new User("UserId","0 1");
        var respond = bot.getDayWord(new String[] {"22:22"}, user);
        Assert.assertArrayEquals(new String[] {"Set time for the word of the day"}, respond);
        Assert.assertEquals(LocalTime.of(22,22),user.timeOfDay);
        Assert.assertEquals(false, user.isSentWord);
    }

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(output));
    }
    @Test
    public void testGenerateVariant(){
        var base = FireBase.getInstance();
        var io = new ConsoleIO();
        var user = new User("UserId", "0 1", UserState.DEFAULT, LocalTime.of(22,22),
                false, false);
        var dayWord = new DayWord(base, io);
        dayWord.beginGame(user);
        var variants = output.toString();
        Assert.assertTrue(variants.contains(dayWord.answer));
        Assert.assertEquals(true, user.isSentWord);
    }

    //@Test
    //не работает из за ошибки с потоками
    //public void testCheckUserTime(){
    //    var base = FireBase.getInstance();
    //    var io = new ConsoleIO();
    //    var user = new User("UserId", "0 1", UserState.DEFAULT, LocalTime.now(),
    //            false, false);
    //    base.updateUser(user);
    //    var dayWord = new DayWord(base, io);
    //    var reminder = new Reminder(base, io, dayWord);
    //    reminder.updateAllUsers();
    //    reminder.checkUsersTime();
    //    Assert.assertEquals(true, user.isSentWord);
    //    var userAfter = new User("UserId", "0 1", UserState.DEFAULT, LocalTime.now().plusHours(1),
    //            false, false);
    //    base.updateUser(userAfter);
    //    reminder.updateAllUsers();
    //    Assert.assertEquals(false, userAfter.isSentWord);
    //}

}
