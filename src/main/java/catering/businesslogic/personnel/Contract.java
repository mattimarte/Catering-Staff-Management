package catering.businesslogic.personnel;

import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;
import catering.util.DateUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Contract {
    private int id;
    private int employeeId;
    private boolean isPermanent;
    private Date startDate;
    private Date endDate;

    public Contract(boolean isPermanent, Date startDate) {
        this.isPermanent = isPermanent;
        this.startDate = startDate;
    }

    public Contract(boolean isPermanent, Date startDate, Date endDate) {
        this.isPermanent = isPermanent;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private Contract(){}

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        String query = "INSERT INTO Contracts (employee_id, is_permanent, start_date, end_date) VALUES (?, ?, ?, ?)";
        PersistenceManager.executeUpdate(query, this.employeeId, this.isPermanent, this.startDate, this.endDate);
        this.id = PersistenceManager.getLastId();
    }

    public void update(){
        String query = "UPDATE Contracts SET is_permanent = ?, start_date = ?, end_date = ? WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.isPermanent, this.startDate, this.endDate, this.id);
    }

    public void delete(){
        String query = "DELETE FROM Contracts WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.id);
    }

    public static List<Contract> loadAllForEmployee(int employeeId){
        final List<Contract> results = new ArrayList<>();
        String query = "SELECT * FROM Contracts WHERE employee_id = " + employeeId + " ORDER BY start_date DESC";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Contract contract = new Contract();
                contract.setId(rs.getInt("id"));
                contract.isPermanent = rs.getBoolean("is_permanent");

                contract.startDate = DateUtils.getDateFromResultSet(rs, "start_date");
                contract.startDate = DateUtils.getDateFromResultSet(rs, "end_date");

                results.add(contract);
            }
        });
        return results;
    }

    // Getters methods
    public boolean isPermanent() {
        return isPermanent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        if(endDate != null)
            return endDate;
        else return null;
    }
}
