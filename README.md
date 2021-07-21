# uberweisung by fjsimon

Build a simple wallet microservice running on the JVM.

## Description

A monetary account holds the current balance for a player.
The balance can be modified by registering transactions on the account, either debit transactions (removing funds) or credit transactions (adding funds).
Create a REST API and an implementation that fulfils the requirements detailed below and honours the constraints.

## Desired Functionality

1. Check current balance per player.
2. Debit / Withdrawal per player. A debit transaction will only succeed if there are sufficient funds on the account (balance - debit amount >= 0). The caller will supply a transaction id that must be unique for all transactions. If the transaction id is not unique, the operation must fail.
3. Credit per player. The caller will supply a transaction id that must be unique for all transactions. If the transaction id is not unique, the operation must fail.
4. Transaction history per player.

Extra: Withdrawals (between min: 20 max: 300) returns notes (100, 50, 20, 10, 5) using the smallest number of notes.

## Documentation (uberweisung API)

Swagger url: http://localhost:8080/swagger-ui.html

## Security

JSON Web Tokens are an open, industry standard RFC 7519 method for representing claims securely between two parties.
JWT.IO (https://jwt.io/)  allows you to decode, verify and generate JWT.

## Build and run build

1. Build the project by running the following command thought terminal: mvn clean install -U

2. Start application by running the following command thought terminal: mvn spring-boot:run

# reference-rates by fjsimon

https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html

## Build

mvn clean install

## Run

mvn spring-boot:run

## Tasks / Endpoints

* Allows an API caller to retrieve the reference rate data for a given 
Date for all available Currencies.

http://localhost:8080/reference/rates?date=2020-09-10

* Given a Date, source Currency (eg. JPY), target Currency (eg. DKK), 
and an Amount, returns the Amount given converted from the first to the 
second Currency as it would have been on that Date (assuming zero fees).

http://localhost:8080/reference/rates/convert?date=2020-09-14&source=JPY&target=DKK&amount=1000

* Given a start Date, an end Date and a Currency, return the highest reference 
exchange rate that the Currency achieved for the period.

http://localhost:8080/reference/rates/maximum?start=2010-09-10&end=2020-09-14&currency=GBP

* Given a start Date, an end Date and a Currency, determine and return 
the average reference exchange rate of that Currency for the period.

http://localhost:8080/reference/rates/average?start=2010-09-10&end=2020-09-14&currency=GBP

* Allows an API caller to retrieve the reference rate data (daily) for all available Currencies using a client.

http://localhost:8080/reference/rates/daily