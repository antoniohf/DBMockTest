# DataBase mocking tests

This is for personal reference only but feel free to use it if it's helpful.

This is a simple test to have a reference on how different DataBase mocking libraries work.

# JOOQ

Only mocks the database connection.
Does not execute queries. Not suitable for mocking state transitions.

# Mockrunner-jdbc

Mocks database driver (basically deregisters existing drivers, registers itself as the driver and mocks everything) and this makes its usage pretty straight forward.
Does not execute queries. Not suitable for mocking state transitions.

https://github.com/steinarb/mockrunner

# Database-rider

Should check this one as well:

https://github.com/database-rider/database-rider




