package org.mvnsearch.rsocket.demo;

import reactor.util.context.Context;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Delegated Context
 *
 * @author linux_china
 */
public class DelegatedContext implements Context {
    private Context delegate;
    private Context parent;

    public DelegatedContext(Context delegate, Context parent) {
        this.delegate = delegate;
        this.parent = parent;
    }

    @Override
    public <T> T get(Object key) {
        if (delegate.hasKey(key)) {
            return delegate.get(key);
        } else {
            return parent.get(key);
        }
    }

    @Override
    public boolean hasKey(Object key) {
        return delegate.hasKey(key) || parent.hasKey(key);
    }

    @Override
    public Context put(Object key, Object value) {
        parent.put(key, value);
        return this;
    }

    @Override
    public Context delete(Object key) {
        return this;
    }

    @Override
    public int size() {
        return delegate.size() + parent.size();
    }

    @Override
    public Stream<Map.Entry<Object, Object>> stream() {
        return Stream.concat(delegate.stream(), parent.stream());
    }
}
