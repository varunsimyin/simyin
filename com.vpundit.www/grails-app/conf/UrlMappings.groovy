class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/product/$id"(controller: "product", parseRequest: true) {
			action = [GET: "show", PUT: "update", DELETE: "delete", POST: "save"]
		}
		
		"/user"(controller: "user", parseRequest: true) {
			action = [GET: "show", PUT: "update", DELETE: "delete", POST: "login"]
		}
		
		"/stockreport/$ticker"(controller: "stockReport", parseRequest: true) {
			action = [POST: "request"]
		}
		
		"/admin/transfernewtickers"(controller: "admin", parseRequest: true) {
			action = [POST: "transferNewTickers"]
		}
		
		"/admin/storereportdata"(controller: "admin", parseRequest: true) {
			action = [POST: "storeReportData"]
		}
		
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
