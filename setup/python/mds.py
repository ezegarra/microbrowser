#MDS - multi dimensional scaling
#dries.wijns[@]gmail.com
import math
import random
import copy

"""
    import pylab as p
    
    def testSimple():
    distanceMatrix = list( [[0,1,1,1],
    [4,0,1,1],
    [1,9,0,1],
    [3,1,1,0]] )
    points = MDS(distanceMatrix).points()
    plot(points)
    exit()
    
    def plot(points):
    x = [point[0] for point in points]
    y = [point[1] for point in points]
    
    p.plot(x,y, 'ro')
    p.show()
    """    

class MDS():
    """Multi-dimensional scaling:
        
        input: distanceMatrix = n*n matrix with the distances between the points,
        diagonal elements must be zero,
        only lower triangle is required (upper triangle will be ignored).
        
        output: list with 2D-points: [[x1,y1],[x2,y2],...,[xn,yn]]
        """
    distanceMatrix = list(list())
    
    def __init__(self, distanceMatrix):
        if self.checkDistanceMatrix(distanceMatrix):
            self.distanceMatrix = distanceMatrix
    
    def initPoints(self):
        """First guess is on a straight line, spaces 1 apart"""
        points = list()
        for i in range(len(self.distanceMatrix)):
            x = i
            y = 0
            points.append([x,y])
        return points
    
    def checkDistanceMatrix(self, m):
        for i in range(len(m)):
            if m[i][i] != 0: #diagonal elements must be zero
                return False
        #loop over all elements under diagonal
        for j in range(len(m)):
            for k in range(j+1):
                if m[j][k] < 0:
                    return False #lenghts must be greater than zero
        
        return True
    
    def distance(self, p1, p2):
        return math.sqrt((p2[0]-p1[0])**2+(p2[1]-p1[1])**2) #euclidian distance between 2 points in 2D
    
    def zeros(self, n):
        return [[0 for item in range(n)] for item in range(n) ]
    
    def createDistanceMatrix(self, points):
        m = self.zeros(len(points))
        for j in range(len(points)):
            for k in range(j+1):
                m[j][k] = self.distance(points[j], points[k])
        return m 
    
    def score(self, points):
        """Sum of squared errors between distances between points given and the distanceMatrix """
        s = 0
        for j in range(len(points)):
            for k in range(j+1):
                s += (self.distance(points[j], points[k]) - self.distanceMatrix[j][k])**2
        return s
    
    def mutate(self, points):
        #select a random point to mutate
        p = random.randint(0, len(points)-1)
        #select a random angle in wich to mutate (in radians)
        a = random.random() * math.pi
        #select a random direction in wich to mutate
        d = random.choice([-1, 1])
        #select a random lenght to mutate
        l = random.choice(self.frange(0.1, 10, 0.1))
        
        #apply mutation
        points[p][0] += d*math.cos(a)*l
        points[p][1] += d*math.sin(a)*l
        
        return points
    
    def frange(self, start, stop, inc):
        l = list()
        i = start
        while i<=stop:
            l.append(i)
            i+=inc
        return l
    
    def points(self):
        numberOfRounds = 9999
        
        topFive = [self.initPoints()]
        
        for i in range(numberOfRounds):
            topFiveCopy = copy.deepcopy(topFive)
            for item in topFive:
                topFiveCopy.append(self.mutate(item))
            topFive = self.selectTopFive(topFiveCopy)
        
        return topFive[0]
    
    def selectTopFive(self, l):
        sortedl = sorted(l, key=self.score)
        return sortedl[0:5]

if __name__ == "__main__":
    #testSimple()
    pass            
