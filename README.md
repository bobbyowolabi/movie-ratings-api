
# Overview
The Internet Movie Database (IMDB) provides access to their [datasets][imdb-datasets] to customers for personal and non-commercial use.  This data is refreshed daily.  Let's design a ratings API based on this data.

# Requirements
* Java 1.8+
* Maven 3.5.3+

## Dependencies

# Design

## Part 1
Build an application that pulls and persists the following attributes for titles (movies, tv shows, etc):
* Title 
* Rating 
* Cast List

Return the persisted title via query by title name.

### Data to Pull
| Field         | Data File               | Notes                                                                                                                                                                       |
|---------------|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| primaryTitle  | title.basics.tsv.gz     |                                                                                                                                                                             |
| startYear     | title.basics.tsv.gz     | Likely won't need to persist. but will use to filter only titles for  2019.  This makes sense for movies, but how do you determine the release date of the TV show episode? |
| averageRating | title.ratings.tsv.gz    | How do individual episodes tie into this table?                                                                                                                             |
| *             | title.principals.tsv.gz |  Need to map nconst to tconst       |

### Data Model

#### Titles
| imdb_id | title | rating | cast_list |
|---------|-------|--------|-----------|

#### Sub Titles
| parent_imdb_id | imdb_id | title | rating |  
|----------------|---------|-------|--------|  

### Components
#### Ratings Source (IMDB impl)
#### Streaming TSV Reader
#### Ratings Data Store

### Considerations
* Will store the tconst id.  We could generate an id for our system; however, this will be easier to sync the data when we get to part 3 and need to refresh the data.
* Is a relational db the best way to go, especially since some of the data is not "1:1", ex: title -> cast list; title -> episodes?
* The cast_list could be better modeled, especially if this would need to be queried in the future.

# Improvements


[imdb-datasets]: https://www.imdb.com/interfaces 