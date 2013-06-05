# Scrapy settings for scraper project
#not used right now

SPIDER_MODULES = ['scraper.spiders']
NEWSPIDER_MODULE = 'scraper.spiders'
DEFAULT_ITEM_CLASS = 'scraper.items.Website'

ITEM_PIPELINES = ['scraper.pipelines.FilterWordsPipeline']
