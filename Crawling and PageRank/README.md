# Crawling and PageRank
A topic sensitive web crawler used to build a graph of **wikipedia** pages.  The pages are then ranked according to the [PageRank](https://en.wikipedia.org/wiki/PageRank) algorithm.
## Classes
### Weighted_Q
An implementation of a _weighted queue_.  Each element of queue is a tuple _\<x,w(x)\>_ where _x_ is any item and _w(x)_ is the weight of _x_.  The item with the largest weight will be the next item removed from the queue, if items have identical weights they are removed in FIFO order.
### Tuple
Stores _\<Page,weight\>_ pairs.
### TupleComparator
Compares the weights of tuples then, if applicable, the order they were inserted.
### WikiCrawler
Performs a topic-sensitive crawling of **wikipeida** according to the input key words.  Can also perform a non-topic-senstive crawl.  Takes a max number of verticies to appear in the wiki page graph as a parameter.  To be courteous the crawler checks **/wiki/robots.txt** to find disallowed webpages and waits 1 second every 10 pages it crawls.
### PageRank
Runs PageRank on the input graph.
### WikiTennisRanker
Ouputs to the console PageRank statistics for the graph described by **wikiTennis.txt**.
