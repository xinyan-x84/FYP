deg <- read.table("D:\\FYP\\FYP2\\PenroseTiling\\FatAndThin\\Thin\\Average Degree\\AverageDegreePRT.txt", header = TRUE, sep = "")
attach(deg)
X = seq(3,8,0.1)
deg.model <- nls(y ~ a*x^c+b, start = list(a = -3, b = 5, c=-0.2))
summary(deg.model)
Y = -3.73622*X^(-0.43882)+5.35276
plot(x,y,main ="Y = -3.73622*X^(-0.43882)+5.35276")
lines(X,Y)
TSS<- sum((y-mean(y))^2)