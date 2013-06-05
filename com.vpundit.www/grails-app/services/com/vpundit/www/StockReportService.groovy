package com.vpundit.www

import datastructures.GrailsStockDayInfo
import java.text.SimpleDateFormat

import net.sf.json.JSONArray;
import net.sf.json.JSONObject
import static main.PortfolioPrediction.predict

import static main.AnalyzeNewPort.addTicker


class StockReportService 
{

    def stockDataCompiler
	def stockDomain
	def stockDataSet
	def analyzeNewPort
	
	def getTicker(String symbolParam)
	{
		def ticker = Ticker.findBySymbol(symbolParam)
		
		return ticker
	}
	
	def getReport(String symbolParam, int numDays)
	{
		def ticker = Ticker.findBySymbol(symbolParam)
		
		if(ticker == null)
		{
			ticker = new Ticker(symbol: symbolParam, queued: false, trained: false)
			ticker.save()
		}
		
		if(!ticker.trained)
		{
			return "stock requested"
		}
		else
		{
			def stockDayInfos = StockDayInfo.findAllByTicker(ticker.symbol, [max: 30, sort: "date", order: "desc"])
			
			return stockDayInfos
		}
	}
	
	def transferNewTickers(String adminpasswd)
	{
		if(adminpasswd.equals("aiforall"))
		{
			def tickers = Ticker.getAll()
			
			for(int i=0; i < tickers.size(); i++)
			{
				if(!tickers.get(i).queued)
				{
					tickers.get(i).queued = true
					tickers.get(i).save()
					
					if(!tickers.get(i).trained)
					{
						addTicker(tickers.get(i).symbol, false)
					}
				}
			}
			
			return "true"
		}
		
		return "false"
	}
	
	def List<GrailsStockDayInfo> getTickerReportData(String ticker)
	{
		Calendar cal = Calendar.getInstance()
		int d = cal.get(Calendar.DAY_OF_MONTH)
		int m = cal.get(Calendar.MONTH)
		int y = cal.get(Calendar.YEAR)
		String s = y + "-" + (m+1) + "-" + d
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
		
		Calendar currentDate = Calendar.getInstance()
		currentDate.setTime(sdf.parse(s));
		
		int pastDays = 30;
		
		def stockDayInfo = StockDayInfo.findByTickerAndDate(ticker, currentDate)
		while(pastDays > 0 && stockDayInfo == null)
		{
			currentDate.add(Calendar.DAY_OF_MONTH, -1);
			stockDayInfo = StockDayInfo.findByTickerAndDate(ticker, currentDate)
			pastDays--;
			
		}
		
		def coreStockDayInfos = null
		if(pastDays < 30)
		{
			currentDate.add(Calendar.DAY_OF_MONTH, 1);
			coreStockDayInfos = predict(ticker, currentDate, Calendar.getInstance())
		}
		return coreStockDayInfos
	}
	
	def storeReportData(String adminpasswd)
	{
		if(adminpasswd.equals("aiforall"))
		{
			def tickers = Ticker.getAll()
			
			for(int i=0; i < tickers.size(); i++)
			{
				def coreStockDayInfos
				try
				{
					coreStockDayInfos = getTickerReportData(tickers.get(i).symbol)
				} catch(Exception e)
				{
					if(e.getMessage().indexOf("404") == -1)
					{
						throw e
					}
					else
					{
						coreStockDayInfos = null
					}
				}
				
				if(coreStockDayInfos != null && coreStockDayInfos.size() > 0)
				{
					tickers.get(i).trained = true
					tickers.get(i).save()
					for(int j=0; j < coreStockDayInfos.size(); j++)
					{
						def stockDayInfo = new StockDayInfo(ticker: coreStockDayInfos.get(j).ticker, date: coreStockDayInfos.get(j).date, open: coreStockDayInfos.get(j).open, close: coreStockDayInfos.get(j).close, decision: coreStockDayInfos.get(j).decision)
						stockDayInfo.save()
					}
				}
				
			}
			
			return "true";
		}
		
		return "false"
	}
}
