package ir.atitec.signalgo.models;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;

/**
 * Created by hamed on 2/1/2018.
 */

public  class JSOGGenerator extends ObjectIdGenerator<JSOGRef>  {
    public static final String REF_KEY = "@ref";

    private static final long serialVersionUID = 1L;
    protected transient int _nextValue;
    protected final Class<?> _scope;

    protected JSOGGenerator() { this(null, -1); }

    protected JSOGGenerator(Class<?> scope, int nextValue) {
        _scope = scope;
        _nextValue = nextValue;
    }

    @Override
    public Class<?> getScope() {
        return _scope;
    }

    @Override
    public boolean canUseFor(ObjectIdGenerator<?> gen) {
        return (gen.getClass() == getClass()) && (gen.getScope() == _scope);
    }

    @Override
    public ObjectIdGenerator<JSOGRef> forScope(Class<?> scope) {
        return (_scope == scope) ? this : new JSOGGenerator(scope, _nextValue);
    }

    @Override
    public ObjectIdGenerator<JSOGRef> newForSerialization(Object context) {
        return new JSOGGenerator(_scope, 1);
    }

    @Override
    public com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey key(Object key) {
        return new IdKey(getClass(), _scope, key);
    }

    // important: otherwise won't get proper handling
    @Override
    public boolean maySerializeAsObject() { return true; }

    // ditto: needed for handling Object-valued Object references
    @Override
    public boolean isValidReferencePropertyName(String name, Object parser) {
        return REF_KEY.equals(name);
    }

    @Override
    public JSOGRef generateId(Object forPojo) {
        int id = _nextValue;
        ++_nextValue;
        return new JSOGRef(id);
    }
}