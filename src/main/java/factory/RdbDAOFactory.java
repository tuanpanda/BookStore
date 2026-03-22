package factory;

import Dao.*;
import Dao.impl.RdbAuthorDao;
import Dao.impl.RdbBookDao;
import Dao.impl.RdbCategoryDao;
import Dao.impl.RdbPublisherDao;

import javax.sql.DataSource;

public class RdbDAOFactory extends DAOFactory {
	private DataSource dataSource;

	// Factory nhận dataSource từ hệ thống Spring Boot
	public RdbDAOFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	@Override
	public CategoryDao getCategoryDao() {
		return new RdbCategoryDao(dataSource);
	}

	@Override
	public PublisherDao getPublisherDao() {
		return new RdbPublisherDao(dataSource);
	}

	@Override
	public AuthorDao getAuthorDao() {
		return new RdbAuthorDao(dataSource);
	}

	@Override
	public BookDao getBookDao() {
		return new RdbBookDao(dataSource);
	}
}
