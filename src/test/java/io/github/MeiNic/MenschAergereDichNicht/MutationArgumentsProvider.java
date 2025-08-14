// This file is part of MenschAergereDichNicht.
// Copyright (C) 2023-2025 MeiNic, TastingComb and contributors.

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.MeiNic.MenschAergereDichNicht;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class MutationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {
        Range[] ranges =
                context.getRequiredTestMethod().getAnnotation(MutationSource.class).value();

        switch (ranges.length) {
            case 0:
                return Stream.empty();
            case 1:
                return IntStream.rangeClosed(ranges[0].lower(), ranges[0].upper())
                        .mapToObj(i -> Arguments.of(i));
            case 2:
                return IntStream.rangeClosed(ranges[0].lower(), ranges[0].upper())
                        .boxed()
                        .flatMap(
                                i ->
                                        IntStream.rangeClosed(ranges[1].lower(), ranges[1].upper())
                                                .mapToObj(j -> Arguments.of(i, j)));
            case 3:
                for (int i = 0; i < ranges.length; i++) {
                    Range range = ranges[i];
                    if (range.lower() > range.upper()) {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Lower bound (%d) is greater than upper bound (%d) for"
                                                + " range%d",
                                        range.lower(), range.upper(), i));
                    }
                }
                return IntStream.rangeClosed(ranges[0].lower(), ranges[0].upper())
                        .boxed()
                        .flatMap(
                                i ->
                                        IntStream.rangeClosed(ranges[1].lower(), ranges[1].upper())
                                                .boxed()
                                                .flatMap(
                                                        j ->
                                                                IntStream.rangeClosed(
                                                                                ranges[2].lower(),
                                                                                ranges[2].upper())
                                                                        .mapToObj(
                                                                                k ->
                                                                                        Arguments
                                                                                                .of(
                                                                                                        i,
                                                                                                        j,
                                                                                                        k))));
            default:
                throw new IllegalArgumentException(
                        "Number of ranges is greater than 3. Note: More than three ranges are not"
                                + " supported yet.");
        }
    }
}
