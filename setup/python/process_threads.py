from xml.dom import minidom
from topia.termextract import tag
from topia.termextract import extract
from array import *
import csv
from HTMLParser import HTMLParser
import re
from math import sqrt

class MLStripper(HTMLParser):
    def __init__(self):
        self.reset()
        self.fed = []
    def handle_data(self, d):
        self.fed.append(d)
    def get_data(self):
        return ''.join(self.fed)

def strip_tags(html):
    s = MLStripper()
    s.feed(html)
    return s.get_data()


doc = minidom.parse('C:/Users/Administrator/Desktop/exported_data.xml')

# Read all the rows
#rowslist = doc.getElementsByTagName( 'rows' )
#print rowslist[0].toxml()
rows = doc.getElementsByTagName( 'row' )
print rows[1].toxml()

# get text
def getText(nodes):
    text = []
    for node in nodes:
        text.append(node.data)
    return ''.join(text)

# Returns a distance-based similarity score
def sim_distance(terms, q1, q2):
    # Get the list of shared terms
    s1 = {}
    for item in terms[q1]:
        if item in terms[q2]:
            s1[item]=1

    # if they have no term in common return 0
    if len(s1) == 0: return 0

    # Add up the squares of all the differences
    sum_of_squares = sum([pow(terms[q1][item]-terms[q2][item],2)
                          for item in terms[q1] if item in terms[q2]])

    return 1/(1+sum_of_squares)

    
####################################################
QUESTION = "1"
ANSWER = "2"
threads = {}
tagger = tag.Tagger('english')
tagger.initialize()
extractor = extract.TermExtractor(tagger=tagger)

terms = {}

# process each question thread
for row in rows:
    values = row.getElementsByTagName('value')
    postid = getText(values[0].childNodes)
    body = strip_tags(getText(values[1].childNodes))
    title = strip_tags(getText(values[2].childNodes))
    tags = getText(values[3].childNodes)
    posttypeid = getText(values[4].childNodes)
    parentid = getText(values[5].childNodes)

    # extract the contents of the question into array
    str_list = []
    str_list.append(title.encode("utf-8"))
    str_list.append(body.encode("utf-8"))
    str_list.append(tags.encode("utf-8"))

    if posttypeid == QUESTION:
        #print "QUESTION"

        #First check if we have already stored an answer
        if postid in threads:
            threads[postid] = ''.join(str_list) + " " + threads[postid]
        else:
            threads[postid] = ''.join(str_list) 
    else:
        #print "ANSWER"
        #print "data is="
        #print data
        if parentid in threads:
            #print "found parentid"
            #print data[parentid]
            threads[parentid] = ''.join(str_list) + " " + threads[parentid]
        else:
            #print "did not find parent id"
            threads[parentid] = ''.join(str_list)


allterms = set([])
    
# process each answer thread
with open('dataoutput.csv', 'wb') as csvfile:
    spamwriter = csv.writer(csvfile, delimiter='|',
                            quotechar='', quoting=csv.QUOTE_NONE)
    for threadid in threads:
        #print extractor.tagger(data[postid])
        #termsfound = extractor(threads[postid])
        #print threadid
        extractor.tagger(threads[threadid])
        extractor.filter = extract.DefaultFilter(singleStrengthMinOccur=2)
        termsfound = extractor(threads[threadid])
        termentry = {}
        # for each term found, store into global array of terms
        for termfound in termsfound:
            _term = re.sub('[|]', '', termfound[0])
            allterms.add(_term)
            it = { 'postid':threadid, 'term': _term, 'occurrences' : termfound[1], 'strength': termfound[2]}
 ##           #termentry[_term] = 1
            termentry[_term] = termfound[1]
            #spamwriter.writerow((it['postid'], it['term'], it['occurrences'],it['strength']))
            try:
                spamwriter.writerow([it['postid'],it['term'],it['occurrences'],it['strength']])
            except UnicodeEncodeError:
                print _term
                print _term.encode("utf-8")
                spamwriter.writerow([it['postid'].encode("utf-8"),it['term'].encode("utf-8"),it['occurrences'].encode("utf-8"),it['strength'].encode("utf-8")])

        # store a dictionary of threads
        terms[threadid] = termentry

############################################################################
# Print a list of all the terms
############################################################################
#with open('all_terms.csv', 'wb') as csvfile:
#    termwriter = csv.writer(csvfile, delimiter=',', quotechar='', quoting=csv.QUOTE_NONE)

#    for term in allterms:
#        a = []
#        if term :
#            a.append(term.encode("utf-8"))
#        else:
#            a.append(' ')
#        termwriter.writerow(a)
f = open("all_terms.csv","w")
for item in allterms:
    f.write("%s\n" % item)
f.close()
############################################################################
# build matrix for computing similarities
############################################################################

terms_length = len(allterms)
thread_vector = {}
for threadid in threads.keys():
    a = []
    for term in allterms:
        if term in terms[threadid]:
##            a.append(1.0)
            a.append(terms[threadid][term])
        else:
            a.append(0.0)
    thread_vector[threadid] = a
        
with open('dataoutputthreadtoterm.csv', 'wb') as csvfile:
    spamwriter1 = csv.writer(csvfile, delimiter='\t', quotechar='', quoting=csv.QUOTE_NONE)

    for item in thread_vector:
        a = []
        a.append(item)
        a.extend(thread_vector[item])
        spamwriter1.writerow(a)		

# compute cosine similarity
def dot_product(vectorA, vectorB):
    dotProduct = 0
    for i in range(len(vectorA)):
        dotProduct += vectorA[i] * vectorB[i]

    return dotProduct

def magnitude(vector):
    return sqrt(dot_product(vector, vector))

def cosine_similarity(vectorA, vectorB):
    dotProduct = dot_product(vectorA, vectorB)
    magnitudeOfA = magnitude(vectorA)
    magnitudeOfB = magnitude(vectorB)

    return dotProduct/(magnitudeOfA*magnitudeOfB)

similarities = []

def compute_all_cosine_similarities():
    
    for t1 in threads.keys():
        for t2 in threads.keys():
            try:
                similarity = cosine_similarity(thread_vector[t1], thread_vector[t2])
                #spamwriter.writerow([t1,t2,similarity])
                similarities.append([t1, t2, similarity])
            except KeyError:
                print "could not find a key: " + t1 + ", " + t2
            except ZeroDivisionError:
                print "div by zero t1=" + t1 + ", t2=" + t2
                #spamwriter.writerow([t1, t2, '0'])
                similarities.append([t1, t2, '0'])

    with open('dataoutputsimilaries.csv', 'wb') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',',
                            quotechar='', quoting=csv.QUOTE_NONE)

        for item in similarities:
            spamwriter.writerow(item)
            
############################################################################

# print how many questions found
print "total rows:"
print len(rows)
print "total threads:"
print len(threads)
print "sample thread entry:"
print threads['1245374']
print "sample term entry:"
print terms['11133637']
print "total terms:"
print len(terms)
print "items are similar:"
print sim_distance(terms, '11133637', '506595')
print sim_distance(terms, '10120848', '4889570')
print "total terms:"
print len(allterms)
print "items cosine similar"
print cosine_similarity(thread_vector['10120848'], thread_vector['4889570'])
print cosine_similarity(thread_vector['4889570'], thread_vector['10120848'])
#compute_all_cosine_similarities()
print "done "



