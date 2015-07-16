package pl.marek1and.myworktime.db.beans;

public class TransportMapping {

    private long workTimeId;
    private long transportId;

    public TransportMapping() {

    }

    public TransportMapping(long workTimeId, long transportId) {
        this.workTimeId = workTimeId;
        this.transportId = transportId;
    }

    public long getWorkTimeId() {
        return workTimeId;
    }

    public void setWorkTimeId(long workTimeId) {
        this.workTimeId = workTimeId;
    }

    public long getTransportId() {
        return transportId;
    }

    public void setTransportId(long transportId) {
        this.transportId = transportId;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + (int)workTimeId;
        hash = hash * 53 + (int)transportId;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        TransportMapping t = (TransportMapping)o;
        return getWorkTimeId() == t.getWorkTimeId()
            && getTransportId() == t.getTransportId();
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", workTimeId, transportId);
    }
}
