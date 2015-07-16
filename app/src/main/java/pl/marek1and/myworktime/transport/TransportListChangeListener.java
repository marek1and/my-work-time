package pl.marek1and.myworktime.transport;

import java.util.Set;

import pl.marek1and.myworktime.db.beans.Transport;

public interface TransportListChangeListener {

    public void transportListChanged(Set<Transport> transports);

}
