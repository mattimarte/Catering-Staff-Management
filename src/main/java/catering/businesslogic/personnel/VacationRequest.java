package catering.businesslogic.personnel;

import catering.persistence.PersistenceManager;

import java.util.Date;
import java.util.Objects;

public class VacationRequest {
    private int id;
    private Date startDate;
    private Date endDate;
    private Date requestDate;
    private boolean approved;
    private Employee employee;

    private VacationRequest() {}

    public VacationRequest(Date startDate, Date endDate, Employee employee) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.employee = employee;
        this.requestDate = new Date();
        this.approved = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        String query = "INSERT INTO VacationRequests (employee_id, start_date, end_date, request_date, is_approved) VALUES (?, ?, ?, ?, ?)";
        PersistenceManager.executeUpdate(query, employee.getId(), startDate, endDate, requestDate, approved);
        this.id = PersistenceManager.getLastId();
    }

    public void update(){
        String query = "UPDATE VacationRequests SET employee_id = ?, start_date = ?, end_date = ?, request_date = ?, is_approved = ? WHERE id = ?";
        PersistenceManager.executeUpdate(query, employee.getId(), startDate, endDate, requestDate, approved, id);
    }

    public void delete(){
        String query = "DELETE FROM VacationRequests WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.id);
    }

    public void approve(){
        this.approved = true;
    }

    public void reject(){
        this.approved = false;
    }

    // Getters methods
    public int getId() {return id;}
    public Employee getEmployee() {return employee;}
    public Date getStartDate() {return startDate;}
    public Date getEndDate() {return endDate;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacationRequest that = (VacationRequest) o;
        return id == that.id && id > 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
