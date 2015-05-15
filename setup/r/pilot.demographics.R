# Create an age frequency

a <- c(1,1,2,0)
a.names <- c("18-21", "22-25", "26-30", ">30")
names(a) <- a.names
barplot(a, main="Age Distribution", ylab="Frequency", xlab="Age Range")

# Create an years of experience

b <- c(9,10,5,10)
hist(b, main="Years of college experience", xlab="Years")

# Gender distribution
c <- c(3, 1)
lbls <- c("Male", "Female")
pct <- round(c/sum(c)*100)
lbls <- paste(lbls, pct) # add percents to labels 
lbls <- paste(lbls,"%",sep="") # ad % to labels 
pie(c, col=rainbow(length(c)),labels = lbls, main="Gender distribution")