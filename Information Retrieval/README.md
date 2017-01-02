# Information Retrieval
An information retrieval system used to search a database of documents (contained in the folder *IR*) for documents relevant to a query *q*.
## Classes
### PositionalIndex
Stores the _postings list_ for a given collection of documents.  Contains the methods _weight_, _TPScore_, and _VSSCore_ to determine the relevance of a document to a given query.
### Query Processor
Returns to the top _k_ most relevant documents to a given query.
### Node
For a given document _Node_ stores it's document frequency and the associated postings list.
### Posting
Stores each document the given word is in and the words location in that document.
### RelevanceScore
Contians the weight as well as the TPScore and VSScore for the given document.
### RelevanceScoreComparator
A comparator for the RelevanceScore class.

## Notes
The file _IR-Report.pdf_ contains the results obtained from a number of queries to the document database.

Github capped the number of documents in _IR_ at 1,000.  The original number of files was ~11,000.  
