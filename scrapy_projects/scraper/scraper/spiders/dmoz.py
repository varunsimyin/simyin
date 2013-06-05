from scrapy.spider import BaseSpider
from scrapy.selector import HtmlXPathSelector
from scrapy.http import Request
import hashlib
import base64
from scraper.items import WebsitePosting
from scrapy import log

class DmozSpider(BaseSpider):
    name = "dmoz"
    url_hashes = []
    spider_id = None
    search_term = ""
    
    
    def parse(self, response):
        #file = open('/home/alagenchev/response.html', 'w')
        #file.write(response.body);
        #file.flush()
        #file.close()
        hxs = HtmlXPathSelector(response)
        sites = hxs.select('//div[@class="postbody"]')
        for site in sites:
            item = WebsitePosting()
            title = site.select('h3/a/text()').extract()
            #content = site.select('div[@class="content"]/text()').extract()
            content = site.select('div[@class="content"]').extract()
            combined_content = ""
            for text in content:
                combined_content+=text
            url = site.select('p/a/@href').extract()
            item['title'] = title
            url = url[0].__str__()[1:]
            url = "http://forums.appmobi.com" + url.__str__()
            item['url'] = url
            item['text'] = combined_content
            yield item
            
        urls = hxs.select('//a/@href').extract()
        for url in urls:
            if not url.__str__().startswith("./viewtopic"):
                continue
            combined_url = "http://forums.appmobi.com" + url[1:]
            myhash = hashlib.sha1(url).digest()
            base64encoding = base64.b64encode(myhash)

            if base64encoding in self.url_hashes or "viewtopic.php" not in combined_url:
                continue
            else:
                self.url_hashes.append(base64encoding)
            #log.msg("extraced url: " + combined_url, level = log.ERROR)
            yield Request(combined_url, callback=self.parse)
