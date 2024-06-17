### Running the application

* Java 1.8 was intentionally chosen as it was in the job description. I wanted to avoid the reviewers not being able to execute because of a locked down environment
* `./gradlew clean build` will execute a  clean build and run tests
* `./gradlew bootRun` will start the application on port 8080 (no TLS unfortunately)
* Sample request `curl -X GET "http://localhost:8080/api/weather/London/GB" -H "X-API-Key: Rk1V6bYrAxk4t1Fsyx0M7tzyhy+bZQvJvCTB9MUkCbc="`
* A working Api key has been placed in the yml file for ease of execution. Normally it would be secured and not commited. This key will be revoked shortly
* Extra dependency licences where checked -  MIT or Apache 2
* On first build or after a clean, there is a warning to do with a MapStruct annotation processor issue. This doesn't affect the build success and is not present on subsequent runs.

### Overview
It's one endpoint that uses h2 database as a cache for an external service. Rate limiting is designed to limit individual users load on external the service. A GlobalExceptionHandler is used to ensure consistency of responses if other endpoints are added.

### Assumptions
* Order of the returned description is important as Open Weather Maps defines the first one as primary
* The data is stored in h2 as a cache, therefore it is ephemeral by nature. As such, I only mapped and stored minimal data
* The rate limiter was supposed to be crude and only consider a single application instance (as opposed to being backed by a distributed cache or fronted by an API gateway)
* City names in other languages are accepted
* Country validated against a list of codes has maintenance and cost benefit implications. It will also require reverse engineering Open Weather Maps custom country code mappings. I went with simple validation for now. Discussed in [CountryCodeValidator](/src/main/java/com/interview/weatherapi/validation/CountryCodeValidator.java) 

### Observations
* Open Weather Maps specifies to use ISO 3166 for the state codes, however they do internal mapping. Requesting UK, the response comes back as GB. This made it trickier to use the data as a cache
* Passing the name api a city in another language does not change the language of the weather description or city in the response (contrary to their documentation). This simplifies some of the implementation decisions
* The free Open Weather Maps name endpoint seems to update its data every 15 to 20 minutes. I consider our data stale 15 minutes after the timestamp in the response  

### Issues
* Validation annotations are honored during tests but not in code execution. I have some further ideas to investigate
* Test cases are sparse. Rate limiter service is decently tested, but I underestimated effort for this piece and didn't allocate enough time. I would not submit this as done ordinarily
* Since validation is not functioning, there is higher potential for ssrf vulnerabilities. Validation and checking the canonical url should both be done, but I didn't get there
* Lombock plugin - there is a scenario where an annotation may not be functioning as expected or ignored. [SystemDto.java](src/main/java/com/interview/weatherapi/dto/SystemDto.java)
* The weather objects from Open Weather Maps (containing descriptions) is shared among responses, is documented, and has meaning. Because of this I assume I have unique key violation issue

### Features I didn't get around to
* TLS, profiles, audit fields

Side note: events.xml was likely blocked by an email filter, so I was unable to explore the Trade Report Engine topic.