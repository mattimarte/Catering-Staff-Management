package catering.businesslogic.personnel;

import catering.businesslogic.event.Service;
import catering.businesslogic.sheet.SummarySheet;
import catering.businesslogic.user.User;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShiftBoard extends SummarySheet {

    private List<RoleAssignment> assignments;

    public ShiftBoard() {
        super(null, null);
        this.assignments = new ArrayList<>();
    }

    public ShiftBoard(Service service, User user) {
        super(service, user);
        this.assignments = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        String query = "INSERT INTO ShiftBoards (service_id, owner_id) VALUES (?, ?)";
        PersistenceManager.executeUpdate(query, this.service.getId(), this.owner.getId());
        this.id = PersistenceManager.getLastId();

        for (RoleAssignment ra : assignments) {
            ra.save(this.id);
        }
    }

    public void update(){
        String query = "UPDATE ShiftBoards SET service_id = ?, owner_id = ? WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.service.getId(), this.owner.getId(), this.id);
    }

    public void delete(){
        String query = "DELETE FROM ShiftBoards WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.id);
    }

    public static ShiftBoard load(int boardId) {
        final ShiftBoard[] result = new ShiftBoard[1];
        String query = "SELECT * FROM ShiftBoards WHERE board_id = " + boardId;

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                ShiftBoard board = new ShiftBoard();
                board.id = rs.getInt("id");
                board.owner = User.load(rs.getInt("owner_id"));
                board.service = Service.loadById(rs.getInt("service_id"));
                board.assignments = RoleAssignment.loadAllForShiftBoard(board.id);

                result[0] = board;
            }
        });

        return result[0];
    }

    public void addAssignment(RoleAssignment assignment) {
        if(assignment != null && !assignments.contains(assignment)) {
            assignments.add(assignment);
        }
    }

    public void removeAssignment(RoleAssignment assignment) {
        assignments.remove(assignment);
    }

    public List<RoleAssignment> getAssignments() {
        return new ArrayList<>(assignments);
    }


}
