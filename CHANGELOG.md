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

# Changelog

All notable changes to *Mensch ärgere Dich nicht* will be documented in this
file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic
Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Fixed

* Info logging statements not visible on light-mode terminals (reported by [@guemax] in [#119])
* Crash when a player wins (reported by [@guemax] in [#143])
* Multiple windows being opened when a player wins (reported by [@guemax] in [#145])
* Figure being moved to base before dice has been rolled (reported by [@guemax] in [#149])
* Figure being able to move out of house regardless of random number (reported by [@guemax] in [#148])
* Close button doesn't exit the program in a terminal (reported by [@guemax] in [#150])

### Removed

* Punishment for moving wrong figure (reported by [@MeiNic] in [#158])

## [v2.0.0] - 2025-05-10

### Added

* Button to show rules during game

### Changed

* Artwork (thank you [@guefra]!)

### Fixed

* Not being able to use single player mode (reported by [@MeiNic] in [#66])
* Finished figures aren't recognized as being in house (reported by [@MeiNic] in [#73])
* Finished figures can be moved (reported by [@MeiNic] in [#73])
* Figures "overflowing" the maximum field number (reported by [@MeiNic] in [#73])
* Typo (reported by [@guemax] in [#88])
* Typo (reported by [@MeiNic] in [#94])
* Figures in house jumping over other figures (reported by [@MeiNic] in [#95])
* Player being able to knock own figure on first field (reported by [@MeiNic] in [#95])
* Crash caused by wrong resource paths (reported by [@MeiNic] in [#100])

### New Contributors

* [@guefra] made their first contribution in [#80]

## [v1.0.0] - 2023-08-10

### Added

* Figure movement
* Consistent look and feel across systems
* Current player hint
* Options for naming and coloring players
* Player movement
* Game loop
* Display winner
* Display hints during game
* Default player names
* Bot movement
* Overview of rules
* Simple logging

### Fixed

* Dice never yielding a six (reported by [@TastingComb] in [#4])
* Endless loop when moving a figure (reported by [@MeiNic] in [#14])
* Being able to move a figure you don't even own (reported by [@MeiNic] in [#21])
* Winner sometimes not being displayed (reported by [@guemax] in [#24])
* Figures in house jumping over other figures (reported by [@MeiNic] in [#31])
* Figures not going into the house (reported by [@MeiNic] in [#31])
* Crash caused by wrong index (reported by [@MeiNic] in [#33])
* Wrong hint being displayed (reported by [@MeiNic] in [#34])
* User input always being reset in landingpage (reported by [@MeiNic] in [#36])
* Being forced to beat another figure with a not-owned one (reported by [@MeiNic] in [#37])
* Figure on first field cannot be knocked (reported by [@MeiNic] in [#41])
* Grammar (reported by [@TastingComb] in [#55])

### New Contributors

* [@MeiNic] made their first contribution in [#1]
* [@TastingComb] made their first contribution in [#4]
* [@guemax] made their first contribution in [#7]

[@guefra]: https://github.com/guefra
[@guemax]: https://github.com/guemax
[@TastingComb]: https://github.com/TastingComb
[@MeiNic]: https://github.com/MeiNic

[#158]: https://github.com/MeiNic/MenschAergereDichNicht/issues/158
[#150]: https://github.com/MeiNic/MenschAergereDichNicht/issues/150
[#149]: https://github.com/MeiNic/MenschAergereDichNicht/issues/149
[#148]: https://github.com/MeiNic/MenschAergereDichNicht/issues/148
[#145]: https://github.com/MeiNic/MenschAergereDichNicht/pull/145
[#143]: https://github.com/MeiNic/MenschAergereDichNicht/pull/143
[#119]: https://github.com/MeiNic/MenschAergereDichNicht/pull/119
[#100]: https://github.com/MeiNic/MenschAergereDichNicht/pull/100
[#95]: https://github.com/MeiNic/MenschAergereDichNicht/pull/95
[#94]: https://github.com/MeiNic/MenschAergereDichNicht/pull/94
[#88]: https://github.com/MeiNic/MenschAergereDichNicht/pull/88
[#80]: https://github.com/MeiNic/MenschAergereDichNicht/pull/80
[#73]: https://github.com/MeiNic/MenschAergereDichNicht/pull/73
[#66]: https://github.com/MeiNic/MenschAergereDichNicht/pull/66
[#55]: https://github.com/MeiNic/MenschAergereDichNicht/pull/55
[#41]: https://github.com/MeiNic/MenschAergereDichNicht/pull/41
[#37]: https://github.com/MeiNic/MenschAergereDichNicht/pull/37
[#36]: https://github.com/MeiNic/MenschAergereDichNicht/pull/36
[#34]: https://github.com/MeiNic/MenschAergereDichNicht/pull/34
[#33]: https://github.com/MeiNic/MenschAergereDichNicht/pull/33
[#31]: https://github.com/MeiNic/MenschAergereDichNicht/pull/31
[#24]: https://github.com/MeiNic/MenschAergereDichNicht/pull/24
[#21]: https://github.com/MeiNic/MenschAergereDichNicht/pull/21
[#14]: https://github.com/MeiNic/MenschAergereDichNicht/pull/14
[#7]: https://github.com/MeiNic/MenschAergereDichNicht/pull/7
[#4]: https://github.com/MeiNic/MenschAergereDichNicht/pull/4
[#1]: https://github.com/MeiNic/MenschAergereDichNicht/pull/1

[unreleased]: https://github.com/MeiNic/MenschAergereDichNicht/compare/v2.0.0...HEAD
[v2.0.0]: https://github.com/MeiNic/MenschAergereDichNicht/compare/v1.0.0...v2.0.0
[v1.0.0]: https://github.com/MeiNic/MenschAergereDichNicht/compare/15dd1fdde8c027b6df9a789522ba0752b2b185fe...v1.0.0
