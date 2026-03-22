package factory;
import Dao.*;
import javax.sql.DataSource;

public abstract class DAOFactory { // Thêm abstract ở đây

	public static final int MYSQL = 1;

	// Các hàm này để abstract để lớp con (RdbDAOFactory) tự thực hiện
	public abstract CategoryDao getCategoryDao();
	public abstract PublisherDao getPublisherDao();
	public abstract AuthorDao getAuthorDao();
	public abstract BookDao getBookDao();

	public static DAOFactory getDAOFactory(int whichFactory, DataSource dataSource) {
		switch (whichFactory) {
			case MYSQL: return new RdbDAOFactory(dataSource);
			default: return null;
		}
	}
}