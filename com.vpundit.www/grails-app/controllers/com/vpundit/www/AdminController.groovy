package com.vpundit.www

class AdminController {

    def index() { }
	
	def stockReportService
	def authenticationService
	
	def transferNewTickers()
	{
		if(request.JSON.has("adminpasswd"))
		{
			String adminpasswd = request.JSON.get("adminpasswd")
		
			render stockReportService.transferNewTickers(adminpasswd)
		}
		else
		{
			render "denied"
		}
	}
	
	def storeReportData()
	{
		if(request.JSON.has("adminpasswd"))
		{
			String adminpasswd = request.JSON.get("adminpasswd")
		
			render stockReportService.storeReportData(adminpasswd)
		}
		else
		{
			render "denied"
		}
	}
}
