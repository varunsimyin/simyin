from scrapy import log
import hashlib
import base64
from bs4 import BeautifulSoup
from Database import Database

class FilterWordsPipeline(object):
    text_hashes = []
    
    def process_item(self, item, spider):
        #log.msg("\n\nitem: " + item.__str__() + "scrape id: " + spider.spider_id.__str__(), level = log.ERROR)
        soup = BeautifulSoup(item["text"])
        item["text"] =  ''.join( soup.findAll( text = True ) ) 
        stripped_content = item["text"].replace(" ", "")
        myhash = hashlib.sha1(stripped_content.__str__()).digest()
        base64encoding = base64.b64encode(myhash)
        item["content_hash"] = base64encoding
        if base64encoding not in self.text_hashes:
            #log.msg("\n\nitem: " + item.__str__(), level = log.ERROR)
            self.text_hashes.append(base64encoding)
            database = Database()
            database.add_item(item, spider.spider_id, spider.search_term)


