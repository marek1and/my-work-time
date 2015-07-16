package pl.marek1and.myworktime.db.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkTime {

    public enum Type {NORMAL, DAYOFF}

    private long id;
    private Date startTime;
    private Date endTime;
    private Type type;
    private List<Note> notes;
    private List<Transport> transports;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Note> getNotes() {
        if(notes == null) {
            notes = new ArrayList<Note>();
        }
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void addNote(Note note) {
        boolean added = false;
        for (Note n: getNotes()) {
            if(n.equals(note)) {
               added = true;
            }
        }
        if(!added && note != null) {
            notes.add(note);
        }
    }

    public List<Transport> getTransports() {
        if(transports == null) {
            transports = new ArrayList<Transport>();
        }
        return transports;
    }

    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }

    public void addTransport(Transport transport) {
        boolean added = false;
        for(Transport t: getTransports()) {
            if(t.equals(transport)) {
                added = true;
            }
        }
        if (!added && transport != null) {
            transports.add(transport);
        }
    }

    public List<TransportMapping> getTransportMapping() {
        List<TransportMapping> mappings = new ArrayList<TransportMapping>();
        for(Transport t: getTransports()) {
            TransportMapping tm = new TransportMapping(getId(), t.getId());
            mappings.add(tm);
        }
        return mappings;
    }

    @Override
    public int hashCode() {
        return 53 * getId().intValue();
    }

    @Override
    public boolean equals(Object o) {
        WorkTime w = (WorkTime)o;
        return getId().equals(w.getId());
    }

}
