# read the data
data.time <- read.csv("C:/projects/proposal/workspace.microbrowse.0.1/prefuse/doc/MicroBrowser_TaskTime - tasktime.csv", header=TRUE)


# descriptive statistics
summary(data.time$mp_t01)
sd(data.time$mp_t01)
summary(data.time$mb_t01)
sd(data.time$mb_t01)
wilcox.test(data.time$mp_t01, data.time$mb_t01, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t02)
sd(data.time$mp_t02)
summary(data.time$mb_t02)
sd(data.time$mb_t02)
wilcox.test(data.time$mp_t02, data.time$mb_t02, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t03)
sd(data.time$mp_t03)
summary(data.time$mb_t03)
sd(data.time$mb_t03)
wilcox.test(data.time$mp_t03, data.time$mb_t03, paired=TRUE)

# do graph
with(data.time, {boxplot(mp_t01, mb_t01, mp_t02, mb_t02, mp_t03, mb_t03, names=c("mp_t01", "mb_t01", "mp_t02", "mb_t02", "mp_t03", "mb_t03"))})

# descriptive statistics
summary(data.time$mp_t04)
sd(data.time$mp_t04)
summary(data.time$mb_t04)
sd(data.time$mb_t04)
wilcox.test(data.time$mp_t04, data.time$mb_t04, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t05)
sd(data.time$mp_t05)
summary(data.time$mb_t05)
sd(data.time$mb_t05)
wilcox.test(data.time$mp_t05, data.time$mb_t05, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t06)
sd(data.time$mp_t06)
summary(data.time$mb_t06)
sd(data.time$mb_t06)
wilcox.test(data.time$mp_t06, data.time$mb_t06, paired=TRUE)



# do graph
with(data.time, {boxplot(mp_t04, mb_t04, mp_t05, mb_t05, mp_t06, mb_t06, names=c("mp_t04", "mb_t04", "mp_t05", "mb_t05", "mp_t06", "mb_t06"), las=2)})



# descriptive statistics
summary(data.time$mp_t07)
sd(data.time$mp_t07)
summary(data.time$mb_t07)
sd(data.time$mb_t07)
wilcox.test(data.time$mp_t07, data.time$mb_t07, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t08)
sd(data.time$mp_t08)
summary(data.time$mb_t08)
sd(data.time$mb_t08)
wilcox.test(data.time$mp_t08, data.time$mb_t08, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t09)
sd(data.time$mp_t09)
summary(data.time$mb_t09)
sd(data.time$mb_t09)
wilcox.test(data.time$mp_t09, data.time$mb_t09, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t10)
sd(data.time$mp_t10)
summary(data.time$mb_t10)
sd(data.time$mb_t10)
wilcox.test(data.time$mp_t10, data.time$mb_t10, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t11)
sd(data.time$mp_t11)
summary(data.time$mb_t11)
sd(data.time$mb_t11)
wilcox.test(data.time$mp_t11, data.time$mb_t11, paired=TRUE)

# descriptive statistics
summary(data.time$mp_t12)
sd(data.time$mp_t12)
summary(data.time$mb_t12)
sd(data.time$mb_t12)
wilcox.test(data.time$mp_t12, data.time$mb_t12, paired=TRUE)

