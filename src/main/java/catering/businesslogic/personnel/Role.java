package catering.businesslogic.personnel;

import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Role {
    private int id;
    private String role;

    public Role(){}

    public Role(String role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        String query = "INSERT INTO Roles (role) VALUES (?)";
        PersistenceManager.executeUpdate(query, this.role);
        this.id = PersistenceManager.getLastId();
    }

    public void update(){
        String query = "UPDATE Roles SET role = ? WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.role, this.id);
    }

    public void delete(){
        String query = "DELETE FROM Roles WHERE id = ?";
        PersistenceManager.executeUpdate(query, this.id);
    }

    public static Role load(int roleId){
        final Role[] result = new Role[1];
        String query = "SELECT * FROM Roles WHERE id = " + roleId;

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                result[0] = new Role();
                result[0].setId(rs.getInt("id"));
                result[0].role = rs.getString("role");
            }
        });

        return result[0];
    }

    public static List<Role> loadAll(){
        final List<Role> results = new ArrayList<>();
        String query = "SELECT * FROM Roles ORDER BY role";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.role = rs.getString("role");
                results.add(role);
            }
        });

        return results;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        if (id > 0 && role1.id > 0)
            return id == role1.id;
        return role.equals(role1.role);
    }

    @Override
    public int hashCode() {
        if (id > 0)
            return Integer.hashCode(id);
        return role.hashCode();
    }
}
