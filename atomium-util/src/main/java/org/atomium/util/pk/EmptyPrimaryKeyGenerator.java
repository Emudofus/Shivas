package org.atomium.util.pk;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/09/12
 * Time: 18:48
 */
public class EmptyPrimaryKeyGenerator<PK> implements PrimaryKeyGenerator<PK> {
    @Override
    public void setMax(PK pk) { }

    @Override
    public PK next() {
        return null;
    }
}
