package com.vpundit.www.tests.integration

import static org.junit.Assert.*

import org.apache.commons.lang.time.DateUtils;
import org.junit.*

import com.vpundit.www.StockReportService;
import com.vpundit.www.Ticker
import com.vpundit.www.StockDayInfo

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date

class StockReportServiceIntegrationTests {

	StockReportService stockReportService
	
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testPersistence() 
	{
    	assert stockReportService.getTicker("msft") == null
		
		stockReportService.getReport("msft", 10)
		
		stockReportService.getReport("goog", 10)
		
		assert stockReportService.getTicker("msft") != null
		
		assert stockReportService.getTicker("goog") != null
		
		assert stockReportService.getTicker("msft").queued == false    
		
		assert stockReportService.getTicker("goog").queued == false
		
		stockReportService.transferNewTickers("aifornone")
		
		assert stockReportService.getTicker("msft").queued == false
		
		stockReportService.transferNewTickers("aiforall")
		
		assert stockReportService.getTicker("msft").queued == true
		
		assert stockReportService.getTicker("goog").queued == true
		
		def data = stockReportService.getTickerReportData("msft")
		
		assert data.size() > 0
		
		data = stockReportService.getTickerReportData("goog")
		
		assert data.size() > 0
		
		assert stockReportService.storeReportData("aiforall") == "true"
		
		int s1 = StockDayInfo.getAll().size()
		
		assert stockReportService.storeReportData("aiforall") == "true"
		
		int s2 = StockDayInfo.getAll().size()
		
		assert s1 == s2
		
		def tickers = Ticker.getAll()
		
		for(int i=0; i < tickers.size(); i++)
		{
			if(tickers.get(i).trained)
			{
				def stockDayInfos = StockDayInfo.findAllByTicker(tickers.get(i).symbol)
				
				assert stockDayInfos.size() > 0
				
				Calendar cal = Calendar.getInstance()
				int d = cal.get(Calendar.DAY_OF_MONTH)
				int m = cal.get(Calendar.MONTH)
				int y = cal.get(Calendar.YEAR)
				String s = y + "-" + (m+1) + "-" + d
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
				
				Calendar currentDate = Calendar.getInstance()
				currentDate.setTime(sdf.parse(s));
				
				int pastDays = 5;
				
				def stockDayInfo = null
				while(pastDays > 0 && stockDayInfo == null)
				{
					stockDayInfo = StockDayInfo.findByTickerAndDate(tickers.get(i).symbol, currentDate)
					
					currentDate.add(Calendar.DAY_OF_MONTH, -1);
					pastDays--;
					
				}
				
				assert(stockDayInfo != null)
			}
		}
    }
}
