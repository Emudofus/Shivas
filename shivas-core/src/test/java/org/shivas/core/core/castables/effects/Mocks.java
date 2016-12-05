package org.shivas.core.core.castables.effects;

import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public final class Mocks {
    private final List<Object> mocks;

    private InOrder inOrder;

    public Mocks() {
        this.mocks = new ArrayList<>();
    }

    private InOrder getInOrder() {
        if (inOrder == null) {
            inOrder = Mockito.inOrder(mocks.toArray());
        }
        return inOrder;
    }

    /**
     * Creates mock object of given class or interface.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param klass class or interface to mock
     * @return mock object
     */
    public <T> T mock(Class<T> klass) {
        T mock = Mockito.mock(klass);
        mocks.add(mock);
        return mock;
    }

    /**
     * Verifies interaction <b>happened once</b> in order.
     * <p>
     * Alias to <code>inOrder.verify(mock, times(1))</code>
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     * InOrder inOrder = inOrder(firstMock, secondMock);
     *
     * inOrder.verify(firstMock).someMethod("was called first");
     * inOrder.verify(secondMock).someMethod("was called second");
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param mock to be verified
     *
     * @return mock object itself
     */
    public <T> T verify(T mock) {
        return getInOrder().verify(mock);
    }

    /**
     * Verifies interaction in order. E.g:
     *
     * <pre class="code"><code class="java">
     * InOrder inOrder = inOrder(firstMock, secondMock);
     *
     * inOrder.verify(firstMock, times(2)).someMethod("was called first two times");
     * inOrder.verify(secondMock, atLeastOnce()).someMethod("was called second at least once");
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param mock to be verified
     * @param mode for example times(x) or atLeastOnce()
     *
     * @return mock object itself
     */
    public <T> T verify(T mock, VerificationMode mode) {
        return getInOrder().verify(mock, mode);
    }
}
