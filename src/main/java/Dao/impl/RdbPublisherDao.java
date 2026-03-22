package Dao.impl;

import Dao.PublisherDao;
import Models.PublisherModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RdbPublisherDao implements PublisherDao {
	private final DataSource dataSource;

	public RdbPublisherDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<PublisherModel> getAllPublishers() {
		List<PublisherModel> list = new ArrayList<>();
		String sql = "SELECT * FROM Publisher";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				PublisherModel p = new PublisherModel();
				p.publisherId = rs.getInt("PublisherID");
				p.publisherName = rs.getString("PublisherName");
				list.add(p);
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public int insertPublisher(PublisherModel publisher) {
		String sql = "INSERT INTO Publisher (PublisherName, Address) VALUES (?, ?)";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, publisher.publisherName);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) return rs.getInt(1);
		} catch (SQLException e) { e.printStackTrace(); }
		return -1;
	}

	@Override
	public PublisherModel getPublisherById(int id) {
		String sql = "SELECT * FROM Publishers WHERE PublisherID = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					PublisherModel p = new PublisherModel();
					p.publisherId = rs.getInt("PublisherID");
					p.publisherName = rs.getString("PublisherName");
					// Đảm bảo trong PublisherModel của bạn có 2 biến này:
					p.address = rs.getString("Address");
					p.phone = rs.getString("Phone");
					return p;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updatePublisher(PublisherModel p) {
		String sql = "UPDATE Publishers SET PublisherName = ?, Address = ?, Phone = ? WHERE PublisherID = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, p.publisherName);
			stmt.setString(2, p.address);
			stmt.setString(3, p.phone);
			stmt.setInt(4, p.publisherId);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deletePublisher(int id) {
		// LƯU Ý: Nếu có sách thuộc nhà xuất bản này, lệnh xóa sẽ lỗi do ràng buộc khóa ngoại
		String sql = "DELETE FROM Publishers WHERE PublisherID = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Lỗi xóa Publisher: Có thể do đang có sách thuộc NXB này. " + e.getMessage());
			return false;
		}
	}
}
