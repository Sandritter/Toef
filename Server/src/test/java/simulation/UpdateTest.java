package simulation;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;

import org.junit.Test;

import simulation.control.Simulation;

public class UpdateTest {
	
	private volatile int called = 1;

	@Test
	public void test(){
		final Simulation simulation = new Simulation();
		simulation.start();
		
		int x = 1;
		double z = 100;
		while (z-- != 0)  {
			int i = simulation.enqueue(new Callable<Integer>(){
				public Integer call() throws Exception {
					called += 1;
					return called;
				}			
			});			
			x++;
			assertEquals(i, x);
		}	
	}
}
