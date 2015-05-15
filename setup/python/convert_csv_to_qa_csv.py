import csv
import time
import datetime

# read input and output files
inputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\MicroBrowserData\data\QueryResults_AllQuestionsAndAnswersForTagJavaMicroProbe.csv', 'rb')
outputfile = open('C:\projects\proposal\workspace.microbrowse.0.1\MicroBrowserData\data\QueryResults_AllQuestionsAndAnswersForTagJavaMicroProbeOUTPUT.csv', 'wb')

# define reader and writers
reader = csv.reader(inputfile)
writer = csv.writer(outputfile)

# define a list to keep track of what questions we have to avoid relationship
# constrain issues in the database where an answer does not have a parent question
QIDs = []
acount = 0;
removedAcount = 0;

# skip first line which is header
next(reader, None) 

# iterate thru the list of items in the input and convert them to the new structure for output file
for row in reader:
    R = []
    
    # postid
    R.append(row[0])

    # type
    if row[1] == "1":
        R.append("Q")
        QIDs.append(row[0])
    else:
        R.append("A")
        
    # parentid
    if row[1] == "2":
        R.append(row[3])
    else:
        R.append("NULL")
    # categoryid
    R.append("NULL")

    # catidpath1
    R.append("NULL")

    # catidpath2
    R.append("NULL")

    # catidpath3
    R.append("NULL")

    # acount
    R.append(row[16])
    
    # amaxvote
    R.append("0")
    
    # selchildid
    if row[2] == "":
        R.append("NULL")
    else:
        R.append(row[2])
    
    # closedbyid
    R.append("NULL")
    
    # userid
    R.append("1")
    
    # cookieip
    R.append("NULL")
    
    # createip
    R.append("180558595")
    
    # lastuserid
    R.append("1")
    
    # lastip
    R.append("180558595")

    # upvotes
    if int(row[5]) < 0:
        R.append("0")
    else:
        R.append(row[5])

    # downvotes
    if int(row[5]) < 0:
        R.append(row[5])
    else:
        R.append("0")

    # netvotes
    R.append(row[5])
    
    # lastviewip
    R.append("180558595")

    # views
    R.append(row[6])
    
    # hotness
    R.append("27387200000")
    
    # flagcount
    R.append("0")
    
    # format
    R.append("html")
    
    # created 2/7/2013 10:35
    #t=datetime.datetime.strptime(row[4],"%m/%d/%Y %H:%M")
    R.append(datetime.datetime.strftime(datetime.datetime.strptime(row[4],"%m/%d/%Y %H:%M"), "%Y-%m-%d %H:%M:00"));
    
    # updated
    #R.append(row[12])
    if row[12] == "":
        R.append("NULL")
    else:
        R.append(datetime.datetime.strftime(datetime.datetime.strptime(row[12],"%m/%d/%Y %H:%M"), "%Y-%m-%d %H:%M:00"));
    
    # updatetype
    R.append("E")
    
    # title
    R.append(row[14])
    
    # content
    R.append(row[7])

    # tags
    row[15] = row[15].replace("><", ",")
    row[15] = row[15].replace(">","");
    row[15] = row[15].replace("<","");
    R.append(row[15])
    
    # name
    R.append("NULL")
    
    # notify
    R.append("@")
    
    # a verification print
    print "."

    # write the row to the output csv
    if row[1] == "1":
        writer.writerow(R)
    else:
        if row[3] in QIDs:
            acount = acount + 1
            writer.writerow(R)
        else:
            removedAcount = removedAcount + 1
            print "-"

inputfile.close()
outputfile.close()

print "done. questions: " + repr(len(QIDs)) + " , answers=" + repr(acount) + ", removed answers=" + repr(removedAcount)
