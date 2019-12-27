import spacy
import os

nlp = spacy.load("es_core_news_md")
print('spaCy loaded')
d = {}

for root, dirs, files in os.walk("profiles/", topdown=False):
    for file in files:
        with open("profiles/" + file) as f:
            doc = nlp(f.read())

        for i in doc.ents:
            if i.label_ != 'MISC':
                d[i.text] = i.label_

with open('chair_entities.txt', 'w') as f:
    for i in d:
        f.write(i + '\t' + d[i] + '\n')
