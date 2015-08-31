package br.ufrgs.MASBench;

import java.util.Objects;

/**
 * NodeID of the FGMD
 * @author Abel Correa {@literal (abel.correa @ inf.ufrgs.br)}
 */
public final class NodeID {
	
	public final Entity instance;
	public final Entity feature;
	public final Integer dimension;
	
	public NodeID(Entity instance, Entity feature, Integer dimension)
	{
		this.instance = instance;
		this.feature = feature;
		this.dimension = dimension;
	}
	
	@Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.instance);
        hash = 59 * hash + Objects.hashCode(this.feature);
        hash = 59 * hash + Objects.hashCode(this.dimension);
        return hash;
    }
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodeID other = (NodeID) obj;
        if (!Objects.equals(this.instance, other.instance)) {
            return false;
        }
        if (!Objects.equals(this.feature, other.feature)) {
            return false;
        }
        if( !Objects.equals(this.dimension, other.dimension))
        {
        	return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "<" + instance + "," + feature + "," + dimension + ">";
    }

}
