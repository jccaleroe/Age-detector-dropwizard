import spacy

nlp = spacy.load("es_core_news_md")

print("finish loading model")

while True:
    doc = nlp(input())
    d = {}
    for i in doc.ents:
        if i.label_ != 'MISC':
            d[i.text] = i.label_
    for i in d:
        print(i + '\t' + d[i])

    print('Finish :)')
