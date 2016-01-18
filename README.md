# Super Simple Stock Market

## Requirements
1. Provide working source code that will :
	1. For a given stock, 
		1. Given any price as , calculate the dividend yield
		2. Given any price as input, calculate the P/E Ratio
		3. Record a trade, with timestamp, quantity of shares, buy or sell indicator and 
		4. Calculate Volume Weighted Stock Price based on trades in past 15 minutes
	2. Calculate the GBCE All Share Index using the geometric mean of prices for all stocks traded price

## Constraints & Notes
1. Written in Java 8
2. No database or GUI, all data held in memory
3. Built with gradle

## Sample data from the Global Beverage Corporation Exchange

Stock Symbol | Type | Last dividend | Fixed dividend | Par value 
------------ | ------------- | ------------- | ------------- | -------------
TEA | Common|  0 |  | 100
POP | Common | 8 | | 100
ALE | Common | 23 | | 60
GIN | Preferred | 8 | 2% | 100
JOE | Common | 13 | | 250

## Running
The code was written as a Java library, hence it lacks main class. Basic functionality can be verified while running unit tests:
```gradle test```

To build library in order to obtain a jar file, one can execute the following gradle task:
```gradle clean build```

If you don't have gradle on your local machine, please use gradle wrapper instead:
```./gradlew clean build```
