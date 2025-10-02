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

* Crash when a player wins ([#143], [#151])
* Figure being able to move out of house regardless of random number ([#148], [#157])
* Figure being moved to base before dice has been rolled ([#149], [#160])
* Multiple windows being opened when a player wins ([#145], [#151])
* Close button doesn't exit the program in a terminal ([#150], [#156])
* Info logging statements not visible on light-mode terminals ([#118], [#119])

### Removed

* Punishment for moving wrong figure ([#158], [#160])

## [v2.0.0] - 2025-05-10

### Added

* Allow showing rules during game

### Changed

* Artwork (thank you [@guefra]! See [#57], [#80], [#84], [#85], [#87])

### Fixed

* Crash caused by wrong resource paths ([#101])
* Not being able to use single player mode ([#66])
* Player being able to knock own figure on first field ([#95])
* Finished figures aren't recognized as being in house ([#73])
* Figures "overflowing" the maximum field number ([#73])
* Figures in house jumping over other figures ([#95])
* Finished figures can be moved ([#73])
* Grammar ([#88], [#94])

### New Contributors

* [@guefra] made their first contribution in [#80]

## [v1.0.0] - 2023-08-10

### Added

* Figure movement
* Human players
* Bot players
* Display winner
* Overview of rules
* Display hints during game
* Consistent look and feel across systems
* Default player names
* Options for naming and coloring players
* Simple logging

### Fixed

* Crash caused by wrong index ([#33])
* Endless loop when moving a figure ([#14])
* Being able to move a figure you don't even own ([#21])
* Dice never yielding a six ([#4])
* Being forced to beat another figure with a not-owned one ([#37])
* Figure on first field cannot be knocked ([#41])
* Winner sometimes not being displayed ([#24])
* Figures not going into the house ([#31])
* Figures in house jumping over other figures ([#31])
* User input always being reset in landingpage ([#36])
* Wrong hint being displayed ([#34])
* Grammar ([#55])

### New Contributors

* [@MeiNic] made their first contribution in [#1]
* [@TastingComb] made their first contribution in [#4]
* [@guemax] made their first contribution in [#7]

[@guefra]: https://github.com/guefra
[@guemax]: https://github.com/guemax
[@TastingComb]: https://github.com/TastingComb
[@MeiNic]: https://github.com/MeiNic

[#160]: https://github.com/MeiNic/MenschAergereDichNicht/pull/160
[#158]: https://github.com/MeiNic/MenschAergereDichNicht/pull/158
[#157]: https://github.com/MeiNic/MenschAergereDichNicht/pull/157
[#156]: https://github.com/MeiNic/MenschAergereDichNicht/pull/156
[#151]: https://github.com/MeiNic/MenschAergereDichNicht/pull/151
[#150]: https://github.com/MeiNic/MenschAergereDichNicht/pull/150
[#149]: https://github.com/MeiNic/MenschAergereDichNicht/pull/149
[#148]: https://github.com/MeiNic/MenschAergereDichNicht/pull/148
[#145]: https://github.com/MeiNic/MenschAergereDichNicht/pull/145
[#143]: https://github.com/MeiNic/MenschAergereDichNicht/pull/143
[#119]: https://github.com/MeiNic/MenschAergereDichNicht/pull/119
[#118]: https://github.com/MeiNic/MenschAergereDichNicht/pull/118
[#101]: https://github.com/MeiNic/MenschAergereDichNicht/pull/101
[#100]: https://github.com/MeiNic/MenschAergereDichNicht/pull/100
[#95]: https://github.com/MeiNic/MenschAergereDichNicht/pull/95
[#94]: https://github.com/MeiNic/MenschAergereDichNicht/pull/94
[#88]: https://github.com/MeiNic/MenschAergereDichNicht/pull/88
[#87]: https://github.com/MeiNic/MenschAergereDichNicht/pull/87
[#85]: https://github.com/MeiNic/MenschAergereDichNicht/pull/85
[#84]: https://github.com/MeiNic/MenschAergereDichNicht/pull/84
[#80]: https://github.com/MeiNic/MenschAergereDichNicht/pull/80
[#73]: https://github.com/MeiNic/MenschAergereDichNicht/pull/73
[#66]: https://github.com/MeiNic/MenschAergereDichNicht/pull/66
[#57]: https://github.com/MeiNic/MenschAergereDichNicht/pull/57
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
