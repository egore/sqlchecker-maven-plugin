# Usage

From the pom.xml configuration

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
                        <directory>${project.build.resources[0].directory}/some/directory/hsql</directory>
                        <directory>${project.build.resources[0].directory}/some/directory/mssql</directory>
                        <directory>${project.build.resources[0].directory}/some/directory/pgsql</directory>
                        <directory>${project.build.resources[0].directory}/some/directory/mysql</directory>
                    </directories>
                </configuration>
            </plugin>
        </plugins>
    </build>

Alternatively using the commandline

     mvn de.egore911.maven:sqlchecker-maven-plugin:check -Dcheck.directories=some/directory/mssql,some/directory/pgsql

