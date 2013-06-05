import psycopg2
from scrapy import log

class Database(object):
    connection = None
    cursor = None
    
    def __init__(self):
        try:
            self.connection = psycopg2.connect("dbname='scrapy' user='rice_cooker' host='tangra.no-ip.org' password='lenovo'")
            self.cursor = self.connection.cursor()
        except:
            print "I am unable to connect to the database"
            
    def add_item(self, item, spider_id, search_term):
        myhash = item["content_hash"].__str__()
        self.cursor.execute('SELECT Count("Id") FROM "ScrapeItems" where "ContentHash" = %s;',(myhash,))
        my_row = self.cursor.fetchone()
        if int(my_row[0]) == 0:
            self.cursor.execute('INSERT INTO "ScrapeItems"("Title", "URL", "Content", "ScrapeId", "ContentHash", "SearchTerm")VALUES (%s, %s, %s, %s, %s, %s);', 
                                (item["title"], item["url"], item["text"], spider_id.__str__(), item["content_hash"], search_term))
            self.connection.commit()

