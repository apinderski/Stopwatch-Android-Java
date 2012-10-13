package edu.luc.etl.cs313.android.simplestopwatch.model.time;

import org.junit.After;
import org.junit.Before;

/**
 * Concrete testcase subclass for the default time model implementation.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html
 */
public class DefaultTimeModelTest extends AbstractTimeModelTest {

	@Before
	public void setUp() throws Exception {
		setModel(new DefaultTimeModel());
	}

	@After
	public void tearDown() throws Exception {
		setModel(null);
	}
}
