from scrapy.item import Item, Field


class WebsitePosting(Item):
    url = Field()
    title = Field()
    text = Field()
    content_hash = Field()
    
def __str__(self):
    return "Title: " + self.title + ", URL: " + self.url + ", Text: " + self.text