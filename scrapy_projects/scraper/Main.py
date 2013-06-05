'''
Created on May 25, 2013

@author: alagenchev
'''

import urllib
from bs4 import BeautifulSoup
from twisted.internet import reactor
from scrapy.crawler import Crawler
from scrapy.settings import Settings
from scrapy import log
from scraper.spiders.dmoz import DmozSpider
import uuid
from uuid import uuid4

if __name__ == '__main__':
    
    site = "appmobi"
    
    spider = DmozSpider()
    spider.spider_id = uuid.uuid4()
    spider.search_term = "htc inspire"
    data = urllib.urlencode({"keywords":spider.search_term})
    html_doc = ""
    if site == "digital_trends":
        spider.domain = "digitaltrends.com"
    elif site == "appmobi":
        spider.domain='appmobi.com'
        html_doc = urllib.urlopen("http://forums.appmobi.com/search.php?sid=b627e87f1367a136eacbadc0b48883cd",data).read()
        #spider.allowed_domains = "appmobi.com"
    
        
    

    
    soup = BeautifulSoup(html_doc)
    divs = soup.findAll('div', attrs={'class' : 'postbody'})


    
        
    for div in divs:
        spider.start_urls.append("http://forums.appmobi.com" + div.a["href"].__str__()[1:]) 
    

    st  = {'ITEM_PIPELINES':'scraper.pipelines.FilterWordsPipeline', 
           'SPIDER_MODULES' : 'scraper.spiders',
           'DEFAULT_ITEM_CLASS':'scraper.items.WebsitePosting',
           'NEWSPIDER_MODULE' : 'scraper.spiders'}
    
    settings = Settings(values = st)
    crawler = Crawler(settings)
    crawler.configure() 
    crawler.crawl(spider)
    crawler.start()
    log.start()
    reactor.run() #@UndefinedVariable
    
    print "done"
    