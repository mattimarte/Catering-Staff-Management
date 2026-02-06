package catering.businesslogic.personnel;

import catering.businesslogic.shift.Shift;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoleAssignment {
    private int id;
    private Employee employee;
    private Role role;
    private Shift shift;

    public RoleAssignment() {}

    public RoleAssignment(Employee employee, Role role) {
        this.employee = employee;
        this.role = role;
    }

    public RoleAssignment(Employee employee, Role role, Shift shift) {
        this.employee = employee;
        this.role = role;
        this.shift = shift;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(int shiftBoardId){
        String query = "INSERT INTO RoleAssignments (shiftboard_id, employee_id, role_id, shift_id) VALUES (?, ?, ?, ?)";
        Integer shiftId = (this.shift != null) ? this.shift.getId() : null;
        PersistenceManager.executeUpdate(query, shiftBoardId, this.employee.getId(), this.role.getId(), shiftId);
        this.id = PersistenceManager.getLastId();
    }

    public void update(){
        String query = "UPDATE RoleAssignments SET employee_id = ?, role_id = ?, shift_id = ? WHERE id = ?";
        Integer shiftId = (this.shift != null) ? this.shift.getId() : null;
        PersistenceManager.executeUpdate(query, this.employee.getId(), this.role.getId(), shiftId);
    }

    public void delete(){
        String query = "DELETE FROM RoleAssignments WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.id);
    }

    public static List<RoleAssignment> loadAllForShiftBoard(int shiftBoardId){
        final List<RoleAssignment> result = new ArrayList<>();
        String query = "SELECT * FROM RoleAssignments WHERE shiftboard_id = " + shiftBoardId;

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                RoleAssignment assignment = new RoleAssignment();
                assignment.id = rs.getInt("id");

                assignment.employee = Employee.load(rs.getInt("employee_id"));
                assignment.role = Role.load(rs.getInt("role_id"));

                int shiftId = rs.getInt("shift_id");
                if(!rs.wasNull()){
                    assignment.shift = Shift.loadItemById(shiftId);
                } else {
                    assignment.shift = null;
                }
                result.add(assignment);
            }
        });
        return result;
    }

    // Getters
    public int getId() { return id; }
    public Employee getEmployee() { return employee; }
    public Role getRole() { return role; }
    public Shift getShift() { return shift; }

    // Setters potrebbero essere utili se vogliamo permettere di modificare un'assegnazione
    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleAssignment that = (RoleAssignment) o;
        if (id > 0 && that.id > 0) {
            return id == that.id;
        }
        // Due assegnazioni sono uguali se riguardano lo stesso impiegato nello stesso turno
        return Objects.equals(employee, that.employee) &&
                Objects.equals(shift, that.shift);
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hash(id);
        }
        return Objects.hash(employee, shift);
    }
}
