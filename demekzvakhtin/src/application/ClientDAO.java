package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Database;

public class ClientDAO {    
	public boolean userExists(String login) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE login_user = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

	public void registerUser(Client client) throws SQLException {
	    if (userExists(client.getLogin())) {
	        throw new SQLException("User already exists", "23000", 1062); // Use SQLState and vendorCode for duplicate entry
	    }
	    String query = "INSERT INTO users (fio, phone, login_user, password_user, type_manager) VALUES (?, ?, ?, ?, ?)";
	    try (Connection conn = Database.getConnection(); 
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, client.getFio());
	        stmt.setString(2, client.getPhone());
	        stmt.setString(3, client.getLogin());
	        stmt.setString(4, client.getPassword());
	        stmt.setString(5, "user"); // Default role for new users
	        stmt.executeUpdate();
	    }
	}
	
    public void addRequest(Request request) throws SQLException {
        String query = "INSERT \r\n"
        		+ "    INTO zayavki (requestID, startDate, homeTechType, problemDescryption, requestStatus, clientID) \r\n"
        		+ "    SELECT ?, ?, ?, ?, ?, u.userID\r\n"
        		+ "        FROM users u \r\n"
        		+ "    WHERE u.userID= ?\r\n"
        		+ "";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, request.getRequestID());
            stmt.setDate(2, request.getStartDate());
            stmt.setString(3, request.getHomeTechType());
            stmt.setString(4, request.getProblemDescryption());
            stmt.setString(5, request.getRequestStatus());
            stmt.setString(6, request.getClientID());
            stmt.executeUpdate();
        }
    }
    
    public void deleteData(int id) throws SQLException {
        String query = "DELETE FROM zayavki WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<Request> getAllRepairRequests() throws SQLException {
        List<Request> requests = new ArrayList<>();
        String query = "SELECT z.id, z.requestID, z.startDate, z.homeTechType, z.problemDescryption, " +
                "z.requestStatus, z.completionDate, z.repairParts, z.masterID, z.clientID " +
                "FROM zayavki z";
        try (Connection conn = Database.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while(rs.next()) {
                Request request = new Request();
                request.setRequestID(rs.getInt("id"));
                request.setStartDate(rs.getDate("startDate"));
                request.setHomeTechType(rs.getString("homeTechType"));
                request.setProblemDescryption(rs.getString("problemDescryption"));
                request.setRequestStatus(rs.getString("requestStatus"));
                requests.add(request);
            }
        }
        return requests;
    }


    // Метод для редактирования существующей заявки в базе данных
    public void editRequest(Request request) throws SQLException {
        String query = "UPDATE zayavki SET requestStatus = ?, problemDescryption = ?, masterID = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, request.getRequestStatus());
            stmt.setString(2, request.getProblemDescryption());
            stmt.setInt(3, request.getMasterID());
            stmt.setInt(4, request.getId());
            stmt.executeUpdate();
        }
    }


    
    public boolean authenticateUser(Client client) throws SQLException {
        String query = "SELECT * FROM users WHERE login_user = ? AND password_user = ?";
        try (Connection conn = Database.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getLogin());
            stmt.setString(2, client.getPassword());  
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  
            }
        }
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients";
        try (Connection conn = Database.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setLogin(query);
                client.setPassword(rs.getString("password"));
                clients.add(client);
            }
        }
        return clients;
    }

}
