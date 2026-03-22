package Dao;

import Models.PublisherModel;

import java.util.List;

public interface PublisherDao {
	List<PublisherModel> getAllPublishers();
	PublisherModel getPublisherById(int id);
	int insertPublisher(PublisherModel publisher);
	boolean updatePublisher(PublisherModel publisher);
	boolean deletePublisher(int id);
}
