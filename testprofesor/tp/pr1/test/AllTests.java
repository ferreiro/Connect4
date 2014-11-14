package tp.pr1.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tp.pr1.logic.test.*;

@RunWith(Suite.class) 
@Suite.SuiteClasses( { 
	BoardTest.class,
	GameTest.class,
	UndoTest.class,
	FourInARowTest.class
	})
public class AllTests {

}
