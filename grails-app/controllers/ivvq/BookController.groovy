package ivvq

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BookController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    BookService bookService
    def itemUserService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Book.list(params), model: [bookInstanceCount: Book.count()]
    }

    def show(Book bookInstance) {

        User user = session["currentUser"]
        boolean isFavourite = false

        if (user != null) {
            isFavourite = itemUserService.isFavourite(user, bookInstance)
        }

        respond bookInstance, model: [isFavourite: isFavourite]
    }

    def create() {
        respond new Book(params)
    }

    @Transactional
    def save(Book bookInstance) {
        if (bookInstance == null) {
            notFound()
            return
        }

        if (bookInstance.hasErrors()) {
            respond bookInstance.errors, view: 'create'
            return
        }

        bookInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'book.label', default: 'Book'), bookInstance.id])
                redirect bookInstance
            }
            '*' { respond bookInstance, [status: CREATED] }
        }
    }

    def edit(Book bookInstance) {
        respond bookInstance
    }

    @Transactional
    def update(Book bookInstance) {
        if (bookInstance == null) {
            notFound()
            return
        }

        if (bookInstance.hasErrors()) {
            respond bookInstance.errors, view: 'edit'
            return
        }

        bookInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Book.label', default: 'Book'), bookInstance.id])
                redirect bookInstance
            }
            '*' { respond bookInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Book bookInstance) {

        if (bookInstance == null) {
            notFound()
            return
        }

        bookInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Book.label', default: 'Book'), bookInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'book.label', default: 'Book'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    @Transactional
    def deleteToFavourite(Book bookInstance) {
        User user = session["currentUser"]

        ItemUser itemUser = itemUserService.getItemUser(user, bookInstance)

        itemUser.favourite = false
        itemUserService.saveItemUser(itemUser)

        redirect(action: "show", id: bookInstance.id)
    }

    @Transactional
    def addToFavourite(Book bookInstance) {
        User user = session["currentUser"]

        ItemUser itemUser = itemUserService.getItemUser(user, bookInstance)

        itemUser.favourite = true
        itemUserService.saveItemUser(itemUser)

        redirect(action: "show", id: bookInstance.id)
    }

    def fetchAllComments() {


    }

}
