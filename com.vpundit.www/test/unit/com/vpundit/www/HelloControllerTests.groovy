package com.vpundit.www



import org.junit.*
import grails.test.mixin.*

@TestFor(HelloController)
@Mock(Hello)
class HelloControllerTests
{

	def populateValidParams(params)
	{
		assert params != null
		// TODO: Populate valid properties like...
		//params["name"] = 'someValidName'
	}

	void testIndex()
	{
		controller.index()
		assert "/hello/list" == response.redirectedUrl
	}

	void testList()
	{

		def model = controller.list()

		assert model.helloInstanceList.size() == 0
		assert model.helloInstanceTotal == 0
	}

	void testCreate()
	{
		def model = controller.create()

		assert model.helloInstance != null
	}

	void testSave()
	{
		controller.save()

		assert model.helloInstance != null
		assert view == '/hello/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/hello/show/1'
		assert controller.flash.message != null
		assert Hello.count() == 1
	}

	void testShow()
	{
		controller.show()

		assert flash.message != null
		assert response.redirectedUrl == '/hello/list'

		populateValidParams(params)
		def hello = new Hello(params)

		assert hello.save() != null

		params.id = hello.id

		def model = controller.show()

		assert model.helloInstance == hello
	}

	void testEdit()
	{
		controller.edit()

		assert flash.message != null
		assert response.redirectedUrl == '/hello/list'

		populateValidParams(params)
		def hello = new Hello(params)

		assert hello.save() != null

		params.id = hello.id

		def model = controller.edit()

		assert model.helloInstance == hello
	}

	void testUpdate()
	{
		controller.update()

		assert flash.message != null
		assert response.redirectedUrl == '/hello/list'

		response.reset()

		populateValidParams(params)
		def hello = new Hello(params)

		assert hello.save() != null

		// test invalid parameters in update
		params.id = hello.id
		//TODO: add invalid values to params object

		controller.update()

		assert view == "/hello/edit"
		assert model.helloInstance != null

		hello.clearErrors()

		populateValidParams(params)
		controller.update()

		assert response.redirectedUrl == "/hello/show/$hello.id"
		assert flash.message != null

		//test outdated version number
		response.reset()
		hello.clearErrors()

		populateValidParams(params)
		params.id = hello.id
		params.version = -1
		controller.update()

		assert view == "/hello/edit"
		assert model.helloInstance != null
		assert model.helloInstance.errors.getFieldError('version')
		assert flash.message != null
	}

	void testDelete()
	{
		controller.delete()
		assert flash.message != null
		assert response.redirectedUrl == '/hello/list'

		response.reset()

		populateValidParams(params)
		def hello = new Hello(params)

		assert hello.save() != null
		assert Hello.count() == 1

		params.id = hello.id

		controller.delete()

		assert Hello.count() == 0
		assert Hello.get(hello.id) == null
		assert response.redirectedUrl == '/hello/list'
	}
}
