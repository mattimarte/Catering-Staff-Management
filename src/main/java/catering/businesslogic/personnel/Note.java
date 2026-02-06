package catering.businesslogic.personnel;

import catering.businesslogic.event.Event;
import catering.businesslogic.user.User;
import catering.persistence.PersistenceManager;

import java.util.Date;
import java.util.Objects;

public class Note {
    private int id;
    private String noteText;
    private Date noteDate;
    private User writer;
    private Event event;
    private Employee employee;

    public Note() {}

    public Note(String noteText, Date noteDate, User writer, Event event, Employee employee) {
        this.noteText = noteText;
        this.noteDate = noteDate;
        this.writer = writer;
        this.event = event;
        this.employee = employee;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        String query = "INSERT INTO Notes (note_text, note_date, writer_id, event_id, employee_id) VALUES (?, ?, ?, ?, ?)";
        PersistenceManager.executeUpdate(query, noteText, noteDate, writer.getId(), event.getId(), employee.getId());
        this.id = PersistenceManager.getLastId();
    }

    // Getters methods
    public int getId() {return id;}
    public String getNoteText() {return noteText;}
    public Date getNoteDate() {return noteDate;}
    public User getWriter() {return writer;}
    public Event getEvent() {return event;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id && id > 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
