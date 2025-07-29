package ru.interprocom.axioma.cache.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.interprocom.axioma.cache.Application;
import ru.interprocom.axioma.cache.config.H2JpaConfig;
//import ru.interprocom.axioma.cache.mapper.BookMapper;
//import ru.interprocom.axioma.cache.model.Author;
//import ru.interprocom.axioma.cache.model.Book;
//import ru.interprocom.axioma.cache.repository.AuthorRepository;
//import ru.interprocom.axioma.cache.repository.BookRepository;
import ru.interprocom.axioma.cache.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BooksControllerTest {
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper om;

/*	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookMapper mapper;

	@Autowired
	private ModelGenerator modelGenerator;

	private Book testBook;

	private Author anotherAuthor;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.build();
		var author = Instancio.of(modelGenerator.getAuthorModel()).create();
		anotherAuthor = Instancio.of(modelGenerator.getAuthorModel()).create();
		authorRepository.save(author);
		authorRepository.save(anotherAuthor);
		testBook = Instancio.of(modelGenerator.getBookModel()).create();
		testBook.setAuthor(author);
	}

	@Test
	public void testIndex() throws Exception {
		bookRepository.save(testBook);
		var result = mockMvc.perform(get("/books"))
				.andExpect(status().isOk())
				.andReturn();

		var body = result.getResponse().getContentAsString();
		assertThatJson(body).isArray();
	}

	@Test
	public void testShow() throws Exception {

		bookRepository.save(testBook);

		var request = get("/books/{id}", testBook.getId());
		var result = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = result.getResponse().getContentAsString();

		assertThatJson(body).and(
				v -> v.node("title").isEqualTo(testBook.getTitle()),
				v -> v.node("authorId").isEqualTo(testBook.getAuthor().getId()),
				v -> v.node("authorFirstName").isEqualTo(testBook.getAuthor().getFirstName()),
				v -> v.node("authorLastName").isEqualTo(testBook.getAuthor().getLastName())
		);
	}

	@Test
	public void testCreate() throws Exception {
		var dto = mapper.map(testBook);

		var request = post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(dto));

		mockMvc.perform(request)
				.andExpect(status().isCreated());

		var book = bookRepository.findByTitle(testBook.getTitle()).get();

		assertThat(book).isNotNull();
		assertThat(book.getTitle()).isEqualTo(testBook.getTitle());
		assertThat(book.getAuthor().getId()).isEqualTo(testBook.getAuthor().getId());
	}

	@Test
	public void testCreateWithWrongauthor() throws Exception {
		var dto = mapper.map(testBook);
		dto.setAuthorId(12345L);

		var request = post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(dto));

		mockMvc.perform(request)
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdate() throws Exception {
		bookRepository.save(testBook);

		var dto = mapper.map(testBook);

		dto.setTitle("new title");
		dto.setAuthorId(anotherAuthor.getId());

		var request = put("/books/{id}", testBook.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(dto));

		mockMvc.perform(request)
				.andExpect(status().isOk());

		var task = bookRepository.findById(testBook.getId()).get();

		assertThat(task.getTitle()).isEqualTo(dto.getTitle());
		assertThat(task.getAuthor().getId()).isEqualTo(dto.getAuthorId());
	}

	@Test
	public void testPartialUpdate() throws Exception {
		bookRepository.save(testBook);

		var dto = new HashMap<String, Long>();
		dto.put("authorId", anotherAuthor.getId());

		var request = put("/books/{id}", testBook.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(dto));

		mockMvc.perform(request)
				.andExpect(status().isOk());

		var task = bookRepository.findById(testBook.getId()).get();

		assertThat(task.getTitle()).isEqualTo(testBook.getTitle());
		assertThat(task.getAuthor().getId()).isEqualTo(dto.get("authorId"));
	}

	@Test
	public void testDestroy() throws Exception {
		bookRepository.save(testBook);
		var request = delete("/books/{id}", testBook.getId());
		mockMvc.perform(request)
				.andExpect(status().isNoContent());

		assertThat(bookRepository.existsById(testBook.getId())).isEqualTo(false);
	}*/
}
