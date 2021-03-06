package ivvq

class Book {
    // Allow to check faster if a book has been added to the database
    String googleID

    String isbn13
    String title
    String publishedDate
    String author
    String publisher
    String description
    String image
    Integer pageCount

    static mapping = {
        description sqlType: "text"
        image sqlType: "text"
    }

    static constraints = {
        googleID matches: "[0-9|a-z|A-Z]{12}", blank: false, unique: true
        isbn13 matches: "978[0-9]{10}", nullable: true
        title blank: false
        description blank: true, nullable: true
        author blank: false
        pageCount min: 0
        image nullable: true, blank: true
        publisher blank: false
    }
}
