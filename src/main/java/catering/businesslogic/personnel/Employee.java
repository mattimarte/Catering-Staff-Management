package catering.businesslogic.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.sql.ResultSet;
import java.sql.SQLException;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

public class Employee {
    private int id;
    private String name;
    private String address;
    private String fiscalCode;
    private String contact;
    private int remainingVacDays;
    private List<Contract> contracts = new ArrayList<>();
    private List<VacationRequest> vacationRequests = new ArrayList<>();
    private List<Note> performanceNotes = new ArrayList<>();

    public Employee(String name, String contact){
        this.name = name;
        this.contact = contact;
        this.remainingVacDays = 20;
        this.contracts = new ArrayList<>();
        this.vacationRequests = new ArrayList<>();
        this.performanceNotes = new ArrayList<>();
    }

    private Employee(){}

    public void setId(int id){
        this.id = id;
    }

    public void save(){
        String query = "INSERT INTO Employees (name, address, fiscalCode, contact, remainingVacDays) VALUES (?, ?, ?, ?, ?)";
        PersistenceManager.executeUpdate(query, this.name, this.address, this.fiscalCode, this.contact, this.remainingVacDays);
        this.id = PersistenceManager.getLastId();
    }

    public void update(){
        String query = "UPDATE Employees SET name = ?, address = ?, fiscalCode = ?, contact = ?, remainingVacDays = ? WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.name, this.address, this.fiscalCode, this.contact, this.remainingVacDays, id);
    }

    public void delete(){
        String query = "DELETE FROM Employees WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.id);
    }

    public static Employee load(int employeeId){
        final Employee[] result = new Employee[1];
        String query = "SELECT * FROM Employees WHERE id = " + employeeId;

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Employee e = new Employee();
                e.id = rs.getInt("id");
                e.name = rs.getString("name");
                e.address = rs.getString("address");
                e.fiscalCode = rs.getString("fiscalCode");
                e.contact = rs.getString("contact");
                e.remainingVacDays = rs.getInt("remainingVacDays");
                e.contracts = Contract.loadAllForEmployee(e.id);
                //TODO: Caricare liste di contratti, note, ecc...
                result[0] = e;
            }
        }, employeeId);

        return result[0];
    }

    public void addContract(Contract c){
        if(c != null && !contracts.contains(c)){
            contracts.add(c);
        }
    }

    public void addVacationRequest(VacationRequest vr){
        if(vr != null && !vacationRequests.contains(vr)){
            vacationRequests.add(vr);
        }
    }

    public void addPerformanceNote(Note note){
        if(note != null && !performanceNotes.contains(note)){
            performanceNotes.add(note);
        }
    }

    public boolean isAssigned(){
        // TODO Da capire
        return false;
    }

    public void updateRemainingVacDays(Date startDate, Date endDate){
        // Compute the difference in ms
        long diffMS = Math.abs(endDate.getTime() - startDate.getTime());
        // Convert to Days plus one for the first day
        long diffDAYS = TimeUnit.DAYS.convert(diffMS, TimeUnit.MILLISECONDS) + 1;
        this.remainingVacDays = Math.max(0, this.remainingVacDays - (int) diffDAYS);
    }

    public void updateData(String name, String address, String fiscalCode, String contact){
        if(name != null && !name.isEmpty()) this.name = name;
        if(address != null && !address.isEmpty()) this.address = address;
        if(fiscalCode != null && !fiscalCode.isEmpty()) this.fiscalCode = fiscalCode;
        if(contact != null && !contact.isEmpty()) this.contact = contact;
    }

    // Getters methods
    public int getId() {return id;}
    public String getName() {return name;}
    public String getAddress() {return address;}
    public String getFiscalCode() {return fiscalCode;}
    public String getContact() {return contact;}
    public int getRemainingVacDays() {return remainingVacDays;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        if (id > 0 && employee.id > 0) {
            return id == employee.id;
        }
        return Objects.equals(fiscalCode, employee.fiscalCode); // Il codice fiscale è un buon identificatore univoco
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hash(id);
        }
        return Objects.hash(fiscalCode);
    }
}
