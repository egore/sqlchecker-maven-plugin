# Purpose

This plugin assures that a given set of directories contains files with the same name. It does not perform checks on the files contents, just their names and presence is verified. This is used to manage flyway SQL scripts for multiple databases. Let's take a look at a few examples.

## Example 1: Everything is file

The following directories and their content exist:

    src/main/resources/some/directory/hsqldb/V1_0__initial.sql
    src/main/resources/some/directory/mysql/V1_0__initial.sql
    src/main/resources/some/directory/mssql/V1_0__initial.sql
    src/main/resources/some/directory/pgsql/V1_0__initial.sql

If we take the configuration shown below in the _Usage_ section, the plugin will tell you that everything is fine and your build will suceed.

## Example 2: One directory is missing

Now the following directories and their content exist:

    src/main/resources/some/directory/hsqldb/V1_0__initial.sql
    src/main/resources/some/directory/mysql/V1_0__initial.sql
    src/main/resources/some/directory/pgsql/V1_0__initial.sql

Again when using the configuration below, the plugin will check for the files and will stop the build because _src/main/resources/some/directory/mssql/V1_0__initial.sql_ is missing.

## Example 3: One file is missing

Now the following directories and their content exist:

    src/main/resources/some/directory/hsqldb/V1_0__initial.sql
    src/main/resources/some/directory/hsqldb/V1_1__important_changes.sql
    src/main/resources/some/directory/mysql/V1_0__initial.sql
    src/main/resources/some/directory/mssql/V1_0__initial.sql
    src/main/resources/some/directory/mssql/V1_1__important_changes.sql
    src/main/resources/some/directory/pgsql/V1_0__initial.sql
    src/main/resources/some/directory/pgsql/V1_1__important_changes.sql

Again we are assuming to use the configuration below, the plugin will check for the files and will again stop the build because _src/main/resources/some/directory/mysql/V1_1__important_changes.sql_ is missing.

# Usage

To include the plugin in your build you can include it in the pom.xml as shown below. The plugin itself is not available from maven central but rather from http://repo.christophbrill.de/, so be sure to include the repository in you pom.xml as well.

    <build>
        <plugins>
            <plugin>
                <groupId>de.egore911.maven</groupId>
                <artifactId>sqlchecker-maven-plugin</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>check</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <directories>
                        <directory>${project.build.resources[0].directory}/some/directory/hsqldb</directory>
                        <directory>${project.build.resources[0].directory}/some/directory/mssql</directory>
                        <directory>${project.build.resources[0].directory}/some/directory/mysql</directory>
                        <directory>${project.build.resources[0].directory}/some/directory/pgsql</directory>
                    </directories>
                </configuration>
            </plugin>
        </plugins>
    </build>

Alternatively feel free to use the commandline

     mvn de.egore911.maven:sqlchecker-maven-plugin:check -Dcheck.directories=some/directory/mssql,some/directory/pgsql

