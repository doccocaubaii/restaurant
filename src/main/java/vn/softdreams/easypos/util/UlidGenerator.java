package vn.softdreams.easypos.util;

import com.github.f4b6a3.ulid.UlidCreator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.xnio.Option;
import org.xnio.channels.Configurable;

import java.io.IOException;
import java.io.Serializable;

public class UlidGenerator implements IdentifierGenerator, Configurable {

    public static final String SEQUENCE_PREFIX = "Ulid_prefix";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return UlidCreator.getMonotonicUlid().toString();
    }

    @Override
    public boolean supportsOption(Option<?> option) {
        return false;
    }

    @Override
    public <T> T getOption(Option<T> option) throws IOException {
        return null;
    }

    @Override
    public <T> T setOption(Option<T> option, T t) throws IllegalArgumentException, IOException {
        return null;
    }
}
