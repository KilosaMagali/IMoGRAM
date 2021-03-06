package ivvq
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(UniversalController)
@Mock([Book,Movie,TVShow])
class UniversalControllerSpec extends Specification {

        def movieService
        def TVShowService
        BookService bookService
        def dataFillingService

    def setup() {
        movieService = new MovieService()
        movieService.transactionManager = Mock(PlatformTransactionManager) {getTransaction(_) >> Mock(TransactionStatus)}
        TVShowService = new TVShowService()
        TVShowService.transactionManager = Mock(PlatformTransactionManager) {getTransaction(_) >> Mock(TransactionStatus)}
        bookService = new BookService()
        bookService.transactionManager = Mock(PlatformTransactionManager) {getTransaction(_) >> Mock(TransactionStatus)}
    }

    def populateValidParams(params) {
        assert params != null
        params["googleID"] = "SteVfQT2WY0C"
        params["title"] = "Titre"
        params["publishedDate"] = "22-05-15"
        params["author"] = "Auteur"
        params["publisher"] = "Maison des Cartes"
        params["pageCount"] = 50
    }

    def populateValidParamsMovie(params) {
        assert params != null
        params["imdbID"] = 'tt1219289'
        params["title"] = 'Limitless'
        params["releaseDate"] = new Date(2015, 03, 03);
        params["runtime"] = '105min'
        params["genre"] = 'Mystery, Sci-Fi, Thriller'
        params["director"] = 'Neil Burger'
        params["writers"] = 'Leslie Dixon (screenplay), Alan Glynn (novel)'
        params["actors"] = 'Bradley Cooper, Robert De Niro, Abbie Cornish, Andrew Howard'
        params["country"] = 'USA'
        params["plot"] = 'With the help of a mysterious pill that enables the user to access 100 percent of his brain abilities,' +
                ' a struggling writer becomes a financial wizard, but it also puts him in a new world with lots of dangers.'
        params["poster"] = 'http://ia.media-imdb.com/images/M/MV5BMTY3NjczNzc5Nl5BMl5BanBnXkFtZTcwMzA2MzQyNA@@._V1_SX300.jpg'
    }

    def populateValidParamsTVShow(params) {
        assert params != null
        params["imdbID"] = "tt0107290"
        params["title"] = "The Movie"
        params["releaseDate"] = "22-09-15"
        params["runtime"] = "127"
        params["network"] = "OCS"
        params["overview"] = "Résume"
        params["airedEpisodes"] = 50
        params["country"] = "Madagascar"
    }

    def cleanup() {
    }

    void "test that the doSearchAll search a book"() {

        setup:
        controller.bookService = bookService
        controller.movieService = movieService
        controller.TVShowService = TVShowService

        when: "the doResearchAll action is executed"
            populateValidParams(params)
            Book book = new Book(params)
            book.save(flush: true)
            params["stringToSearch"] = "Titr"
            controller.doSearchAll()

        then: "a book is returned"
        model.bookInstanceCount == 1

        and: "his name is Da vinci Code"
        model.bookInstanceList.get(0) == book

    }

    void "test that the doSearchAll search a movie"() {
        setup:
        controller.bookService = bookService
        controller.movieService = movieService
        controller.TVShowService = TVShowService

        when: "the doResearchAll action is executed"
        populateValidParamsMovie(params)
        Movie movie = new Movie(params)
        movie.save(flush: true)
        params["stringToSearch"] = "Limit"
        controller.doSearchAll()

        then: "a movie is returned"
        model.movieInstanceCount == 1

        and: "the movie is correct"
        model.movieInstanceList.get(0) == movie
    }

    void "test that the doSearchAll search a TVShow"() {
        setup:
        controller.bookService = bookService
        controller.movieService = movieService
        controller.TVShowService = TVShowService

        when: "the doResearchAll action is executed"
        populateValidParamsTVShow(params)
        TVShow tvShow = new TVShow(params)
        tvShow.save(flush: true)
        params["stringToSearch"] = "Movie"
        controller.doSearchAll()

        then: "a TVShow is returned"
        model.tvShowInstanceCount == 1

        and: "the TVShow is correct"
        model.tvShowInstanceList.get(0) == tvShow
    }

    void "test that the doSearchMovie search a movie"() {
        setup:
        controller.movieService = movieService

        when: "the doSearchMovie action is executed"
        populateValidParamsMovie(params)
        Movie movie = new Movie(params)
        movie.save(flush: true)
        controller.doSearchMovies()

        then: "a Movie is returned"
        model.movieInstanceCount == 1

        and: "the movie is correct"
        model.movieInstanceList.get(0) == movie
    }

    void "test that the doSearchBook search a book"() {
        setup:
        controller.bookService = bookService

        when: "the doSearchBook action is executed"
        populateValidParams(params)
        Book book = new Book(params)
        book.save(flush: true)
        controller.doSearchBooks()

        then: "a book is returned"
        model.bookInstanceCount == 1

        and: "the book is correct"
        model.bookInstanceList.get(0) == book
    }

    void "test that the doSearchTVShow search a TVShow"() {
        setup:
        controller.TVShowService = TVShowService

        when: "the doSearchTVShow action is executed"
        populateValidParamsTVShow(params)
        TVShow tvShow = new TVShow(params)
        tvShow.save(flush: true)
        controller.doSearchTvShow()

        then: "a TVShow is returned"
        model.tvShowInstanceCount == 1

        and: "the TVShow is correct"
        model.tvShowInstanceList.get(0) == tvShow
    }

}
