package com.vpundit.www

import net.sf.json.JSONArray;
import net.sf.json.JSONObject
import grails.converters.JSON

class StockReportController 
{
	
	def stockReportService
	def authenticationService

    def index() { }
	
	def request()
	{
		if(request.JSON.has("username") && request.JSON.has("passwd"))
		{
			String username = request.JSON.get("username")
			String passwd = request.JSON.get("passwd")
		
			def user = authenticationService.authenticate(username, passwd)
			
			if(user != null)
			{
				def report = stockReportService.getReport(params.ticker, 20)
				
				if(report.equals("stock requested"))
				{
					render report
				}
				else
				{
					def reportlist = (List<StockDayInfo>)report
					
					def result = new JSONArray()
					
					for(int i=0; i < reportlist.size(); i++)
					{
						def jobject = new JSONObject()
						jobject.put("day", reportlist.get(i).date.get(Calendar.DAY_OF_MONTH))
						jobject.put("month", reportlist.get(i).date.get(Calendar.MONTH))
						jobject.put("year", reportlist.get(i).date.get(Calendar.YEAR))
						
						jobject.put("open", reportlist.get(i).open)
						jobject.put("close", reportlist.get(i).close)
						jobject.put("decision", reportlist.get(i).decision)
						
						result.add(jobject)
						
					}
					render result as JSON
				}
			}
			else
			{
				render "denied"
			}
		}
		else
		{
			render "denied"
		}
		
	}
}
