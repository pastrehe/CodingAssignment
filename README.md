# LogParser

The program takes the input file path as input argument, reads input file and parse them.
Also, it flags any long events that take longer than 4ms. Found events are writed to file-based HSQLDB.
If destination table doesn't exists, application creates it.
In current config database files located in *'working_directory/db/'* dir

### Starting application

Application requires [gradle](https://gradle.org) to run.
Clone code from [github](https://github.com/pastrehe/CodingAssignment.git), and let the gradle to do it's work

```sh
$ git clone https://github.com/pastrehe/CodingAssignment.git
$ cd CodingAssignment/
$ gradle build
$ gradle run  --args='/Users/user/app.log'
```

By default, app logs to stdout with *INFO* severity.
This behaviour can be changed in 'log4j.properties' file.

Application's parameters stored in 'application.properties' file.
You can change following parameters:
 - maxDuration - upper threshold for *Alert* flag
 - sqlConnectionUrl - database connection parameters
 - sqlUserName - username for database connection
 - sqlPassword - password for database connection

Application uses 1/2 of detected CPUs, i.e. if server has 8 CPUs, parser will create 4 threads for data processing.

> N.B.
> The application is developed with some assumptions and limitations:
> 
>- input file contains pairs of events, there are no lost *Start* or *Stop* events
>- parser can't merge events located in different files
>
>Those limitation can be removed with the other cache implementation,
>for example with [guava cache](https://github.com/google/guava) that supports expirations,
>or [Redis](https://redis.io) - fast key-value storage, that supports expiration and data persistence.
>
>Any error during execution is considered critical and interrupts execution. This is not production-ready behaviour,
>and should be changed in real app.

