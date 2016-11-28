# MinHash and Locality Sensitive Hashing
An implementation of MinHash and Locality Sensitive Hashing (LSH) to determine similar documents in a collection.
## Classes
### MinHash
Creates a MinHash Matrix for a document collection and specified number of random permutations (both are parameters passed to the constructor).  
### MinHashAccuracy
Tests the ability of MinHash to estimate the Jaccard Similarity of the documents in the folder _space_.  Contains a main method that will output the number of document pairs where _abs(JaccardSim - MinHashSim)>eta_ as a function of the number of permutations used to construct the MinHash as well as the value of _eta_.  
To perform this experiment you may need to alter line (19) of _MinHashAccuracy_ with the path to the folder _space_.
### MinHashTime
Contains a main method that will output the time taken to: construct the MinHash Matrix, estimate the Jaccard Similarity for every document pair in the collection, calculate the exact Jaccard Similarity for every pair in the collection.
### LSH
Performs a Locality Sensitive Hashing of a MinHash Matrix.  The function _nearDuplicatesOf_ takes a document name as a parameter and returns a list of documents that are near dupliates of it.
### NearDuplicates
Finds near duplicates of a document in a collection by creating a MinHash Matrix and then performing LSH.  Given a document name and a similarity threshold, _s_, it will output ot the console a list of documents that are _s_-similar to the input document.
