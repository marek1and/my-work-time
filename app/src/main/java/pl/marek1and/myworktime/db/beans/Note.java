package pl.marek1and.myworktime.db.beans;

import java.util.Date;

public class Note {

    private long id;
    private String title;
    private String note;
    private Date modifyTime;
    private WorkTime workTime;

    public Note() {
        modifyTime = new Date();
    }

    public Note(String note) {
        this();
        this.note = note;
    }

    public Note(String title, String note) {
        this(note);
        this.title = title;
    }

    public Note(long id, String title, String note) {
        this(title, note);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public WorkTime getWorkTime() {
        return workTime;
    }

    public void setWorkTime(WorkTime workTime) {
        this.workTime = workTime;
    }

    @Override
    public int hashCode() {
        return 31 * getId().intValue();
    }

    @Override
    public boolean equals(Object o) {
        Note n = (Note)o;
        return getId().equals(n.getId());
    }
}
