package Dao;
import Models.AuthorModel;
import java.util.List;

public interface AuthorDao {
	List<AuthorModel> getAllAuthors();
	AuthorModel getAuthorById(int id);
	int insertAuthor(AuthorModel author);
	boolean updateAuthor(AuthorModel author);
	boolean deleteAuthor(int id);
}
