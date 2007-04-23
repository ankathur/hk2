package test1;

import org.jvnet.hk2.annotations.Service;

/**
 * @author Kohsuke Kawaguchi
 */
@Service(factory=TestFactory.class)
public class FactoryTest extends Test {
    private final int x;

    public FactoryTest(int x) {
        this.x = x;
    }

    public void run() {
        // make sure that the factory run.
        assertEquals(x,5);
    }
}
