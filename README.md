
# Overview
The Internet Movie Database (IMDB) provides access to their [datasets][imdb-datasets] to customers for personal and non-commercial use.  This data is refreshed daily.  Let's design a ratings API based on this data.

# Requirements
* Java 1.8+
* Maven 3.5.3+

## Dependencies
[Commons CSV][commons-csv]	
[Spring Boot][spring-boot]	
[H2][h2]	
[Logback Classic][logback-classic]

# How to Run
1. Clone this repository
1. Execute in the top level directory in order to build and assemble the application:
```bash
mvn clean install
```
1. Execute the following to run the application:
```bash
java -Dratings.properties.file=src/main/resources/movie-ratings-api.properties -jar target/movie-ratings-api-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```	
1. Once the following log message is written, the API is active:
```bash
[main] INFO  c.o.movie.ratings.MovieRatingsApp - Started MovieRatingsApp in 2.371 seconds (JVM running for 358.461)
```
1. A request can be executed on a browser or curl:
```
http://localhost:8080/movie-ratings?title=Ambulans
```
Response
```json
{"title":"Ambulans","type":"short","userRating":"7.7","calculatedRating":null,"castList":"Zbigniew Józefowicz, Leopold R. Nowak, Boguslaw Sochnacki, Janusz Morgenstern, Tadeusz Lomnicki, Krzysztof Komeda, Jerzy Lipman, Janina Niedzwiecka","episodes":[]}
```

## Settings File
The properties files referenced above provides configuration to the application that can be modified as needed.  

```bash
# Database Configuration
ratings.db.username=test_user
ratings.db.password=*****
ratings.db.path=./target/test-data/test 
	
# IMDb Configuration
imdb.title.basics.url=https://datasets.imdbws.com/title.basics.tsv.gz
imdb.title.ratings.url=https://datasets.imdbws.com/title.ratings.tsv.gz
imdb.title.principals.url=https://datasets.imdbws.com/title.principals.tsv.gz
imdb.title.episode.url=https://datasets.imdbws.com/title.episode.tsv.gz
imdb.name.basics.url=https://datasets.imdbws.com/name.basics.tsv.gz 	
	
# Ratings App Configuration
title.include.years=2019
```
### Offline Datasets
This application downloads datasets from IMDb and the urls are specified in the configuration file.  As these are large files, it is possible to specify a URL that is on the file system; for example:
```bash
imdb.title.basics.url=file:///Users/<user>/Downloads/title.basics.tsv.gz
```



# Design

## Requirements
Build an API endpoint that pulls and persists the following attributes for titles (movies, tv shows, etc):
* Title 
* Rating 
* Cast List

Provide the ability to only persist titles from certain years.

For TV shows, average all the episode ratings and provide an average rating

Provide ability to regularly synchronize and update persisted data from IMDb.

Return the persisted title via query by title name.

**Sample Response to user**:
```json
{
   "title": "Foo",
   "type": "tvSeries",
   "userRating": "5.4",
   "castList": "Person 1, Person 2",
   "calculatedRating": 5.9,
   "episodes": [{
      "title": "foo",
      "userRating": 6.5,
      "seasonNumber": 1,
      "episodeNumber": 10,
      "castList": "Person 1, Person 2"      
   }]
}
```

### Data to Pull
| Field         | Data File               | Notes                                                                                                                                                                       |
|---------------|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| primaryTitle  | title.basics.tsv.gz     |                                                                                                                                                                             |
| startYear     | title.basics.tsv.gz     | Likely won't need to persist. but will use to filter only titles for  2019.  This makes sense for movies, but how do you determine the release date of the TV show episode? |
| averageRating | title.ratings.tsv.gz    | How do individual episodes tie into this table?                                                                                                                             |
| *             | title.principals.tsv.gz |  Need to map nconst to tconst       |
| primaryName   | name.basics.tsv.gz |      |

### A Consolidation Approach to Aggregating the Data:
The problem with the approach listed below is that all subsequent records after "title.basics" will need to be quieried for before they are inserted.  Millions of "Select" sql statements for millions of records isn't performant.
<pre>
For All records in title.basics.tsv.gz 
	SKIP
		startYear != 2019
	IF titleType == "tvEpisode"
		STORE in Episodes_Store
			tconst
			primaryTitle
	STORE in TITLES_STORE
		tconst
		titleType
		primaryTitle
</pre>
<pre>
For All records in title.ratings.tsv.gz	
	STORE in TITLES_STORE
		averageRating
		WHERE
			tconst MATCHES
	STORE in EPISODES_STORE
		averageRating
		WHERE
			tconst MATCHES
</pre>
<pre>
For All records in title.principals.tsv.gz
	STORE in TITLES_STORE
		nconst in castList
		WHERE
			tconst MATCHES
	STORE in EPISODES_STORE
		nconst in castList
		WHERE
			tconst MACTHES
</pre>
<pre>
For All record in title.episode.tsv.gz
	STORE in EPISODE_STORE
		parentTconst, episodeNumber, seasonNumber
		WHERE
			tconst MATCHES	
</pre>
<pre>
For All records in name.basics.tsv.gz
	STORE in PRINCIPALS_STORE
		primaryName
		WHERE
			nconst MATCHES
</pre>

#### Data Model

##### Titles
| tconst | primaryTitle | averageRating | castList |
|--------|--------------|---------------|----------|

##### Episodes
| parent_tconst | tconst | primaryTitle | averageRating | castList | 
|---------------|--------|--------------|---------------|----------|  

##### Names
| nconst | primaryName | 
|--------|-------------|

#### Notes & Considerations
* Will store the tconst id.  We could generate an id for our system; however, this will be easier to sync the data when syncing the data.
* Is a relational db the best way to go, especially since some of the data is not "1:1", ex: title -> cast list; title -> episodes?
* The cast_list could be better modeled, especially if this would need to be queried in the future.

### A Cloning Approach to Aggregating the Data: 
This approach adopts a storage design that aligns with the downloaded dataset.  This was the approach used.
<pre>
For All records in title.basics.tsv.gz 
	SKIP
		startYear != 2019
	STORE in TITLES_STORE
		tconst
		titleType
		primaryTitle
</pre>
<pre>
For All records in title.ratings.tsv.gz	
	STORE in RATINGS_STORE
		tconst
		averageRating
</pre>
<pre>
For All records in title.principals.tsv.gz
	STORE in PRINCIPAL_STORE
		tconst		
		nconst
		ordering
</pre>
<pre>
For All record in title.episode.tsv.gz
	STORE in EPISODE_STORE
		tconst
		parentTconst
		episodeNumber
		seasonNumber
</pre>
<pre>
For All records in name.basics.tsv.gz
	STORE in PRINCIPALS_STORE
		nconst
		primaryName
</pre>

### High Level Diagram
![](docs/diagram.svg)

# Improvements
- Ability to account for interrupted downloads
- Database - batching, prepared statements
- Explore Inmemory DB, Cache
- Work with Data persistence framework and remove custom sql related code

[imdb-datasets]: https://www.imdb.com/interfaces 
[commons-csv]: http://commons.apache.org/proper/commons-csv/index.html
[spring-boot]: https://spring.io/projects/spring-boot
[h2]: https://www.h2database.com/html/main.htmla
[logback-classic]: http://logback.qos.ch/
