package ru.interprocom.axioma.cache.util;

//import ru.interprocom.axioma.cache.model.Author;
//import ru.interprocom.axioma.cache.model.Book;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelGenerator {
/*	public Model<Book> getBookModel() {
		return bookModel;
	}

	public void setBookModel(Model<Book> bookModel) {
		this.bookModel = bookModel;
	}

	public Model<Author> getAuthorModel() {
		return authorModel;
	}

	public void setAuthorModel(Model<Author> authorModel) {
		this.authorModel = authorModel;
	}

	private Model<Book> bookModel;
	private Model<Author> authorModel;

	@Autowired
	private Faker faker;

	@PostConstruct
	private void init() {
		authorModel = Instancio.of(Author.class)
				.ignore(Select.field(Author::getId))
				.ignore(Select.field(Author::getBooks))
				.supply(Select.field(Author::getFirstName), () -> faker.name().firstName())
				.supply(Select.field(Author::getLastName), () -> faker.name().lastName())
				.toModel();


		bookModel = Instancio.of(Book.class)
				.ignore(Select.field(Book::getId))
				.supply(Select.field(Book::getTitle), () -> faker.book().title())
				.toModel();
	}*/
}
