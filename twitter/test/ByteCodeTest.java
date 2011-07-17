import junit.framework.Assert;
import models.User;

import org.junit.Test;

import play.test.UnitTest;


public class ByteCodeTest extends UnitTest {

	@Test
	public void byteCodeTest(){
		User user = new User("user1", "user1");
		System.out.println(user.password);
		Assert.assertEquals("user1", user.password);
	}
	
}
