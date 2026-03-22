package Models;

public class AuthorModel {
	public int authorId;
	public String authorName;
	public String biography;
	public AuthorModel() {}
	public AuthorModel(String name) { this.authorName = name; }
}