import models.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;


public class DaoTest extends UnitTest {

	@Before
	public void setup(){
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data.yml");
	}
	
	@Test
	public void daoTest(){		
		long numeroUsuarios = User.count();
		Assert.assertEquals(2, numeroUsuarios);
		
		
		User findUser1 = User.find("byUsername", "user1").first();
		Assert.assertNotNull(findUser1);
		Assert.assertEquals("user1", findUser1.password);
	}
	
}
