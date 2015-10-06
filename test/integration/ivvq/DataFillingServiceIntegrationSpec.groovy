package ivvq

import org.codehaus.groovy.grails.web.json.JSONElement
import spock.lang.*

/**
 *
 */
class DataFillingServiceIntegrationSpec extends Specification {

    def dataFillingService

    def setup() {
    }

    def cleanup() {
    }

    void "test that the MOVIE is correctly added to the database"() {

        given: "an IMDB id that belongs to a unique movie"
        String imdbID = "tt1219289"

        when: "the json is loaded and the movie is saved"
        Movie movie = dataFillingService.jsonToMovieSave(imdbID)

        then: "the movie has no errors"
        !movie.hasErrors()

        and: "Movie's title is correct"
        movie.title == "Limitless"

        and: "The movie is added to the database"
        Movie.findByImdbID(imdbID) != null
    }

    void "test that the BOOK is correctly added to the database"() {

        given: "an IMDB id that belongs to a unique movie"
        String googleID = "SteVfQT2WY0C"

        when: "the json is loaded and the book is saved"
        Book book = dataFillingService.jsonToBookSave(googleID)

        then: "the book has no errors"
        !book.hasErrors()

        and: "Book's title is correct"
        book.title == "Da Vinci code"

        and: "The book is added to the database"
        Book.findByIsbn13(9782709637404) != null
    }
}
