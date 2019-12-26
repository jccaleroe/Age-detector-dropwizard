# Age-detector-dropwizard
Experiment for serving a python model on java

Follow instructions on detector folder and then in image-loader folder.

## Entity disambiguation progress

1. Download of [Wikimedia](https://dumps.wikimedia.org/) Spanish xml dump.

2. Extraction of each page clean content with 
[Attardi](https://github.com/attardi/wikiextractor) extractor into a single file with an approximate
size of 6 GB.

3. Extract named entities of this single file using spaCy. A named entity is a "real-world object" that’s assigned 
a name – for example, a person, a country, a product or a book title. spaCy can recognize 
[various types](https://spacy.io/api/annotation#named-entities) of named entities in a document.

4. Saved the extracted named entities into a sqlite data base for quick access on posterior 
processing task.

5. Calculate the distance of each entity document title with all the entities in it's content with a 
custom vector representation using 1 and 2 n-grams.

6. Cluster the entities of each document into 5 groups using jenks and the distances of the vectors,
so the entities in the 4th and 5th group are marked for disambiguation with respect to the document's title.

7. Replace the entities in the 5th group with the ones in 4th group in the paragraph they appear.

8. Save the modified paragraphs with the correct and incorrect antities into a json file
with an approximated size of 40 GB.

## Database version 2

1. Download the MariaDb Wikipedia database.

2. Create scrip to get all the pages of a category ans it's subcategories' pages recursively
up to a 10th level in the spanning tree.

3. With the spanning tree generated, find disambiguation pages to create new dataset.

## Models to use 

The datasets created are meant to be used with the model proposed [here](https://github.com/contextscout/ned-graphs),
also, a [knowledge base](https://spacy.io/api/kb) could be created to be used directly with spaCy and 
their entity linking [API](https://spacy.io/usage/training#entity-linker).

An example of an entity recognition demo based on the content of the text can be seen 
[here](https://www.dbpedia-spotlight.org/demo/) 
