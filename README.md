# LRUC
Last Recently Used Cache - always discards least recently used item first.
It stores last used memory.

#Getting started
To run this project we need have installed java jdk at least 1.8.x .

##Installing and running application
We can easy install project in terminal with bash.

First we need to clone project from repository:

````
git clone https://github.com/xav9211/LRUC.git
````
Next we need to enter the LRUC directory:

````
cd LRUC/
````
Now we need build project with:

````
./gradlew clean build
````
and run it by command:

````
java -jar ./build/libs/LRUC-1.0-SNAPSHOT.jar
````

#API
This application is build on simple REST api with 4 functions:

- get(id):
    - URL: /cache/{key}
    - Method: GET
    - URL params: key=[integer]
    - Response codes:
        - Success (200 Ok) Returns value of item with key added as url param
        - Not Found (404) When item with key param is not in cache
        - Bad Request (400) When key param is not valid type
        
- put(id, value):
    - URL: /cache/{key}
    - Method: POST
    - URL params: key=[integer]
    - Data params: value=[string] (type test/plain) can be any type of file represented by string
    - Response codes:
        - Success (200 Ok) Puts new item to cache with key and value.
            If capacity is reached, then least used item is removed.
        - Bad Request (400) When key param is not valid type

- changeCapacity(capacity):
    - URL: /cache/capacity/{capacity}
    - Method: PUT
    - URL params: capacity=[integer]
    - Response codes:
        - Success (200 Ok) Change capacity as capacity param.
            When capacity is less than cache max size then 
            least used items over capacity size are removed.
        - Bad Request (400) When capacity param is not valid type or is 0 or less.

- invalidate():
    - URL: /cache/invalidate
    - Method: DELETE
    - Response codes:
        - Success (200 Ok) Clear whole cache


#Project simple description
Project contains:

- classes:
    - LRUCache it is RESTController class which operates on Cache class
        and handle exceptions to send proper request when needed (Bad Request, Not Found)
    - Cache do all operations on Cache
    - ItemNotFoundException exception which is thrown when user try to get item
        which not exist in the cache and is handled in LRUCache class

- tests classes:
    - CacheTests for unit tests
    - LRUCacheTests for integration tests
    
Initial value of capacity is set in config.properties file, it must be greater than 0.