package com.vpundit.www

import org.springframework.dao.DataIntegrityViolationException

class HelloController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [helloInstanceList: Hello.list(params), helloInstanceTotal: Hello.count()]
    }

    def create() {
        [helloInstance: new Hello(params)]
    }

    def save() {
        def helloInstance = new Hello(params)
        if (!helloInstance.save(flush: true)) {
            render(view: "create", model: [helloInstance: helloInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'hello.label', default: 'Hello'), helloInstance.id])
        redirect(action: "show", id: helloInstance.id)
    }

    def show(Long id) {
        def helloInstance = Hello.get(id)
        if (!helloInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'hello.label', default: 'Hello'), id])
            redirect(action: "list")
            return
        }

        [helloInstance: helloInstance]
    }

    def edit(Long id) {
        def helloInstance = Hello.get(id)
        if (!helloInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'hello.label', default: 'Hello'), id])
            redirect(action: "list")
            return
        }

        [helloInstance: helloInstance]
    }

    def update(Long id, Long version) {
        def helloInstance = Hello.get(id)
        if (!helloInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'hello.label', default: 'Hello'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (helloInstance.version > version) {
                helloInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'hello.label', default: 'Hello')] as Object[],
                          "Another user has updated this Hello while you were editing")
                render(view: "edit", model: [helloInstance: helloInstance])
                return
            }
        }

        helloInstance.properties = params

        if (!helloInstance.save(flush: true)) {
            render(view: "edit", model: [helloInstance: helloInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'hello.label', default: 'Hello'), helloInstance.id])
        redirect(action: "show", id: helloInstance.id)
    }

    def delete(Long id) {
        def helloInstance = Hello.get(id)
        if (!helloInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'hello.label', default: 'Hello'), id])
            redirect(action: "list")
            return
        }

        try {
            helloInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'hello.label', default: 'Hello'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'hello.label', default: 'Hello'), id])
            redirect(action: "show", id: id)
        }
    }
}
