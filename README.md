<!-- This file is part of MenschAergereDichNicht. -->
<!-- Copyright (C) 2023-2025 MeiNic, TastingComb and contributors. -->

<!-- This program is free software: you can redistribute it and/or modify -->
<!-- it under the terms of the GNU General Public License as published by -->
<!-- the Free Software Foundation, either version 3 of the License, or -->
<!-- (at your option) any later version. -->

<!-- This program is distributed in the hope that it will be useful, -->
<!-- but WITHOUT ANY WARRANTY; without even the implied warranty of -->
<!-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the -->
<!-- GNU General Public License for more details. -->

<!-- You should have received a copy of the GNU General Public License -->
<!-- along with this program.  If not, see <https://www.gnu.org/licenses/>. -->

![Logo of Mensch ärgere Dich nicht](res/title.png)

*Mensch ärgere Dich nicht* is a digital version of the [same-named
German board
game](https://en.wikipedia.org/wiki/Mensch_%C3%A4rgere_Dich_nicht).

![Screenshot of the application showing the game board during a
round.](res/screenshot.png)

## Installation

*Mensch ärgere Dich nicht* currently requires the Java Runtime
Environment to be installed on your computer.  We might provide native
executables in the future.

Go to the [latest
release](https://github.com/MeiNic/MenschAergereDichNicht/releases/tag/latest)
of *Mensch ärgere Dich nicht* and download the `.jar` file at the
bottom of the page.  Open a terminal in the same directory as the
downloaded file and run:

``` shell
java -jar <your-jar-file>
```

Have fun!

## Contributing

Thank you very much for your interest in making Mensch ärgere Dich
nicht better.  If you would like to report a bug, request a feature or
have a question, please [open an
issue](https://github.com/MeiNic/MenschAergereDichNicht/issues/new)
here on GitHub.

If you would like to help develop *Mensch ärgere Dich nicht*, go ahead
and fork this repository. See [Getting Started with
GitHub](https://docs.github.com/en/get-started) if you haven't worked
with Git and/or GitHub before.

## Development

This game uses [Maven](https://maven.apache.org/) as a build tool. To build and run the game, use these commands:

```bash
mvn compile && java -jar target/MenschAergereDichNicht-*.jar 
```

### Testing

For now the project has a coverage of about 60% of the code base.
To run the tests, you can use your build-in IDE function or Maven:

```bash
mvn test
```

We have the goal to increase the test coverage in the future. If you want to help with that, please write tests for the 
code you are working on. Note that your test must also run in a headless environment, as we use GitHub actions to run 
the tests on every pull request.

### Code Formatting

We use the Google Java Format for formatting the code. To format the code or check if the code is formatted correctly,
you can use these commands:

```bash
# Check formatting
mvn spotless:check

# Apply formatting
mvn spotless:apply
```

You find more information about the formatting in the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

### Creating a Pull Request

When you have made changes to the code and want them to be merged into the main branch, please create a pull request. 
Make sure to ...
- ... format your code correctly,
- ... run all tests,
- ... explain the changes you made,
- ... and reference an issue in the commit description (e.g., "Fixes #123") if applicable.

Note that on your first pull request the GitHub actions will not run automatically, but require a maintainer to start 
them manually. After that, the actions will run automatically on every pull request.

## License

*Mensch ärgere Dich nicht* is available under the
[GPL-3.0-or-later](./COPYING) license.

Copyright (C) 2023–2025 MeiNic, TastingComb and contributors.
